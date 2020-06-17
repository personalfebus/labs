#ifndef CLIONPROJECTS_DECLARATION_H
#define CLIONPROJECTS_DECLARATION_H

#include <iostream>
using namespace std;

class Rational{
private:
    int nominator;
    int denominator;
public:
    int getNom();
    int getDen();
    int gcd(int a, int b);
    void simplify();
    //Rational&operator-(const Rational &other);
    Rational&operator=(const Rational &obj);
    //ostream&operator<< (ostream& os);
    Rational(int nom, int den);
    virtual ~Rational();
};

class RationalStack{
private:
    int n;
    int fullsize;
    Rational **arr;
    //Rational **args = new Rational*[10];
public:
    RationalStack();
    int scanner(string s);
    void makeArrBigger();
    int size();
    Rational& get(int i);
    Rational& operator[](int i);
    RationalStack&operator=(const RationalStack &obj);
    void add(Rational *num);
    Rational& pop();
    Rational& min();
    int compare(Rational obj1, Rational obj2);
    RationalStack(const RationalStack &obj);
    virtual ~RationalStack();
};

#endif //CLIONPROJECTS_DECLARATION_H

