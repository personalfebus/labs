#ifndef LAB9_DECLARATION_H
#define LAB9_DECLARATION_H

#include <iostream>
#include <vector>

using namespace std;

template <typename T, typename N>
class Parcel{
private:
    vector<T> terminals;
    vector<N> notterminals;
    vector<bool> order;
public:
    explicit Parcel();
    void print();
    Parcel operator+ (const Parcel &other) const;
    Parcel& operator+= (const Parcel &other);
    Parcel& operator+= (T term);
    Parcel& operator+= (N notterm);
    Parcel& operator() (N x, Parcel &p);
};

template <typename T, typename N>
Parcel<T, N>::Parcel() {}

template <typename T, typename N>
Parcel<T, N>& Parcel<T, N>::operator+=(const Parcel<T, N> &other) {
    for (int i = 0; i < other.order.size(); i++){
        if (i < other.terminals.size()) this->terminals.push_back(other.terminals[i]);
        if (i < other.notterminals.size()) this->notterminals.push_back(other.notterminals[i]);
        this->order.push_back(other.order[i]);
    }
    return *this;
}

template <typename T, typename N>
Parcel<T, N>& Parcel<T, N>::operator+=(T term) {
    this->terminals.push_back(term);
    this->order.push_back(true);
    return *this;
}

template <typename T, typename N>
Parcel<T, N>& Parcel<T, N>::operator+=(N notterm) {
    this->notterminals.push_back(notterm);
    this->order.push_back(false);
    return *this;
}

template <typename T, typename N>
Parcel<T, N>& Parcel<T, N>::operator()(N x, Parcel &P) {
    int ind = notterminals.size();
    for (int i = 0; i < notterminals.size(); i++){
        //cout << "cycle " << i << " of " << notterminals.size() << '\n';
        if (notterminals[i] == x){
            //cout << "found on pos " << ind << '\n';
            ind = i;
            break;
        }
    }
    int posord = 0;
    int termind = 0;
    int j = -1;
    int k = 0;
    int i = 0;
    for (i = 0; i < order.size(); i++){
        if (j == ind) {
            //cout << "found on p00s " << i << '\n';
            break;
        }
        if (this->order[i]) k++;
        else j++;
    }
    posord = i - 1;
    termind = k;
    notterminals[ind] = P.notterminals[0];
    order[posord] = P.order[0];
    notterminals.insert(notterminals.begin()+ ind + 1, P.notterminals.begin()+1, P.notterminals.end());
    terminals.insert(terminals.begin() + termind, P.terminals.begin(), P.terminals.end());
    order.insert(order.begin() + posord + 1, P.order.begin()+1, P.order.end());
    return *this;
}

template <typename T, typename N>
Parcel<T, N> Parcel<T, N>::operator+(const Parcel &other) const {
    Parcel<T, N> tmp;
    tmp.order = order;
    tmp.terminals = terminals;
    tmp.notterminals = notterminals;
    tmp.order.insert(tmp.order.begin() + tmp.order.size(), other.order.begin(), other.order.end);
    tmp.terminals.insert(tmp.terminals.begin() + tmp.terminals.size(), other.terminals.begin(), other.terminals.end);
    tmp.notterminals.insert(tmp.notterminals.begin() + tmp.notterminals.size(), other.notterminals.begin(), other.notterminals.end);
    return tmp;
}

template <typename T, typename N>
void Parcel<T, N>::print() {
    int tt = 0;
    int nn = 0;
    for (int i = 0; i < order.size(); i++){
        if (order[i]){
            cout << terminals[tt] << ' ';
            tt++;
        }
        else {
            cout << notterminals[nn] << ' ';
            nn++;
        }
    }
    cout << '\n';
}
#endif //LAB9_DECLARATION_H
