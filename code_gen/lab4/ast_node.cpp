//
// Created by personalfebus on 09.06.2021.
//

#include "ast_node.h"
#include "bb_node.h"
#include <iostream>
#include <unordered_map>

using namespace std;

NumberExprAST::NumberExprAST(int Val) {
    this->Val = Val;
}

string NumberExprAST::codegen(std::unordered_map<std::string, int>& vars) {
    string str = "";
    str += to_string(Val);
    return str;
}

std::string NumberExprAST::expr_type() {
    return "number";
}

VariableExprAST::VariableExprAST(const string &Name) {
    this->Name = Name;
}

const string & VariableExprAST::getName() {
    return Name;
}

string VariableExprAST::codegen(std::unordered_map<std::string, int>& vars) {
//    int version = 0;
//    if (vars.count(Name)) {
//        version = vars[Name];
//        vars[Name] = version + 1;
//    } else {
//        vars.insert(make_pair(Name, 1));
//    }
//    string str = "";
//    str += Name;
//    str += "_v.";
//    str += to_string(version);
//    return str;
    return Name;
}

std::string VariableExprAST::expr_type() {
    return "variable";
}

BinaryExprAST::BinaryExprAST(char Op, unique_ptr<ExprAST> LHS, unique_ptr<ExprAST> RHS) {
    this->Op = Op;
    this->LHS = move(LHS);
    this->RHS = move(RHS);
}

string BinaryExprAST::codegen(std::unordered_map<std::string, int>& vars) {
//    string str = "";
    cout << "binaryexpr\n";
    string ir = "";

    string l = this->LHS->codegen(vars);
    //cout << "l=" << l << ";\n";
    string strl = "";
    if (isdigit(l[0])) {
        strl += l;
    } else {
        int version = 0;
        if (vars.count(l)) {
            version = vars[l];
            vars[l] = version + 1;
        } else {
            vars.insert(make_pair(l, 0));
            ir += "%";
            ir += l;
            ir += "_v.";
            ir += to_string(version);
            version++;
            ir += " = alloca i32\n";
        }
        strl += "%";
        strl += l;
        strl += "_v.";
        strl += to_string(version);
        version++;
    }

    string r = this->RHS->codegen(vars);
    //cout << "r=" << r << ";\n";

    //поставить в начало спец символ и поменять процент в иф
    if (r[0] == '%') {
        switch (Op) {
            case '=': {
                int num = vars["tmp_var"] - 1;
                ir = r;
                ir += "store i32 ";
                ir += "%";
                ir += to_string(num);
                ir += ", i32 ";
                ir += strl;
                break;
            }
            case '+': {
                int num = vars["tmp_var"] - 1;
                ir = r;
                ir += "%";
                ir += to_string(num + 1);
                ir += " = ";
                vars["tmp_var"] = num + 2;
                ir += "add i32 ";
                ir += "%";
                ir += to_string(num);
                ir += ", ";
                ir += strl;
                break;
            }
            case '-': {
                int num = vars["tmp_var"] - 1;
                ir = r;
                ir += "%";
                ir += to_string(num + 1);
                ir += " = ";
                vars["tmp_var"] = num + 2;
                ir += "sub i32 ";
                ir += "%";
                ir += to_string(num);
                ir += ", ";
                ir += strl;
                break;
            }
            default: throw "how did you get there?";
        }
    } else {
        switch (Op) {
            case '=': {
                if (isdigit(r[0])) {
                    ir += "store i32 ";
                    ir += r;
                    ir += ", i32 ";
                    ir += strl;
                } else {
                    int num = vars[r];
                    ir += "store i32 ";
                    ir += "%";
                    ir += r;
                    ir += "_v.";
                    ir += to_string(num);
                    ir += ", i32 ";
                    ir += strl;
                    vars[r] = num + 1;
                }
                break;
            }
            case '+': {
                if (isdigit(r[0])) {
                    int num = vars["tmp_var"] - 1;
                    ir = r;
                    ir += "%";
                    ir += to_string(num + 1);
                    ir += " = ";
                    vars["tmp_var"] = num + 2;
                    ir += "add i32 ";
                    ir += r;
                    ir += ", ";
                    ir += strl;
                } else {
                    int numr = vars[r];
                    int num = vars["tmp_var"] - 1;
                    ir = r;
                    ir += "%";
                    ir += to_string(num + 1);
                    vars["tmp_var"] = num + 2;
                    ir += "add i32 ";
                    ir += "%";
                    ir += r;
                    ir += "_v.";
                    ir += to_string(numr);
                    ir += ", ";
                    ir += strl;
                    vars[r] = numr + 1;
                }
                break;
            }
            case '-': {
                int num = vars[r];
                ir += "sub i32 ";
                ir += "%";
                ir += r;
                ir += "_v.";
                ir += to_string(num);
                ir += ", ";
                ir += strl;
                break;
            }
            default: throw "how did you get there?";
        }
    }
    ir += '\n';
    return ir;
    //to do
}

std::string BinaryExprAST::expr_type() {
    return "binary";
}

IfExprAST::IfExprAST(unique_ptr<ExprAST> Cond, vector<unique_ptr<ExprAST>> Then,
                     vector<unique_ptr<ExprAST>> Else) {
    this->Cond = move(Cond);
    this->Then = move(Then);
    this->Else = move(Else);
}

string IfExprAST::codegen(std::unordered_map<std::string, int>& vars) {
    //to do
    cout << "ifexpr\n";
    return "";
}

std::string IfExprAST::expr_type() {
    return "if";
}

ForExprAST::ForExprAST(unique_ptr<ExprAST> Start, unique_ptr<ExprAST> End, unique_ptr<ExprAST> Step,
                       vector<unique_ptr<ExprAST>> Body) {
    this->Start = move(Start);
    this->End = move(End);
    this->Step = move(Step);
    this->Body = move(Body);
}

string ForExprAST::codegen(std::unordered_map<std::string, int>& vars) {
    //to do
    cout << "forexpr\n";
    return "";
}

std::string ForExprAST::expr_type() {
    return "for";
}

PrototypeAST::PrototypeAST(const string &Name, vector<string> Args) {
    this->Name = Name;
    this->Args = move(Args);
}

const string & PrototypeAST::getName() {
    return Name;
}

std::string PrototypeAST::codegen(std::unordered_map<std::string, int> &vars) {
    string ir = "";
    for (string arg : Args) {
        int version = 0;
        ir += "%";
        ir += arg;
        ir += "_v.";
        ir += to_string(version);
        version++;
        ir += " = alloca i32\n";
        ir += "store i32 %";
        ir += to_string(vars.size());
        ir += ", i32 %";
        ir += arg;
        ir += "_v.";
        ir += to_string(version);
        version++;
        ir += '\n';
        vars.insert(make_pair(arg, version));
    }
    cout << ir << endl;
    return "";
}

FunctionAST::FunctionAST(unique_ptr<PrototypeAST> &proto, vector<unique_ptr<ExprAST>> &body) {
    this->proto = move(proto);
    this->body = move(body);
}

string FunctionAST::codegen() { //return vector<bb_node> массив всех вершин графа
    return "";
}