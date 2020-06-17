public class UnsignNum {
    private int[] mas;

    public UnsignNum(int raz){
        this.mas = new int[raz];
    }

    public void fillWithDigits(String str){
        for (int i = 0; i < str.length(); i++){
            int asc = str.charAt(i) + 0;
            if ((asc < 48) || (asc > 57)) {
                throw new RuntimeException(str.charAt(i) + " - NOT A NUMBER");
            }
            this.addElem(str.charAt(i) - 48, i);
        }
    }

    public void addElem(int a, int i){
        this.mas[i] += a;
        int c = this.mas[i];
        if ((this.mas[i] / 10 > 0) && (i > 0)){
            this.mas[i] = 0;
            this.addElem(c % 10, i);
            this.addElem(c / 10, i - 1);
        }
        if (this.mas[0] / 10 > 0) moveOver();
    }

    public int getElem(int i){
        return this.mas[i];
    }

    public int[] getMas(){
        return this.mas;
    }

    public void moveOver(){
        int[] mas1 = new int[this.mas.length + 1];
        for (int i = 1; i < this.mas.length; i++){
            mas1[i + 1] = this.mas[i];
        }
        mas1[0] = this.mas[0] / 10;
        mas1[1] = this.mas[0] % 10;
        this.mas = mas1;
    }

    public String toString(){
        String s = "";
        for (int i = 0; i < this.mas.length; i++){
            s = s + mas[i] + " ";
        }
        s = s + "\n";
        return s;
    }

    public UnsignNum Summ(UnsignNum B1){
        int al = this.mas.length;
        int bl = B1.getMas().length;
        int mn, mx;
        boolean wh = false;

        if (al > bl){
            mx = al;
            mn = bl;
            wh = true;
        }
        else {
            mx = bl;
            mn = al;
        }

        UnsignNum C1 = new UnsignNum(mx);

        for (int i = mn - 1; i >= 0; i--){
            int c = this.mas[i + (this.mas.length - mn)] + B1.getElem(i + (B1.getMas().length - mn));
            C1.addElem(c, i + (mx - mn));
        }

        for (int i = mx - mn - 1; i >= 0; i--){
            if (wh) C1.addElem(this.mas[i], i);
            else C1.addElem(B1.getElem(i), i);
        }

        return C1;
    }
}