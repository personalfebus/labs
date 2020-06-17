#ifndef LAB7_DECLARATION_H
#define LAB7_DECLARATION_H

#include <iostream>

using namespace std;

template <typename T, int N, int M, bool isEven = (N % 2 == 0)>
class Route{
private:
    int theway[N];
    T **dist_matrix;
public:
    //explicit Route(T (& matrix)[M][M]);
    explicit Route(T **matrix,const int route[N]);
    template <int N2>
    Route<T, N + N2, M> addRoute(const Route<T, N2, M> &other);
    T getDist();
};

template <typename T, int N, int M, bool isEven>
Route<T, N, M, isEven>::Route(T **matrix, const int route[M]){
    dist_matrix = matrix;
    for (int i = 0; i < N; i++){
        theway[i] = route[i];
    }
}

template <typename T, int N, int M, bool isEven>
template <int N2>
Route<T, N + N2, M> Route<T, N, M, isEven>::addRoute(const Route<T, N2, M> &other) {
    int nextway[N + N2];
    for (int i = 0; i < N2; i++){
        nextway[i] = other.theway[i];
    }

    for (int i = N2; i < N + N2; i++){
        nextway[i] = theway[i];
    }
    return Route(dist_matrix, nextway);
}

template <typename T, int N, int M, bool isEven>
T Route<T, N, M, isEven>::getDist() {
    T dist = 0;
    if (N < 2) {
        cout << "Bad route" << '\n';
        return 0;
    }
    for (int i = 1; i < N; i++){
        dist += dist_matrix[theway[i-1]][theway[i]];
    }
    return dist;
}

template <typename T, int N, int M>
class Route<T, N, M, false>{
private:
    int theway[N];
    T **dist_matrix;
public:
    //explicit Route(T (& matrix)[M][M]);
    explicit Route(T **matrix,const int route[N]);
    template <int N2>
    Route<T, N + N2, M> addRoute(const Route<T, N2, M> &other);
    T getDist();
    T getOddsDist();
};

template <typename T, int N, int M>
T Route<T, N, M, false>::getOddsDist() {
    T dist = 0;
    if (N < 3) {
        cout << "Bad route" << '\n';
        return 0;
    }
    for (int i = 1; i < M; i++){
        if (theway[i] < 0) break;
        if (theway[i] % 2 == 0) {
            dist += dist_matrix[theway[i - 1]][theway[i]];
        }
    }
    return dist;
}

template <typename T, int N, int M>
template <int N2>
Route<T, N + N2, M> Route<T, N, M, false>::addRoute(const Route<T, N2, M> &other) {
    int nextway[M];
    int ind = 0;
    for (int i = 0; i < M; i++){
        if (other.theway[i] < 0){
            ind = i;
            break;
        }
        nextway[i] = other.theway[i];
    }

    for (int i = ind; i < M; i++){
        if (theway[i] < 0){
            break;
        }
        nextway[i] = theway[i];
    }
    return Route(dist_matrix, nextway);
}

template <typename T, int N, int M>
T Route<T, N, M, false>::getDist() {
    T dist = 0;
    if (N < 2) {
        cout << "Bad route" << '\n';
        return 0;
    }
    for (int i = 1; i < M; i++){
        if (theway[i] < 0) break;
        dist += dist_matrix[theway[i-1]][theway[i]];
    }
    return dist;
}

template <typename T, int N, int M>
Route<T, N, M, false>::Route(T **matrix, const int route[M]){
    dist_matrix = matrix;
    for (int i = 0; i < M; i++){
        theway[i] = route[i];
    }
}

#endif //LAB7_DECLARATION_H
