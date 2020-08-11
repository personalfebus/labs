unsigned long peak(unsigned long nel,
        int (*less)(unsigned long i, unsigned long j))
{
            unsigned long  l = 0;
            unsigned long r = nel - 1;
            unsigned long m;
            
           while (l != r)
            {
                m = r / 2 + l / 2;
                if (less(m, m + 1) > 0)
                    l = m + 1;
                else if (less(m, m - 1) > 0)
                        r = m - 1;
                    else return m;
            }
        return l;
}