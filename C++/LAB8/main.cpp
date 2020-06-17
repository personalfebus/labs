#include <iostream>
#include <cctype>
#include <string>
#include <string.h>
#include "Declaration.h"

using namespace std;

ostream& operator<< (ostream& os, Rational& rational){
    os << rational.getNom() << '/' << rational.getDen() << ' ';
    return os;
}

int main() {
    RationalStack *stack = new RationalStack();
    string s;
    getline(cin, s, '\n');
    stack->scanner(s);

    cout << "Size = " << stack->size() << '\n';
    cout << stack->get(5);
    //cout << stack->pop();
    cout << stack->min();
    cout << (*stack)[1];
//    cout << '\n';
//    Rational tst = stack->get(0);
//    cout << tst << '\n';
//    stack->add(&tst);
//    cout << stack->pop();
//    RationalStack *nextstack = stack;
//    cout << nextstack->get(0);
    //cout << stack->get(0);
    //cout << stack->get(6);
    //cout << stack->operator[](0);
    return 0;
}