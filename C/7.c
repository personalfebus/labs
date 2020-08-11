#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main (int argc, char ** argv)
{
    long x, i, p;
    long m = 0, pr = 1;
    int k = 0;

    scanf("%ld", &x);

    x = abs(x);

    long z = sqrt(x);
    z++;

    int *a = (int*)malloc(z * (sizeof (int)));

    for (i = 0; i < (z - 2); i++)
        a[i] = 1;

    for (p = 0; p < (z - 2);)
    {
        k = 0;
        for (i = 2; i * (p + 2) - 2 < (z - 2); i++)
            a[i * (p + 2) - 2] = 0;
        for (i = (p + 1); i < (z - 2); i++)
            if (a[i] > 0)
            {
                p = i;
                k = 1;
                break;
            }
        if (k == 0) break;
    }

    k = 0;

    for (i = 0; i < (z - 2); i++)
    {
        if ((a[i] > 0) && (x % (i + 2) == 0))
        {
            m = i + 2;
            pr *= (i + 2);
        }
    }

    for (i = 0; i < (z - 2); i++)
        if (a[i] > 0)
        while ((x % (i + 2) == 0) && (x != (i + 2)))
            x /= (i + 2);
    printf("%ld", x);
    free(a);
    return 0;
}
