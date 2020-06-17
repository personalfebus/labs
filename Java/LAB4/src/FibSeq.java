import java.util.Iterator;

public class FibSeq implements Iterable<Integer[]>{
    private Integer[] arr;
    int pos;

    public String toString(Integer[] arr) {
        StringBuilder s = new StringBuilder();
        s.ensureCapacity(32);
        for (int i = 0; i < arr.length; i++) {
            s.append(arr[i]);
            s.append(" ");
        }
        return s.toString();
    }

    public FibSeq(Integer[] mas){
        this.arr = mas;
        pos = this.arr.length;
    }

    private int fibGen(int n){
        if (n == 1) return 1;
        if (n == 2) return 1;
        else return this.fibGen(n - 1) + this.fibGen(n - 2);
    }

    private void extendArr(){
        Integer[] mas = new Integer[this.arr.length * 2];
        for (int i = 0; i < this.arr.length; i++){
            mas[i] = this.arr[i];
        }
        this.arr = mas;
    }

    public void addArr(int a){
        if (this.pos == this.arr.length){
            this.extendArr();
        }
        this.arr[this.pos] = a;
        this.pos++;
    }

    public void setArr(Integer[] mas){
        this.arr = mas;
        pos = this.arr.length;
    }

    public Iterator<Integer[]> iterator(){
        return new FibIterator();
    }

    private class FibIterator implements Iterator<Integer[]>{
        private int size;

        public FibIterator(){
            this.size = 0;
        }

        @Override
        public boolean hasNext(){
            boolean wh = false;
            for (int i = size; i < arr.length; i++) {
                if (arr[i] == fibGen(1)){
                    wh = true;
                    this.size = i;
                    break;
                }
            }
            return wh;
        }

        @Override
        public Integer[] next(){
            if (!(this.hasNext())) throw new RuntimeException("вызов без HasNext, подпосл не сущ");

            int left = this.size, right = this.size + 1;
            for (int j = right; j < arr.length; j++) {
                if (arr[j] == fibGen(right - left + 1)) right++;
                else break;
            }

            Integer[] mas = new Integer[right - left];
            for (int i = left; i < right; i++) mas[i - left] = arr[i];
            this.size = right;
            return mas;
        }
    }
}