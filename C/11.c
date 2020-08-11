unsigned long binsearch(unsigned long nel, int (*compare)(unsigned long i))
{
    unsigned long t = nel;
    unsigned long l = 0, r = nel - 1;
    unsigned long m;
    while (r != l)
    {
        if (r - l < 2) m = 1;
        else m = (r - l) / 2;
        if (compare(l + m) == 0)
        {
            t = l + m;
            break;
        }
        else if (compare(l + m) < 0)
            l += m;
            else r -= m;
    }
    return t;
}