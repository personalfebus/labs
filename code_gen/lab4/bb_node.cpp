//
// Created by personalfebus on 09.06.2021.
//

#include <iostream>
#include "bb_node.h"
using namespace std;

bb_node::bb_node(string str)  {
    label = str;
}

void bb_node::add_expr(unique_ptr<ExprAST> expr) {
    exprs.emplace_back(move(expr));
}

void bb_node::add_expr_to_ir(string str) {
    if (ir != "") {
        ir += '\n';
    }
    ir += str;
}

void bb_node::add_edge(int node, int desc) {
    if (desc) {
        out_edges.push_back(node);
    } else {
        in_edges.push_back(node);
    }
}

bb_graph::bb_graph(FunctionAST *f) {
    unique_ptr<bb_node> uniquePtr = make_unique<bb_node>("skip_0");
    unique_ptr<bb_node> uniquePtr1 = make_unique<bb_node>("return_1");
    nodes.push_back(move(uniquePtr));
    nodes.push_back(move(uniquePtr1));
    //to do
    unordered_map<string, int> vars;
    f->proto->codegen(vars);
    cout << "AA\n";
    int tmp_vars_count = vars.size(); //не ноль, а 0 + кол-во переменных в прототипе
    vars.insert(make_pair("tmp_var", tmp_vars_count));
    cout << "BB\n";
    string block = "";
//    bb_node entry_node(block);
//    for (int i = 0; i < body.size(); i++) {
//        string next_stmt = body[i]->codegen(vars);
//        cout << i << ")\n" << next_stmt << endl;
//    }

    int current_node = 0;
    bool need_new_block = false;
    for (int i = 0; i < f->body.size(); i++) {
        if (i == f->body.size() - 1) {
            //return stmt
            string skip_str = "local_return_";
            skip_str += to_string(nodes.size());
            unique_ptr<bb_node> nodePtr = make_unique<bb_node>(skip_str);
            nodePtr->add_edge(1, 1);
            nodes[1]->add_edge(nodes.size(), 0);
            nodes.emplace_back(move(nodePtr));
            if (!need_new_block) {
                nodes[current_node]->add_edge(nodes.size() - 1, 1);
                nodes[nodes.size() - 1]->add_edge(current_node, 0);
            }
            current_node = nodes.size() - 1;
        } else {
            string type = f->body[i]->expr_type();
            if (type == "if") {
//                %ifcond = icmp ne i32 %b12, 0
//                br i1 %ifcond, label %then, label %else
                nodes[current_node]->add_edge(nodes.size(), 1);

                ExprAST *ex = f->body[i].release();
                IfExprAST *exif = (IfExprAST*)ex;

                string cond_str = "condition_";
                cond_str += to_string(nodes.size());
                unique_ptr<bb_node> condPtr = make_unique<bb_node>(cond_str);
                condPtr->add_expr(move(exif->Cond));
                nodes.emplace_back(move(condPtr));
                nodes[nodes.size() - 1]->add_edge(current_node, 0);
                nodes[nodes.size() - 1]->add_edge(nodes.size(), 1); //then
                nodes[nodes.size() - 1]->add_edge(nodes.size() + 1, 1); //else

                string then_str = "then_";
                then_str += to_string(nodes.size());
                unique_ptr<bb_node> thenPtr = make_unique<bb_node>(then_str);
//                thenPtr->exprs = exif->Then;
                for (int j = 0; j < exif->Then.size(); j++) {
                    // rec
                    thenPtr->add_expr(move(exif->Then[j]));
                }
                nodes.emplace_back(move(thenPtr));
                nodes[nodes.size() - 1]->add_edge(nodes.size() - 2, 0);
                nodes[nodes.size() - 1]->add_edge(nodes.size() + 1, 1);

                string else_str = "else_";
                else_str += to_string(nodes.size());
                unique_ptr<bb_node> elsePtr = make_unique<bb_node>(else_str);
                //elsePtr->exprs = exif->Else;
                for (int j = 0; j < exif->Else.size(); j++) {
                    // rec
                    elsePtr->add_expr(move(exif->Else[j]));
                }
                nodes.emplace_back(move(elsePtr));
                nodes[nodes.size() - 1]->add_edge(nodes.size() - 3, 0);
                nodes[nodes.size() - 1]->add_edge(nodes.size(), 1);
                current_node = nodes.size();
                need_new_block = true;
            } else if (type == "for") {
                nodes[current_node]->add_edge(nodes.size(), 1);
                ExprAST *ex = f->body[i].release();
                ForExprAST *exfor = (ForExprAST*)ex;

                string prev_str = "for_previous_";
                prev_str += to_string(nodes.size());
                unique_ptr<bb_node> prevPtr = make_unique<bb_node>(prev_str);
                prevPtr->add_expr(move(exfor->Start));
                nodes.emplace_back(move(prevPtr));
                nodes[nodes.size() - 1]->add_edge(nodes.size(), 1);

                string cond_str = "for_condition_";
                cond_str += to_string(nodes.size());
                unique_ptr<bb_node> condPtr = make_unique<bb_node>(cond_str);
                condPtr->add_expr(move(exfor->End));
                nodes.emplace_back(move(condPtr));
                nodes[nodes.size() - 1]->add_edge(nodes.size() - 2, 0);
                nodes[nodes.size() - 1]->add_edge(nodes.size(), 1);

                //prev --> cond
                //cond --> next
                //cond --> body
                //body --> step
                //step --> cond

                string body_str = "for_body_";
                body_str += to_string(nodes.size());
                unique_ptr<bb_node> bodyPtr = make_unique<bb_node>(body_str);
                for (int j = 0; j < exfor->Body.size(); j++) {
                    // rec
                    bodyPtr->add_expr(move(exfor->Body[j]));
                }
                nodes.emplace_back(move(bodyPtr));
                nodes[nodes.size() - 1]->add_edge(nodes.size() - 2, 0);
                nodes[nodes.size() - 1]->add_edge(nodes.size(), 1);

                string step_str = "for_step_";
                step_str += to_string(nodes.size());
                unique_ptr<bb_node> stepPtr = make_unique<bb_node>(step_str);
                stepPtr->add_expr(move(exfor->Step));
                nodes.emplace_back(move(stepPtr));
                nodes[nodes.size() - 1]->add_edge(nodes.size() - 2, 0);
                nodes[nodes.size() - 1]->add_edge(nodes.size() - 3, 1);
                nodes[nodes.size() - 3]->add_edge(nodes.size(), 1);
                current_node = nodes.size();
                need_new_block = true;
            } else {
                if (need_new_block) {
                    string skip_str = "skip_";
                    skip_str += to_string(nodes.size());
                    unique_ptr<bb_node> nodePtr = make_unique<bb_node>(skip_str);
                    nodes.emplace_back(move(nodePtr));
                }
                nodes[current_node]->add_expr(move(f->body[i]));
            }
            cout << "body_size = " << f->body.size() << "; nodes_size = " << nodes[0]->exprs.size() << endl;
        }
    }
}

void bb_graph::add_node(unique_ptr<bb_node> node) {
    nodes.emplace_back(move(node));
}

void bb_graph::print_graph() {
    cout << nodes.size() << endl;
    printf("\ndigraph {\n");

    for (int i = 0; i < nodes.size(); i++) {
        printf("%s[shape=square];\n", nodes[i]->label.c_str());
    }

    for (int i = 0; i < nodes.size(); i++) {
        for (int j = 0; j < nodes[i]->out_edges.size(); j++) {
            string tmp = nodes[nodes[i]->out_edges[j]]->label;
            printf("%s -> %s;\n", nodes[i]->label.c_str(), tmp.c_str());
        }
    }

    printf("}");
}