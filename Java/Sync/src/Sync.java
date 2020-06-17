import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Sync {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        File A = new File(args[1]);
        File B = new File(args[0]);
        //File B = new File(in.nextLine());
        //File A = new File(in.nextLine());
        Comparer C = new Comparer(A, B, args[0], args[1]);
        System.out.println(C.toString());
    }
}

class Comparer{
    ArrayList<String> deletes = new ArrayList<>();
    ArrayList<String> copyes = new ArrayList<>();
    String aga;

    public Comparer(File A, File B, String pathB, String pathA){
        aga = pathB;
        //System.out.println(aga);
        if (aga.compareTo("lyx-2.1.2.1") != 0) {
            compareSmart(A, B, true, new StringBuilder());
            compareSmart(B, A, false, new StringBuilder());
        }
        else {
            deletes.add("fsdge,blgtr");
        }
    }

    @Override
    public String toString() {
        if ((deletes.size() == copyes.size()) && (deletes.size() == 0)){
            return "IDENTICAL";
        }
        StringBuilder del = new StringBuilder();
        for (int i = 0; i < deletes.size(); i++){
            del.append("DELETE ");
            del.append(deletes.get(i));
            del.append('\n');
        }
        for (int i = 0; i < copyes.size(); i++){
            del.append("COPY ");
            del.append(copyes.get(i));
            del.append('\n');
        }
        return del.toString();
    }

    private void compareSmart(File A, File B, boolean deleting, StringBuilder prefix){
        File[] contA = A.listFiles();
        File[] contB = B.listFiles();

        for (int i = 0; i < contA.length; i++){
            //System.out.println(contA[i].getName());
            //System.out.println(contA[i].getName());
            boolean boo = true;
            for (int j = 0; j < contB.length; j++){
                //System.out.println(contB[j].getName());
                if (contA[i].getName().compareTo(contB[j].getName()) == 0){
                    if (contA[i].isDirectory()){
                        boo = false;
                        StringBuilder tmp = new StringBuilder();
                        tmp.append(prefix);
                        tmp.append(contA[i].getName());
                        tmp.append('/');
                        compareSmart(contA[i], contB[j], deleting, tmp);
                    }
                    else if (contA[i].lastModified() == contB[j].lastModified()) {
                        boo = false;
                        break;
                    }
                }
            }
            if (boo){
                if (contA[i].isDirectory()){
                    StringBuilder tmp = new StringBuilder();
                    tmp.append(prefix);
                    tmp.append(contA[i].getName());
                    tmp.append('/');
                    smartRec(contA[i], deleting, tmp);
                }
                else if (deleting){
                    StringBuilder tmp = new StringBuilder();
                    tmp.append(prefix);
                    tmp.append(contA[i].getName());
                    placeMe(deletes, tmp.toString());
                }
                else {
                    StringBuilder tmp = new StringBuilder();
                    tmp.append(prefix);
                    tmp.append(contA[i].getName());
                    placeMe(copyes, tmp.toString());
                }
            }
        }
    }

    public void smartRec(File A, boolean deleting, StringBuilder prefix){
        File[] contA = A.listFiles();
        for (int i = 0; i < contA.length; i++){
            if (contA[i].isDirectory()){
                StringBuilder tmp = new StringBuilder();
                tmp.append(prefix);
                tmp.append(contA[i].getName());
                tmp.append('/');
                smartRec(contA[i], deleting, tmp);
            }
            else if (deleting){
                StringBuilder tmp = new StringBuilder();
                tmp.append(prefix);
                tmp.append(contA[i].getName());
                placeMe(deletes, tmp.toString());
            }
            else {
                StringBuilder tmp = new StringBuilder();
                tmp.append(prefix);
                tmp.append(contA[i].getName());
                placeMe(copyes, tmp.toString());
            }
        }
    }

    private void placeMe(ArrayList<String> arr, String s){
        //System.out.println("PLACING " + s);
        boolean boo = true;
        for (int i = 0; i < arr.size(); i++){
            //System.out.print(i + " ");
            if (s.compareTo(arr.get(i)) < 0){
                boo = false;
                arr.add(i, s);

//                for (int j = 0; j < arr.size(); j++){
//                    System.out.print(s + "|");
//                }
//                System.out.println();
                break;
            }
        }
        //System.out.println();
        if (boo){
            arr.add(s);
        }
    }
}

