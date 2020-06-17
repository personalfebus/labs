#include <iostream>
#include "Declaration.h"
using namespace std;

int main() {
    const int n = 5;
    int arr[n][n];
    int way[n];
    for (int i = 0; i < n; i++){
        if (i < 3) way[i] = i;
        else way[i] = -1;
    }
    for (int i = 0; i < n; i++){
        for (int j = 0; j < n; j++){
            if (i == j) arr[i][j] = 0;
            else arr[i][j] = i + j + 2;
            cout << arr[i][j] << ' ';
        }
        cout << '\n';
    }
    Route<int, 3, n> route(arr, way);
    cout << route.getDist() << '\n';
    cout << route.getOddsDist() << '\n';
    int way2[n];
    for (int i = 0; i < n; i++){
        if (i < 2) way2[i] = i + 3;
        way2[i] = -1;
    }
    Route<int, 2, n> route1(arr, way2);
    Route<int, 5, n> route2 = route.addRoute(route1);
    cout << route2.getDist() << '\n';
    cout << route2.getOddsDist() << '\n';
    return 0;
}