//
// Created by personalfebus on 09.06.2021.
//

#ifndef OPT_LAB4_BB_NODE_H
#define OPT_LAB4_BB_NODE_H

#include <string>
#include <vector>
#include <memory>
#include "ast_node.h"

using namespace std;

class bb_node {
public:
    string label;
    vector<int> in_edges;
    vector<int> out_edges;
    string ir;
    vector<std::unique_ptr<ExprAST>> exprs;
    unordered_map<std::string, int> vars;

public:
    bb_node(std::string str);
    void add_expr_to_ir(std::string str);
    void add_edge(int node, int desc); //desc == 0 => in; desc == 1 => out
    void add_expr(unique_ptr<ExprAST> expr);
};

class bb_graph {
public:
    vector<unique_ptr<bb_node>> nodes;
    int tmp_var_count = 0;
    //0 node = start node
    //1 node = global return node
public:
    bb_graph(FunctionAST *f);
    void add_node(unique_ptr<bb_node> node);
    void print_graph();
};


#endif //OPT_LAB4_BB_NODE_H
