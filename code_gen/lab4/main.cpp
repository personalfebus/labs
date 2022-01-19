#include <fstream>
#include <streambuf>
#include <iostream>

#include <map>
#include <string>
#include <vector>
#include <memory>

#include "bb_node.h"
#include "ast_node.h"

//tokens
enum Token {
    tok_error = 0,
    tok_eof = -1,

    tok_ident = -2,
    tok_number = -3,

    tok_if = -4,
    tok_for = -5,
    tok_return = -6,

    tok_lparen = -7,
    tok_rparen = -8,
    tok_lfig = -9,
    tok_rfig = -10,
    tok_comma = -11,

    tok_eq = -12,
    tok_plus = -13,
    tok_minus = -14,
    tok_else = -15
};

static std::vector<bb_node> bb_nodes;

//buffer for input sequence
static std::string seq;
static int cur_pos = 0;

//current token value
static std::string StrVal;
static int NumVal;

static char next_char() {
    if (cur_pos >= seq.length())
        return '$';
    char c = seq[cur_pos++];
    return c;
}

static Token get_token() {
    static char LastChar = ' ';
    while (isspace(LastChar)) {
        LastChar = next_char();
    }

    if (isalpha(LastChar)) {
        StrVal = LastChar;
        while (isalnum((LastChar = next_char())))
            StrVal += LastChar;
        if (StrVal == "if")
            return tok_if;
        if (StrVal == "for")
            return tok_for;
        if (StrVal == "return")
            return tok_return;
        if (StrVal == "else")
            return tok_else;
        return tok_ident;
    }

    if (isdigit(LastChar)) {
        std::string NumStr;
        do {
            NumStr += LastChar;
            LastChar = next_char();
        } while (isdigit(LastChar));

        NumVal = strtol(NumStr.c_str(), nullptr, 10);
        return tok_number;
    }

    Token t = tok_error;
    switch (LastChar){
        case '$': t = tok_eof; break;
        case '(': t = tok_lparen; break;
        case ')': t = tok_rparen; break;
        case '{': t = tok_lfig; break;
        case '}': t = tok_rfig; break;
        case ',': t = tok_comma; break;
        case '=': t = tok_eq; break;
        case '+': t = tok_plus; break;
        case '-': t =  tok_minus; break;
    }
    LastChar = next_char();

    return t;
}

static Token cur_tok = tok_eof;
static Token getNextToken() { return cur_tok = get_token(); }

void init_scanner(const char* filepath) {
    cur_pos = 0;
    std::ifstream t(filepath);
    seq = std::string((std::istreambuf_iterator<char>(t)),
                      std::istreambuf_iterator<char>());
}

//// ast classes

//class ExprAST {
//public:
//    virtual ~ExprAST() = default;
//
//    virtual std::string codegen() = 0; //Value*
//};
//
//
//class NumberExprAST : public ExprAST {
//    int Val;
//
//public:
//    NumberExprAST(int Val) : Val(Val) {}
//
//    std::string codegen() override;
//};
//
//
//class VariableExprAST : public ExprAST {
//    std::string Name;
//
//public:
//    VariableExprAST(const std::string &Name) : Name(Name) {}
//    const std::string &getName() const { return Name; }
//
//    std::string codegen() override;
//};
//
//
//class BinaryExprAST : public ExprAST {
//    char Op;
//    std::unique_ptr<ExprAST> LHS, RHS;
//
//public:
//    BinaryExprAST(char Op, std::unique_ptr<ExprAST> LHS,
//                  std::unique_ptr<ExprAST> RHS)
//            : Op(Op), LHS(std::move(LHS)), RHS(std::move(RHS)) {}
//
//    std::string codegen() override;
//};
//
//
//class IfExprAST : public ExprAST {
//public:
//    std::unique_ptr<ExprAST> Cond;
//    std::vector<std::unique_ptr<ExprAST> > Then, Else;
//
//public:
//    IfExprAST(std::unique_ptr<ExprAST> Cond, std::vector<std::unique_ptr<ExprAST> > Then,
//              std::vector<std::unique_ptr<ExprAST> > Else)
//            : Cond(std::move(Cond)), Then(std::move(Then)), Else(std::move(Else)) {}
//
//    std::string codegen() override;
//};
//
//
//class ForExprAST : public ExprAST {
//    std::unique_ptr<ExprAST> Start, End, Step;
//    std::vector<std::unique_ptr<ExprAST> > Body;
//
//public:
//    ForExprAST(std::unique_ptr<ExprAST> Start,
//               std::unique_ptr<ExprAST> End, std::unique_ptr<ExprAST> Step,
//               std::vector<std::unique_ptr<ExprAST> > Body)
//            : Start(std::move(Start)), End(std::move(End)),
//              Step(std::move(Step)), Body(std::move(Body)) {}
//
//    std::string codegen() override;
//};
//
//
//class PrototypeAST {
//    std::string Name;
//    std::vector<std::string> Args;
//
//public:
//    PrototypeAST(const std::string &Name, std::vector<std::string> Args)
//            : Name(Name), Args(std::move(Args)) {}
//
//    std::string codegen();
//    const std::string &getName() const { return Name; }
//};
//
//
//class FunctionAST {
//public:
//    std::unique_ptr<PrototypeAST> proto;
//    std::vector<std::unique_ptr<ExprAST>> body;
//
//public:
//    FunctionAST(std::unique_ptr<PrototypeAST>& proto,
//                std::vector<std::unique_ptr<ExprAST>>& body)
//            : proto(std::move(proto)), body(std::move(body)) {}
//
//    std::string codegen();
//};

