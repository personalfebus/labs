import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;

public class SparseSet<T extends Hintable> extends AbstractSet<T> {
    private int n;
    private ArrayList<T> dense;

    public SparseSet() {
        n = 0;
        dense = new ArrayList<>();
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public Iterator<T> iterator(){
        Iterator<T> it = new Iterator<T>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < n;
            }

            @Override
            public T next() {
                if (hasNext()) return dense.get(i++);
                else {
                    i = 0;
                    return dense.get(i);
                }
            }

            public void remove(){
                n--;
                //sparse[dense[i - 1]] = 0;
                dense.get(i - 1).setHint(0);
                //sparse[dense[n]] = i - 1;
                dense.get(n).setHint(i - 1);
                //dense[i - 1] = dense[n];
                dense.set(i - 1, dense.get(n));
                i--;
            }
        };
        return it;
    }

    @Override
    public boolean add(T x) {
        boolean boo = false;
//        if ((x < dense.length) && ((!contains(x)) || (size() == 0))){
//            dense[n] = x;
//            sparse[x] = n;
//            n++;
//            boo = true;
//        }
        if ((size() == 0) || !contains(x)){
            dense.add(x);
            x.setHint(n);
            n++;
            boo = true;
        }
        return boo;
    }

    @Override
    public boolean contains(Object o) {
        //int x = (int)o;
        T x = (T)o;
        //return (x < dense.length) && (dense[sparse[x]] == x);
        return dense.get(x.hint()) == x;
    }

    @Override
    public boolean remove(Object o) {
        boolean boo = false;
//        int x = (int)o;
//        if ((size() > 0) && (contains(x))) {
//            n--;
//            dense[sparse[x]] = dense[n];
//            sparse[dense[n]] = sparse[x];
//            sparse[x] = 0;
//            boo = true;
//        }
        T x = (T)o;
        if ((size() > 0) && (contains(x))){
            n--;
            dense.set(x.hint(), dense.get(n));
            dense.get(n).setHint(x.hint());
            x.setHint(0);
            boo = true;
        }
        return boo;
    }

    @Override
    public void clear(){
        n = 0;
    }
}