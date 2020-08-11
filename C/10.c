int maxarray(void *base, unsigned long nel, unsigned long width, int (*compare)(void *a, void *b))
{
        char *p = (char *)base;
        char t = 0;
        for (unsigned long i = 1; i < nel; i++)
        {
                char *s = &p[i*width];
                char *ss = &p[t*width];
                if (compare(s, ss) > 0)
                    t = i;
        }
        return t;
}