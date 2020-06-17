public class Sentance implements Comparable<Sentance> {
    private String s;

    public Sentance(String s){
        this.s = s;
    }

    public String toString(){
        return this.s;
    }

    public int parseDec(){
        int boo = 0;
        int counter = 0;
        for (int i = 0; i < this.s.length(); i++){
            if ((this.s.charAt(i) > 47) && (this.s.charAt(i) < 58) && (boo >= 0)){
                boo = 1;
            }
            else if ((this.s.charAt(i) == 32) && (boo == 1)){
                counter++;
                boo = 0;
            }
            else if (this.s.charAt(i) == 32){
                boo = 0;
            }
            else boo = -1;
        }
        if ((this.s.charAt(this.s.length() - 1) != 32) && (boo == 1)){
            counter++;
        }
        return counter;
    }

    public int compareTo(Sentance AA){
        if (this.parseDec() == AA.parseDec()) return 0;
        else if (this.parseDec() < AA.parseDec()) return -1;
        else if (this.parseDec() > AA.parseDec()) return  1;
        return 1;
    }
}