//// llvm code
//
//static AllocaInst *CreateEntryBlockAlloca(Function *TheFunction,
//                                          StringRef VarName) {
//    IRBuilder<> TmpB(&TheFunction->getEntryBlock(),
//                     TheFunction->getEntryBlock().begin());
//    return TmpB.CreateAlloca(Type::getInt32Ty(*context), nullptr, VarName);
//}
//
//std::string NumberExprAST::codegen() {
//    return ConstantInt::get(*context, APInt(32, Val, false));
//}
//
//std::string VariableExprAST::codegen() {
//    AllocaInst *A = named_values[Name];
//    if (!A)
//        A = CreateEntryBlockAlloca(builder->GetInsertBlock()->getParent(), Name);
//    named_values[Name] = A;
//    return builder->CreateLoad(A->getAllocatedType(), A, Name.c_str());
//}
//
//std::string BinaryExprAST::codegen() {
//    if (Op == '='){
//        VariableExprAST *LHSE = static_cast<VariableExprAST *>(LHS.get());
//        if (!LHSE)
//            return nullptr;
//
//        Value *Val = RHS->codegen();
//        if (!Val)
//            return nullptr;
//
//        Value *Variable = named_values[LHSE->getName()];
//        if (!Variable){
//            Variable = CreateEntryBlockAlloca(builder->GetInsertBlock()->getParent(), LHSE->getName());
//            named_values[LHSE->getName()] = static_cast<AllocaInst*>(Variable);
//        }
//
//        builder->CreateStore(Val, Variable);
//        return Val;
//    }
//
//    Value *L = LHS->codegen();
//    Value *R = RHS->codegen();
//    if (!L || !R)
//        return nullptr;
//
//    switch (Op) {
//        case '+':
//            return builder->CreateAdd(L, R, "addtmp");
//        case '-':
//            return builder->CreateSub(L, R, "subtmp");
//        default:
//            return nullptr;
//    }
//}
//
//
//std::string IfExprAST::codegen() {
//    Value *CondV = Cond->codegen();
//    if (!CondV)
//        return nullptr;
//
//    CondV = builder->CreateICmpNE(
//            CondV, ConstantInt::get(*context, APInt(32, 0, false)), "ifcond");
//
//    Function *TheFunction = builder->GetInsertBlock()->getParent();
//
//    BasicBlock *ThenBB = BasicBlock::Create(*context, "then", TheFunction);
//    BasicBlock *ElseBB = BasicBlock::Create(*context, "else");
//    BasicBlock *MergeBB = BasicBlock::Create(*context, "ifcont");
//
//    builder->CreateCondBr(CondV, ThenBB, ElseBB);
//
//    builder->SetInsertPoint(ThenBB);
//
//    for(auto &expr : Then){
//        expr->codegen();
//    }
//
//    builder->CreateBr(MergeBB);
//    ThenBB = builder->GetInsertBlock();
//
//    TheFunction->getBasicBlockList().push_back(ElseBB);
//    builder->SetInsertPoint(ElseBB);
//
//    for(auto &expr : Else){
//        expr->codegen();
//    }
//
//    builder->CreateBr(MergeBB);
//    ElseBB = builder->GetInsertBlock();
//
//    TheFunction->getBasicBlockList().push_back(MergeBB);
//    builder->SetInsertPoint(MergeBB);
//
//    return nullptr;
//}
//
//std::string ForExprAST::codegen() {
//    Function *TheFunction = builder->GetInsertBlock()->getParent();
//
//
//    Value *StartVal = Start->codegen();
//    if (!StartVal)
//        return nullptr;
//
//    BasicBlock *LoopBB = BasicBlock::Create(*context, "loop", TheFunction);
//
//    builder->CreateBr(LoopBB);
//
//    builder->SetInsertPoint(LoopBB);
//
//    for(auto &expr : Body){
//        expr->codegen();
//    }
//
//    Value *StepVal = nullptr;
//    if (Step) {
//        StepVal = Step->codegen();
//        if (!StepVal)
//            return nullptr;
//    } else {
//        StepVal = ConstantInt::get(*context, APInt(32, 1, false));
//    }
//
//    Value *EndCond = End->codegen();
//    if (!EndCond)
//        return nullptr;
//
//    EndCond = builder->CreateICmpNE(
//            EndCond, ConstantInt::get(*context, APInt(32, 0, false)), "loopcond");
//
//    BasicBlock *AfterBB =
//            BasicBlock::Create(*context, "afterloop", TheFunction);
//
//    builder->CreateCondBr(EndCond, LoopBB, AfterBB);
//
//    builder->SetInsertPoint(AfterBB);
//
//    return Constant::getNullValue(Type::getInt32Ty(*context));
//}
//
//
//Function *PrototypeAST::codegen() {
//    std::vector<Type *> Ints(Args.size(), Type::getInt32Ty(*context));
//    FunctionType *FT =
//            FunctionType::get(Type::getInt32Ty(*context), Ints, false);
//
//    Function *F =
//            Function::Create(FT, Function::ExternalLinkage, Name, mod.get());
//
//    unsigned Idx = 0;
//    for (auto &Arg : F->args())
//        Arg.setName(Args[Idx++]);
//
//    return F;
//}
//
//Function *FunctionAST::codegen() {
//
//    Function *TheFunction = proto->codegen();
//
//    if (!TheFunction)
//        return nullptr;
//
//    BasicBlock *BB = BasicBlock::Create(*context, "entry", TheFunction);
//    builder->SetInsertPoint(BB);
//
//    named_values.clear();
//    for (auto &Arg : TheFunction->args())
//    {
//        AllocaInst *Alloca = CreateEntryBlockAlloca(TheFunction, Arg.getName());
//        builder->CreateStore(&Arg, Alloca);
//        named_values[std::string(Arg.getName())] = Alloca;
//
//    }
//    Value *RetVal;
//    for(std::unique_ptr<ExprAST> &expr: body)
//    {
//        RetVal = expr->codegen();
//    }
//    if (!RetVal){
//        return nullptr;
//    }
//    builder->CreateRet(RetVal);
//
//    verifyFunction(*TheFunction);
//
//    return TheFunction;
//
//}

