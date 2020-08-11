#include <stdio.h>


int main(int argc, char ** argv)
{
    int str, sto, elem, ei, ej;
    int sad = 0;

    scanf("%d %d ", &str, &sto);

    int a[str][sto];

    for (int i = 0; i < str; i++)
        for (int j = 0; j < sto; j++)
            scanf("%d", &a[i][j]);

    elem = a[0][0];
    ei = 0;
    ej = 0;

    for (int i = 1; i < str; i++)
        if (a[i][0] < elem)
        {
            elem = a[i][0];
            ei = i;
            ej = 0;
        }

    sad = 1;

    for (int j = 1; j < sto; j++)
        if (a[ei][j] > elem)
        {
            elem = a[ei][j];
            ej = j;
            for (int i = (ei + 1); i < str; i++)
                if (a[i][ej] < elem)
                {
                 sad = 0;
                 break;
                }
            for (int i = (ei - 1); i >= 0; i--)
                if (a[i][ej] < elem)
                {
                    sad = 0;
                    break;
                }
        }

    if (sad == 1) printf("%d %d", ei, ej);
    else printf("none");


    return 0;
}
