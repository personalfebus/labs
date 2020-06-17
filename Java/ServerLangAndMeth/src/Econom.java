import java.util.Scanner;

public class Econom {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        Token tok = new Token();
        int iter = 0;
        String[] tokenContainer = new String[s.length()];
        int curr = 0;
        //System.out.println("N");
        for (int i = 0; i < s.length(); i++){
            if ((s.charAt(i) == '@') || (s.charAt(i) == '#') || (s.charAt(i) == '$')) {
                iter++;
                System.out.println("Found Iteration " + iter);
            }
            else if (s.charAt(i) == '(') {
                System.out.println("Found a Token");
                tokenContainer[curr] = tok.getToken(s, i + 1);
                curr++;
            }
        }
        System.out.println("---------");
        for (int i = 0; i < curr - 1; i++) {
            for (int j = i + 1; j < curr; j++) {
                if (tokenContainer[i].equals(tokenContainer[j])){
                    iter--;
                    System.out.println("Found Dup = " + tokenContainer[i]);
                    break;
                }
            }
        }
        System.out.println("- - - - - - -");
        System.out.println(iter);
    }
}

class Token{
    public String getToken(String s, int i){
        int start = i - 1;
        int num = 1;
        while (num > 0){
            if (s.charAt(i) == '(') {
                System.out.println("Found (");
                num++;
            }
            else if (s.charAt(i) == ')'){
                System.out.println("Found )");
                num--;
            }
            i++;
        }
        return s.substring(start, i);
    }
}