//parse

//Kinda Grammar:

// program = func_def {func_body}
//func_def = ident (var_list)
//var_list =  ident var_list1
//var_list1 = , ident var_list1 | .
// func_body = expr expr1
//expr1 = expr expr1 | return number | var
//expr = assign | for | if
// assign = ident '=' bin_expr
//for = 'for' (start, cond, step) assign | bin_expr
// if = 'if' (bin_expr) assign | bin_expr

void assertion(Token t) {
    if (cur_tok != t){
        throw "Parsing error";
    }
}

std::vector<std::string> ParseVars();
std::vector<std::unique_ptr<ExprAST>> ParseBody();
std::unique_ptr<ExprAST> ParseExpr();
std::unique_ptr<ExprAST> ParseBinOp();
std::unique_ptr<ExprAST> ParseEq();

FunctionAST* Parse() {
    std::unique_ptr<PrototypeAST> proto;
    getNextToken();

    assertion(tok_ident);
    std::string name = StrVal;

    getNextToken();
    std::vector<std::string> args = ParseVars();

    proto = std::make_unique<PrototypeAST>(name, args);

    FunctionAST *f;
    std::vector<std::unique_ptr<ExprAST>> body = ParseBody();
    f = new FunctionAST(proto, body);
    assertion(tok_eof);
    return f;
}


std::vector<std::string> ParseVars()
{
    std::vector<std::string> args;
    assertion(tok_lparen);
    getNextToken();
    while(cur_tok != tok_rparen && cur_tok != tok_eof)
    {
        assertion(tok_ident);
        args.emplace_back(StrVal);
        getNextToken();
        if (cur_tok == tok_comma)
            getNextToken();
    }
    getNextToken();
    return args;
}

