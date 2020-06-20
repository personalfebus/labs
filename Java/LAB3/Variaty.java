public class Variaty implements Comparable<Variaty> {
    private String[] arr;

    public Variaty(String[] ss){
        this.arr = ss;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.ensureCapacity(32);
        for (int i = 0; i < this.arr.length; i++){
            s.append(arr[i]);
            s.append("\n");
        }
        return s.toString();
    }

    public int compareTo(Variaty B) {
        if (this.getCommon() == B.getCommon()) return 0;
        else if (this.getCommon() > B.getCommon()) return 1;
        else return -1;
    }

    private boolean contain(int j, char a){
        for (int i = 0; i < this.arr[j].length(); i++){
            if (this.arr[j].charAt(i) == a) return true;
        }
        return false;
    }

    private boolean checkForNotLatin(char s){
        return ((s < 65) || ((s > 90) && (s < 97)) || (s > 122));
    }

    private int getCommon(){
        int num = 0;
        for (int i = 0; i < this.arr[0].length(); i++){
            char s = this.arr[0].charAt(i);
            if (checkForNotLatin(s)) continue;
            int tmp = 1;
            for (int j = 1; j < this.arr.length; j++){
                if (this.contain(j, s)){
                    //System.out.println("FOUND " + s + " in " + j);
                    continue;
                }
                tmp = 0;
                break;
            }
            num += tmp;
        }
        return num;
    }
}