void revarray(void *base, unsigned long nel, unsigned long width)
{
    char *p = (char *)base;
    char *t;

    for (unsigned long i = 0; i < (nel / 2); i++)
        for (unsigned long j = 0; j < width; j++)
        {
            t = p[(nel - i -1) * width + j];
            p[(nel - i -1) * width + j] = p[i * width + j];
            p[i * width + j] = t;
        }
}