#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *fibstr(int n)
{
    int a = 1, b = 1, c = 2;
    for (int i = 2; i < n; i++)
    {
        c = a + b;
        a = b;
        b = c;
    }
    int k1 = 1;
    int k2 = 1;
    int k3 = 1;
    char *s = malloc(a + 1);
    char *ss = malloc(c + 1);
    char *sss = malloc(c + 1);
    s[0] = 'a'; s[1] = '\0';
    if (n == 1)
    {
        free(ss);
        free(sss);
        return s;
    }
    ss[0] = 'b'; ss[1] = '\0';
    sss[0] = 'b'; sss[1] = '\0';
    //printf("%s %s\n", s, ss);
    for (int i = 2; i < n; i++)
    {
        strncpy(sss,ss,c);
        k1 = k2;
        k2 = 0;
        strncpy(ss,s,c);
        for (int j = 0; (j < c) && sss[j]; j++)
        {
            ss[k1 + j] = sss[j];
            k2++;
        }
        strncpy(s,sss,a);
        //printf("%s %s %s\n", s, ss, sss);
        k3 = k1 + k2;
    }
    s[k1] = '\0';
    ss[k3] = '\0';
    //printf("%s\n%s\n", s, ss);
    free(s);
    free(sss);
    return ss;
}

int main(int argc, char ** argv)
{
    /*int k;
    int n = 4;
    k = fibstr(n);
    printf("%d", k);*/

    char *p;
    int n;
    scanf("%d", &n);

    p = (char *)fibstr(n);

    printf("%s \n", p);

    free(p);
    return 0;
}
