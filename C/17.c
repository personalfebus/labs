#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char ** argv)
{
    int vis;
    int shir;

    scanf("%d ", &vis);
    scanf("%d ", &shir);


    /*if (argc != 4)
    {
        printf("Usage: frame <height> <width> <text>");
        return 0;
    } */

    //char *s = argv[4];
    //vis = atoi(argv[2]);
    //shir = atoi(argv[3]);
    char s[100];
    gets(s);

    int k = (int)strlen(s);

    if (k > (shir - 2j))
    {
        printf("Error");
        return 0;
    }

    if (vis < 3)
    {
        printf("Error");
        return 0;
    }

    int shir1;
    int vis1;

    if (vis % 2 == 0) vis1 = vis / 2;
    else vis1 = vis / 2 + 1;

    shir1 = (shir - k) / 2 + 1;

    for (int i = 1; i < vis; i++)
    {
        if (i == 1)
            for (int j = 1; j <= shir; j++)
                printf("*");
        else if (i == vis1)
        {
            printf("*");
            for (int j = 2; j < shir1; j++)
                printf(" ");
            printf("%s", s);
            for (int j = shir1 + k; j < shir; j++)
                printf(" ");
            printf("*");
        }
        else if (i != vis1)
        {
            printf("*");
            for (int j = 2; j < shir; j++)
                printf(" ");
            printf("*");
        }
        //printf("%d %d", i, vis);
        printf("\n");
    }
    for (int j = 1; j <= shir; j++)
        printf("*");
    return 0;
}
