#include <stdio.h>

int main(int argc , char ** argv)
{
    long long int a, b, m, b1;
    int c = 0, k = 0, i = 1;
    long long int rez = 0;
    scanf("%lli%lli%lli", &a, &b, &m);
    b1 = b;
    while (b1>0)
    {
        b1 /= 2;
        k++;
        printf("%d ", k);
    }
    int H[k];
    while (b>0)
    {
        H[k-i] = b % 2;
        b /= 2;
        i++;
    }
    for (i=0; i<k; i++)
        printf("%d", H[i]);

    for (i=0; i<k; i++)
    {
        rez = (((rez%m)*(2%m))%m + ((a%m)*(H[i]%m))%m)%m;
        printf(" %lli", rez);
    }


    printf(" %lli", rez);
    return 0;
}
