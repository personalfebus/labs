int strdiff(char *a, char *b)
{
    int k1 = 0, k2 = 0;

    for (int i = 0; a[i]; i++)
        k1++;
    for (int i = 0; b[i]; i++)
        k2++;
    
    int km;

    if (k1 > k2) km = k1;
    else km = k2;

    int len = km;

    int z = 0;

    for (int j = 0; j < len; j++)
    {
        for (int kkk = 0; kkk < 8; kkk++)
        {
            if ((a[j] & 1) != (b[j] & 1))
                return z;
            z++;
            a[j] = a[j] >> 1;
            b[j] = b[j] >> 1;
        }
    }
    return -1;
}