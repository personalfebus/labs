#include "Declaration.h"

using namespace std;

int RationalStack::scanner(string s) {
    unsigned int boo = 0;
    unsigned int commapos = 0;
    int nom = 0;
    int odd = 0;
    int k = 0;
    for (unsigned int i = 0; i < s.length(); i++){
        if ((boo == 0) && (((int)s[i] > 47) && ((int)s[i] < 58))){
            boo = 1;
            nom = (int)s[i] - 48;
            //cout << '\n' << "First num = " << s[i];
        }
        else if ((boo == 1) && ((int)s[i] == 44)){
            boo = 2;
            commapos = i;
            //cout << '\n' << "Comma on pos = " << i;
        }
        else if (boo == 1){
            nom = nom * 10 + (int)s[i] - 48;
        }
        else if ((boo == 2) && (((int)s[i] == 32) || (i == s.length() - 1))){
            boo = 0;
            int sz = i - commapos - 1;
            if ((i == s.length() - 1) && ((int)s[i] != 32)){
                sz++;
                odd = odd * 10 + (int)s[i] - 48;
            }
            int den = 1;
            for (int j = 0; j < sz; j++){
                den *= 10;
            }
            //int tmp = den*nom + odd;
            add(new Rational(den*nom + odd, den));
            nom = 0;
            odd = 0;
        }
        else if (boo == 2){
            odd = odd * 10 + (int)s[i] - 48;
        }
        k++;
    }
}

Rational::Rational(int nom, int den) {
    int n = 0;
    nominator = nom;
    denominator = den;
    simplify();
}

Rational::~Rational() {}

//ostream& Rational::operator<<(ostream &os) {
//    os << getNom() << '/' << getDen() << ' ';
//    return os;
//}

Rational& Rational::operator=(const Rational &obj) {
    if (this != &obj){
        nominator = obj.nominator;
        denominator = obj.denominator;
    }
}

int Rational::getNom() {
    return nominator;
}

int Rational::getDen() {
    return denominator;
}

void Rational::simplify() {
    int k = gcd(nominator, denominator);
    nominator /= k;
    denominator /= k;
}

int Rational::gcd(int a, int b) {
    while (b != 0) {
        int tmp = a % b;
        a = b;
        b = tmp;
    }
    return a;
}

//Rational& Rational::operator-(const Rational &other){
//    int k = gcd(denominator, other.denominator);
//    int nextden = denominator * other.denominator / k;
//    cout << "gcd = " << k << "Denominators = " << denominator << ',' << other.denominator << ';' << "Nextden = " << nextden << '\n';
//    Rational(other).nominator = nominator*nextden - other.nominator*nextden;
//    other.denominator = nextden;
//    return ;
//}

RationalStack::RationalStack(const RationalStack &obj) {
    arr = new Rational*[fullsize];
    copy(obj.arr, obj.arr + fullsize, arr);
}

int RationalStack::size() {
    return n;
}

RationalStack::RationalStack() {
    n = 0;
    fullsize = 10;
    arr = new Rational*[10];
}

void RationalStack::add(Rational *num) {
    if (n == fullsize){
        makeArrBigger();
    }
    arr[n++] = num;
}

void RationalStack::makeArrBigger() {
    fullsize *= 2;
    Rational **rational = new Rational*[fullsize];
    copy(arr, arr + fullsize, rational);
    delete [] arr;
    arr = rational;
}

Rational& RationalStack::get(int i) {
    if (i >= n) throw "No such element exception";
    return *arr[i];
}

Rational& RationalStack::operator[](int i) {
    if (i >= n) throw "No such element exception";
    return *arr[i];
}

Rational& RationalStack::pop() {
    if (n == 0) throw "No such element exception";
    return *arr[--n];
}

int RationalStack::compare(Rational obj1, Rational obj2) {
    //return obj1->operator-(*obj2).getNom();
    //Rational lul = obj1 - obj2;
    //cout << "Comp = " << lul.getNom() << '/' << lul.getDen() << '\n';
    //return lul.getNom();
    int k = obj1.gcd(obj1.getDen(), obj2.getDen());
    int nxden = obj1.getDen() * obj2.getDen() / k;
    //cout << obj1.getNom() * nxden << " and " << obj2.getNom() * nxden << " Next den = " << nxden << '\n';
    return obj1.getNom() * nxden / obj1.getDen() - obj2.getNom() * nxden / obj2.getDen();
}

Rational& RationalStack::min() {
    int ind = 0;
    for (int i = 1; i < n; i++){
        if (compare(*arr[ind], *arr[i]) > 0) ind = i;
        //cout << ind << '\n';
    }
    for (int i = ind; i < n - 1; i++){
        swap(arr[i], arr[i + 1]);
    }
    return pop();
}

RationalStack::~RationalStack() {
    delete [] arr;
}

RationalStack& RationalStack::operator=(const RationalStack &obj) {
    if (this != &obj){
        Rational **rational = new Rational*[fullsize];
        copy(arr, arr + fullsize, rational);
        delete [] arr;
        arr = rational;
    }
    return *this;
}