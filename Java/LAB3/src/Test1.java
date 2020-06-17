public class Test1 {
    public static void main(String[] args){
        int k = Integer.parseInt(args[0]);
        int p = Integer.parseInt(args[1]);
        Matrix Andrei = new Matrix(k, p);
        for (int i = 0; i < k; i++){
            for (int j = 0; j < p; j++){
                int nominator = (int)(10.0 * Math.random());
                int denominator = 1;
                Andrei.addElem(i, j, nominator, denominator);
                System.out.print(nominator);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
        int kk = Andrei.getRank();
        System.out.print("\n");
        for (int i = 0; i < k; i++){
            for (int j = 0; j < p; j++){
                int nom = Andrei.getNom(i, j);
                int denom = Andrei.getDen(i, j);
                System.out.print(nom);
                if ((denom != 1) && (nom != 0)){
                    System.out.print("/");
                    System.out.print(denom);
                }
                System.out.print(" ");
            }
            System.out.print("\n");
        }
        System.out.print("Rang = ");
        System.out.print(kk);
    }
}
