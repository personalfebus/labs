#include <stdio.h>

int main(int argc , char ** argv)
{
    int i, j, k, n, m, x16, y16, x2, y2;
    int x = 0, y = 0;

    scanf("%d", &n);

    for (i = 0; i<n; i++)
    {
        k = 0;
        int a;
        scanf("%d", &a);
        for (j = 0; j<a; j++)
        {
            if (j == 0) k++;
            else k = k*2;
            //printf("%d ", k);
        }
        x = x + k;
        //printf("\n%d\n", x);
    }

    scanf("%d", &m);

    for (i = 0; i<m; i++)
    {
        k = 0;
        int b;
        scanf("%d", &b);
        for (j = 0; j<b; j++)
        {
            if (j == 0) k++;
            else k = k*2;
            //printf("%d ", k);
        }
        y = y + k;
        //printf("\n%d\n", x);
    }
    i = 0;
    //printf("\n%d\n", x);
    //printf("\n%d\n", y);
    while ((x>0) || (y>0))
    {
        x16 = x % 16;
        y16 = y % 16;
        x2 = 0;
        y2 = 0;
        j = 1;
        k = 1;
        i = i + 4;
        //printf("\n%d %d", x16, y16);
        while (x16 > 0)
        {
            x2 = x2 + (x16 % 2 * k);
            k = k*10;
            x16 = x16 / 2;
        }
        while (y16 > 0)
        {
            y2 = y2 + (y16 % 2 * j);
            j = j*10;
            y16 = y16 / 2;
        }
        //printf("\n%d %d %d\n", (i - 4), x2, y2);
        for(j=(i-4); j<i; j++)
        {
            //printf("%d\n%d %d\n%d %d\n", j, x2, (x2 % 2), y2, (y2 % 2));
            if ((((x2 % 10) % 2) == 1) && (((y2 % 10) % 2) == 1))
                printf("%d ", (j + 1));
            x2 = x2 / 10;
            y2 = y2 / 10;
        }
        x = x / 16;
        y = y / 16;
    }






   /* scanf("%d", &m);

    for (i=0; i<n; i++)
    {
        scanf("%d", &b);
        n = n + pow(10,b);
    }

    n = n | m;

    printf("%d", n); */

    return 0;
}
