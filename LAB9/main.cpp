#include <iostream>
#include <vector>
#include "declaration.h"
using namespace std;

int main() {
//    vector<int> vector1 = {3, 4, 5};
//    vector<int> vector2 = {1, 2};
//    vector1.insert(vector1.begin() + 3, vector2.begin(), vector2.end());
//    for (int i = 0; i < vector1.size(); i++){
//        cout << vector1[i] << ' ';
//    }
    Parcel<int, double> prcl;
    prcl += 5;
    prcl += 3.14;
    prcl += 7;
    prcl.print();
    Parcel<int, double> a2;
    a2 += 1.5;
    a2 += 3;
    a2 += 2.2;
    a2 += 90;
    //prcl += a2;
    prcl(3.14, a2);
    prcl.print();
    return 0;
}