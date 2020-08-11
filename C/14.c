#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int wcount(char *s)
{
    int k = 0;
    int b = 1;
    int i = 0;
    for (i = 0; (i < 100) && (s[i] != 0); i++)
    {
        if ((s[i] != ' ') && (b == 1))
        {
            k++;
            b = 0;
        }
        if (s[i] == ' ')
            b = 1;
    }
    return k;
}

int main(int argc, char ** argv)
{
    char *s = malloc(100);
    gets(s);
    int l;
    l = wcount(s);
    printf("%d", l);


    return 0;
}
