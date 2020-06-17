#include <iostream>
#include <iterator>
#include <vector>

using namespace std;

class PolynomIterator;

class Multiplyer;

class Polynom{
private:
    vector<Multiplyer> pol;
    friend class PolynomIterator;
    friend class Multiplyer;
public:
    Polynom();
    PolynomIterator begin();
    PolynomIterator end();
    void addParticle(int a);
};

class Multiplyer{
private:
    int value;
    int mul = 1;
    friend class PolynomIterator;
public:
    explicit Multiplyer(int a);
    int getVal();
    bool operator== (const Multiplyer &other) const;
    bool operator!= (const Multiplyer &other) const;
    Multiplyer& operator= (int a);
    Multiplyer& operator= (const Multiplyer &other);
};

class PolynomIterator: public iterator<forward_iterator_tag, int>{
private:
    const Polynom *polynom;
    int pos = -1;
    bool isDefault = true;
public:
    explicit PolynomIterator(const Polynom &pol);
    PolynomIterator(const Polynom &pol, int i);
    bool isEnd() const;
    bool operator== (const PolynomIterator &other) const;
    bool operator!= (const PolynomIterator &other) const;
    Multiplyer& operator* ();
    PolynomIterator& operator++ ();
    PolynomIterator& operator++ (int);
};

Polynom::Polynom() {}

void Polynom::addParticle(int a) {
    pol.push_back(Multiplyer(a));
}

Multiplyer::Multiplyer(int a) {
    value = a;
    mul = 1;
}

bool Multiplyer::operator==(const Multiplyer &other) const {
    return value == other.value;
}

bool Multiplyer::operator!=(const Multiplyer &other) const {
    return !(*this == other);
}

Multiplyer& Multiplyer::operator=(int a) {
    value = a;
}

Multiplyer& Multiplyer::operator=(const Multiplyer &other) {
    value = other.value;
    mul = other.mul;
}

int Multiplyer::getVal() {
    return mul * value;
}

PolynomIterator Polynom::begin() {
    return PolynomIterator(*this, 0);
}

PolynomIterator Polynom::end() {
    return PolynomIterator(*this, pol.size());
}

PolynomIterator::PolynomIterator(const Polynom &pol) {
    pos = -1;
    polynom = &pol;
}

PolynomIterator::PolynomIterator(const Polynom &pol, int i) {
    pos = i;
    polynom = &pol;
    isDefault = false;
}

bool PolynomIterator::isEnd() const{
    return polynom->pol.size() == pos;
}

bool PolynomIterator::operator==(const PolynomIterator &other) const {
    return (isDefault && other.isDefault) ||
            (isDefault && other.isEnd()) ||
            (isEnd() && other.isDefault) ||
            ((polynom == other.polynom) && (pos == other.pos));
}

bool PolynomIterator::operator!=(const PolynomIterator &other) const {
    return !(*this == other);
}

Multiplyer& PolynomIterator::operator*() {
    if (isDefault) throw "not initialized iterator";
    Multiplyer *tmp = new Multiplyer(0);
    tmp->value = polynom->pol[pos].value;
    tmp->mul = pos;
    //Multiplyer tmp = polynom->pol[pos];
    //tmp.mul = pos;
    return *tmp;
}

PolynomIterator& PolynomIterator::operator++() {
    if (isDefault) throw "not initialized iterator";
    if (pos == polynom->pol.size()){
        cout << "Reached last Elem" << '\n';
        throw overflow_error("Bad");
        //return *this;
    }
    pos++;
    return *this;
}

PolynomIterator& PolynomIterator::operator++(int) {
    //PolynomIterator tmp(*polynom);
    PolynomIterator *tmp = new PolynomIterator(*polynom, pos);
    operator++();
    return *tmp;
}

int main(){
    Polynom polynom;
    PolynomIterator it(polynom, 0);

    for (int i = 1; i < 5; i++) {
        polynom.addParticle(i);
    }

    for (auto ter = polynom.begin(); ter != polynom.end();){
        cout << (*ter++).getVal() << endl;
    }
    cout << endl;
    try {
        for (int i = 0; i < 10; i++) {
            cout << (*it++).getVal() << endl;
        }
    }
    catch (overflow_error &err){
        cout << err.what() << endl;
    }

    return 0;
}