std::vector<std::unique_ptr<ExprAST>> ParseBody()
{
    assertion(tok_lfig);
    getNextToken();
    std::vector<std::unique_ptr<ExprAST>> exprs;
    while (cur_tok == tok_ident || cur_tok == tok_for || cur_tok == tok_if)
    {
        exprs.push_back(ParseExpr());
    }
    assertion(tok_return);
    getNextToken();
    exprs.push_back(ParseBinOp());
    assertion(tok_rfig);
    getNextToken();
    return exprs;
}

std::unique_ptr<ExprAST> ParseEq()
{
    assertion(tok_ident);
    std::unique_ptr<ExprAST> LHS =  std::make_unique<VariableExprAST>(StrVal);
    getNextToken();
    assertion(tok_eq);
    getNextToken();
    std::unique_ptr<ExprAST> RHS = ParseBinOp();
    return std::make_unique<BinaryExprAST>('=', std::move(LHS), std::move(RHS));
}

std::unique_ptr<ExprAST> ParseExpr()
{
    if (cur_tok == tok_ident){
        return ParseEq();
    }
    if (cur_tok == tok_for){
        getNextToken();
        assertion(tok_lparen);
        getNextToken();
        std::unique_ptr<ExprAST> start = ParseEq();
        assertion(tok_comma);
        getNextToken();
        std::unique_ptr<ExprAST> cond = ParseBinOp();
        assertion(tok_comma);
        getNextToken();
        std::unique_ptr<ExprAST> step = ParseEq();
        assertion(tok_rparen);
        getNextToken();
        assertion(tok_lfig);
        getNextToken();
        std::vector<std::unique_ptr<ExprAST> > body;
        while (cur_tok != tok_rfig && cur_tok != tok_eof)
        {
            body.push_back(std::move(ParseEq()));
        }
        assertion(tok_rfig);
        getNextToken();
        return std::make_unique<ForExprAST>(std::move(start), std::move(cond), std::move(step), std::move(body));
    }
    if (cur_tok == tok_if){
        getNextToken();
        assertion(tok_lparen);
        getNextToken();
        std::unique_ptr<ExprAST> cond = ParseBinOp();
        assertion(tok_rparen);
        getNextToken();
        assertion(tok_lfig);
        getNextToken();
        std::vector<std::unique_ptr<ExprAST> > body;
        while (cur_tok != tok_rfig && cur_tok != tok_eof)
        {
            body.push_back(std::move(ParseEq()));
        }
        assertion(tok_rfig);
        getNextToken();
        assertion(tok_else);
        getNextToken();
        assertion(tok_lfig);
        getNextToken();
        std::vector<std::unique_ptr<ExprAST> > else_body;
        while (cur_tok != tok_rfig && cur_tok != tok_eof)
        {
            else_body.push_back(std::move(ParseEq()));
        }
        assertion(tok_rfig);
        getNextToken();
        return std::make_unique<IfExprAST>(std::move(cond), std::move(body), std::move(else_body));
    }
    return nullptr;
}

std::unique_ptr<ExprAST> ParseBinOp()
{
    if (cur_tok == tok_ident){
        std::string var(StrVal);
        getNextToken();
        if (cur_tok != tok_plus && cur_tok != tok_minus){
            return std::make_unique<VariableExprAST>(var);
        }
        char op = cur_tok == tok_plus ? '+' : '-';
        getNextToken();
        return std::make_unique<BinaryExprAST>(op, std::make_unique<VariableExprAST>(var), ParseBinOp());
    }
    else if (cur_tok == tok_number)
    {
        int number = NumVal;
        getNextToken();
        if (cur_tok != tok_plus && cur_tok != tok_minus){
            return std::make_unique<NumberExprAST>(number);
        }
        char op = cur_tok == tok_plus ? '+' : '-';
        getNextToken();
        return std::make_unique<BinaryExprAST>(op, std::make_unique<NumberExprAST>(number), ParseBinOp());
    }
    return nullptr;
}

bb_node* make_bb(FunctionAST *f) {
    for (int i = 0; i < f->body.size(); i++) {
        f->body[i];
    }
    return nullptr;
}

int main(int argc, char *argv[])
{
    using namespace std;
//    if (argc == 2){
        init_scanner("/home/personalfebus/opt_lab4/input.txt");
//        initialize_module();
        FunctionAST* f = Parse();
        bb_graph *graph = new bb_graph(f);
        //f->codegen(graph);
//        mod->print(errs(), nullptr);
//    } else {
//        cout<<"One argument(filepath) expected"<<endl;
//    }
    graph->print_graph();
    return 0;
}
