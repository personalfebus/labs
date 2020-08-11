#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *concat(char **s, int n)
{
    char *nw = malloc(100);
    int k = 0;
    printf("%s \n", s[1]);
    for (int i = 0; i < n; i++)
        for (int j = 0; (j < 100) && s[i][j]; j++)
        {
            nw[k] = s[i][j];
            k++;
        }
    nw[k] = 0;
    return nw;
}

int main(int argc, char ** argv)
{
    int n;
    int k = 0;
    scanf("%d ", &n);

    char **s = (char **)malloc(100);

    for (int i = 0; i < n; i++)
    {
        s[i] = malloc(100);
        gets(s[i]);
    }

    printf("%s \n", s[0]);

    char *tt = concat(s,n);

    printf("%s \n", tt);
    for (int i = 0; i < n; i++)
        free(s[i]);
    free(s);
    free(tt);
    return 0;
}
