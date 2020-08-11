#include <stdio.h>

int main(int argc , char ** argv)
{
    long i, k, k1;
    long km = 0;
    long long int x, x1;
    long long int a = 0, b = 1, s = 0, a1 = 0, b1 = 1, s1 = 0;

    scanf("%lli", &x);
    if (x == 0)
    {
    printf("%d", 0);
    return 0;
    }

    x1 = x;
    while (x1>0)
    {
        //printf("%lli ", x1);
        k1 = 0; a1 = 0; b1 = 1; s1 = 0;
        while ((a1+b1)<=x1)
        {
            if (k1>90) break;
            s1 = a1 + b1;
            a1 = b1;
            b1 = s1;
            k1++;
            printf("%lli ", s1);
            printf("%ld\n", k1);
        }
        x1 = x1 - s1;
        //printf("\n%ld \n", k1);
        if (k1>km) km = k1;
    }

    printf("\n %ld \n", km);

    int H[km];

    for (i=0; i<km; i++)
    {
        H[i] = 0;
        //printf("%d ", H[i]);
    }


    while (x>0)
    {
        k = 0; a = 0; b = 1; s = 0;
        while ((a+b)<=x)
        {
            if (k>90) break;
            s = a + b;
            a = b;
            b = s;
            k++;
        }
        //printf("%lli ", s);
        x = x - s;
        H[k-1] = 1;
    }
    km--;
    for (i=km; i>=0; i--)
        printf("%d", H[i]);
    return 0;

}
