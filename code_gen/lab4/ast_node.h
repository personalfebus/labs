//
// Created by personalfebus on 09.06.2021.
//

#ifndef OPT_LAB4_AST_NODE_H
#define OPT_LAB4_AST_NODE_H

#include <string>
#include <vector>
#include <memory>
#include <unordered_map>

class ExprAST {
public:
    virtual ~ExprAST() = default;
    virtual std::string codegen(std::unordered_map<std::string, int>& vars) = 0; //Value*
    virtual std::string expr_type() = 0;
};


class NumberExprAST : public ExprAST {
    int Val;

public:
    NumberExprAST(int Val);
    std::string codegen(std::unordered_map<std::string, int>& vars) override;
    std::string expr_type() override;
};


class VariableExprAST : public ExprAST {
    std::string Name;

public:
    VariableExprAST(const std::string &Name);
    const std::string &getName();
    std::string codegen(std::unordered_map<std::string, int>& vars) override;
    std::string expr_type() override;
};


class BinaryExprAST : public ExprAST {
    char Op;
    std::unique_ptr<ExprAST> LHS, RHS;

public:
    BinaryExprAST(char Op, std::unique_ptr<ExprAST> LHS,
                  std::unique_ptr<ExprAST> RHS);
    std::string codegen(std::unordered_map<std::string, int>& vars) override;
    std::string expr_type() override;
};


class IfExprAST : public ExprAST {
public:
    std::unique_ptr<ExprAST> Cond;
    std::vector<std::unique_ptr<ExprAST> > Then, Else;

public:
    IfExprAST(std::unique_ptr<ExprAST> Cond, std::vector<std::unique_ptr<ExprAST> > Then,
              std::vector<std::unique_ptr<ExprAST> > Else);
    std::string codegen(std::unordered_map<std::string, int>& vars) override;
    std::string expr_type() override;
};


class ForExprAST : public ExprAST {
public:
    std::unique_ptr<ExprAST> Start, End, Step;
    std::vector<std::unique_ptr<ExprAST>> Body;

public:
    ForExprAST(std::unique_ptr<ExprAST> Start,
               std::unique_ptr<ExprAST> End, std::unique_ptr<ExprAST> Step,
               std::vector<std::unique_ptr<ExprAST> > Body);
    std::string codegen(std::unordered_map<std::string, int>& vars) override;
    std::string expr_type() override;
};


class PrototypeAST {
    std::string Name;
    std::vector<std::string> Args;

public:
    PrototypeAST(const std::string &Name, std::vector<std::string> Args);
    std::string codegen(std::unordered_map<std::string, int>& vars);
    const std::string &getName();
};


class FunctionAST {
public:
    std::unique_ptr<PrototypeAST> proto;
    std::vector<std::unique_ptr<ExprAST>> body;

public:
    FunctionAST(std::unique_ptr<PrototypeAST>& proto,
                std::vector<std::unique_ptr<ExprAST>>& body);
    std::string codegen();
};


#endif //OPT_LAB4_AST_NODE_H
