public class Test {
    public static void main(String[] args) {
        int n1 = Integer.parseInt(args[0]);
        UnsignNum A1 = new UnsignNum(n1);
        A1.fillWithDigits(args[1]);
        int n2 = Integer.parseInt(args[2])
                ;
        UnsignNum B1 = new UnsignNum(n2);
        B1.fillWithDigits(args[3]);
        System.out.println(A1);
        System.out.println(B1);
        UnsignNum C1 = A1.Summ(B1);
        System.out.println(C1);
    }
}