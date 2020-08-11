#include <stdio.h>

int main(int argc, char ** argv)
{
    int n, k, i;
    int s = 0, sm = 0;
    scanf("%d", &n);
    int a[n];
    for (i = 0; i<n; i++)
    {
        scanf("%d", &a[i]);
        // printf("%d\n", a[i]);
    }
    scanf("%d", &k);
    for (i = 0; i<k; i++)
        s = s + a[i];
    sm = s;
    for (i = 0; i<(n - k); i++)
    {
        s = s - a[i] + a[i + k];
        if (s > sm)
            sm = s;
        //printf("%d\n", s);
    }
   printf("%d", sm);

    return 0;
}
