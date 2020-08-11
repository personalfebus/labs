#include <stdio.h>

int main(int argc, char ** argv)
{
    int i, j, k, m;
    int n = 8;
    int a[n], b[n];

    for (i=0; i<n; i++)
        scanf("%d", &a[i]);

    for (i=0; i<n; i++)
        scanf("%d", &b[i]);

    for (i=0; i<n; i++)
    {
        k = 0;
        m = 0;
        for(j=0; j<n; j++)
            if (a[i] == a[j]) k++;
        for(j=0; j<n; j++)
            if (a[i] == b[j]) m++;
        printf("%d %d \n", k, m);
        if (k != m)
        {
            printf("%s", "no");
            return 0;
        }

    }
    printf("%s", "yes");
    return 0;
}

