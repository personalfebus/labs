#include <iostream>
#include <llvm/IR/IRBuilder.h>
#include <llvm/IR/Value.h>
#include <llvm/IR/LLVMContext.h>
#include "llvm/IR/Module.h"
#include <llvm/IR/Type.h>
#include "llvm/ADT/StringExtras.h"
#include "llvm/IR/AssemblyAnnotationWriter.h"
#include "llvm/IR/CFG.h"
#include "llvm/IR/Constants.h"
#include "llvm/IR/DebugInfo.h"
#include "llvm/IR/DerivedTypes.h"
#include "llvm/IR/ModuleSlotTracker.h"
#include "llvm/IR/ValueSymbolTable.h"
#include "llvm/Support/Format.h"
#include "llvm/Support/FormattedStream.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/BinaryFormat/Dwarf.h"

using namespace llvm;

static std::unique_ptr<LLVMContext> context;
static std::unique_ptr<Module> module;
static std::unique_ptr<IRBuilder<>> builder;

static void initialize_module() {
    context = std::make_unique<LLVMContext>();
    module = std::make_unique<Module>("lab2", *context);
    builder = std::make_unique<IRBuilder<>>(*context);
}

int main() {
    initialize_module();

    FunctionType *funcType = FunctionType::get(builder->getInt32Ty(), false);
    Function *mainFunc = Function::Create(funcType, Function::ExternalLinkage,"main", module.get());
    BasicBlock *entry = BasicBlock::Create(*context, "entrypoint", mainFunc);
    builder->SetInsertPoint(entry);

    Value *value_1_p = ConstantInt::get(*context,APInt(32, 353));
    Value *value_2_p = ConstantInt::get(*context, APInt(32, 48));

    builder->CreateRet(builder->CreateFAdd(value_1_p, value_2_p, "my_var"));
    module->print(outs(), nullptr);
    return 0;
}
