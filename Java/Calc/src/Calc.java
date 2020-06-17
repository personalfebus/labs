import java.util.HashMap;
import java.util.Scanner;

public class Calc {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String tmp = in.nextLine();
        StringBuilder s = new StringBuilder(tmp);
        Lexer lexer = new Lexer(s, in);
        //System.out.println(lexer.dictionary.get("ABC"));
        //System.out.println(lexer.dictionary.get("Kappa"));
        //System.out.println(lexer.dictionary.toString());
        Parser parser = new Parser(s, lexer.dictionary);
        //System.out.println(parser.parseT(0).toString());
        //System.out.println("-----");
        ParsingContainer result = parser.parseE(0);
        //System.out.println(result.toString());
        if (result.index < s.length() - 1){
            System.out.println("error");
        }
        else {
            System.out.println(result.value);
        }
        //System.out.println(str.substring(0, str.length()));
        //CATCH ERRORS!!!

    }
}

class Lexer {
    public HashMap<String, Long> dictionary;

    public Lexer(StringBuilder s, Scanner in){
        //Scanner in = new Scanner(System.in);
        dictionary = new HashMap<>();
        boolean needLatin = true;
        int start = 0;
        for (int i = 0; i < s.length(); i++){
            boolean boo = isLatin(s.charAt(i));
            //System.out.println(i + " " + boo + " " + s.charAt(i));
            if (needLatin && boo){
                start = i;
                needLatin = false;
            }
            if (!needLatin){
                if (!(boo || isNumber(s.charAt(i)))) {
                    String word = s.substring(start, i);
                    if (!dictionary.containsKey(word)) {
                        Long num = in.nextLong();
                        dictionary.put(word, num);
                    }
                    needLatin = true;
                }
                else if (i == s.length() - 1){
                    Long num = in.nextLong();
                    dictionary.put(s.substring(start), num);
                    needLatin = true;
                }
            }
        }
    }

    public boolean isLatin(char a){
        return  (((int)a > 64) && ((int)a < 91)) || (((int)a > 96) && ((int)a < 123));
    }

    public boolean isNumber(char a){
        return ((int)a > 47) && ((int)a < 58);
    }
}

class Parser{

    //-x * (x+10) * (128/x-5)
//    <E>  ::= <T> <E’>.
//    <E’> ::= + <T> <E’> | - <T> <E’> | .
//    <T>  ::= <F> <T’>.
//    <T’> ::= * <F> <T’> | / <F> <T’> | .
//    <F>  ::= <number> | <var> | ( <E> ) | - <F>.

    private HashMap<String, Long> dictionary;
    private StringBuilder s;

    public Parser(StringBuilder s, HashMap<String, Long> d){
        this.s = s;
        this.dictionary = d;
    }

    public ParsingContainer parseE(int pos){
        //System.out.println("parseE - " + pos);
        for (int i = pos; i < s.length(); i++) {
            if (isSpace(s.charAt(i))) continue;
            ParsingContainer contT = parseT(i);
            if (contT.index == -1){
                return new ParsingContainer(-1, 0, 0);
            }
            //System.out.println(contT.toString());
            //System.out.println(s.charAt(contT.index));
            ParsingContainer contE1 = parseE1(contT.index, contT);
            if (contE1.index == -1){
                return new ParsingContainer(-1, 0, 0);
            }
//            if (contE1.operation == 43){
//                contT.value += contE1.value;
//                contT.index = contE1.index;
//            }
//            else if (contE1.operation == 45){
//                contT.value -= contE1.value;
//                contT.index = contE1.index;
//            }
//            System.out.println(contT.toString());
            //System.out.println(contE1.toString());
            //return contT;
            return contE1;
        }
        ParsingContainer errcont = new ParsingContainer(-1, 0, 0);
        return errcont;
    }

    public ParsingContainer parseE1(int pos, ParsingContainer pair){
        //System.out.println("parseE1 - " + pos);
        int endpos = pos;
        for (int i = pos; i < s.length(); i++) {
            if (isSpace(s.charAt(i))) continue;
            if (isAddOperation(s.charAt(i))){
                ParsingContainer contT = parseT(i + 1);
                if (contT.index == -1){
                    return new ParsingContainer(-1, 0, 0);
                }
                if ((int)s.charAt(i) == 43){
                    pair.value += contT.value;
                    pair.index = contT.index;
//                    contT.value += contE1.value;
//                    contT.index = contE1.index;
                }
                else if ((int)s.charAt(i) == 45){
                    pair.value -= contT.value;
                    pair.index = contT.index;
//                    contT.value -= contE1.value;
//                    contT.index = contE1.index;
                }
                ParsingContainer contE1 = parseE1(pair.index, pair);
                if (contE1.index == -1){
                    return new ParsingContainer(-1, 0, 0);
                }
//                if (contE1.operation == 43){
//                    contT.value += contE1.value;
//                    contT.index = contE1.index;
//                }
//                else if (contE1.operation == 45){
//                    contT.value -= contE1.value;
//                    contT.index = contE1.index;
//                }
                //contT.operation = (int)s.charAt(i);
                //return contT;
                return contE1;
            }
            else {
                endpos = i;
                break;
            }
        }
        //!!!
        //pair.index = endpos;
        return pair;
//        ParsingContainer cont = new ParsingContainer(endpos, 0, 0);
//        return cont;
//        ParsingContainer errcont = new ParsingContainer(-1, 0, 0);
//        return errcont;
    }

    public ParsingContainer parseT(int pos){
        //System.out.println("parseT - " + pos);
        for (int i = pos; i < s.length(); i++) {
            if (isSpace(s.charAt(i))) continue;
            ParsingContainer contF = parseF(i);
            if (contF.index == -1){
                return new ParsingContainer(-1, 0, 0);
            }
            ParsingContainer contT1 = parseT1(contF.index, contF);
            if (contT1.index == -1){
                return new ParsingContainer(-1, 0, 0);
            }
//            if (contT1.operation == 42){
//                contF.value *= contT1.value;
//                contF.index = contT1.index;
//            }
//            else if (contT1.operation == 47){
//                contF.value /= contT1.value;
//                contF.index = contT1.index;
//            }
            //return contF;
            return contT1;
        }
        ParsingContainer errcont = new ParsingContainer(-1, 0, 0);
        return errcont;
    }

//    <E>  ::= <T> <E’>.
//    <E’> ::= + <T> <E’> | - <T> <E’> | .
//    <T>  ::= <F> <T’>.
//    <T’> ::= * <F> <T’> | / <F> <T’> | .
//    <F>  ::= <number> | <var> | ( <E> ) | - <F>.

    public ParsingContainer parseT1(int pos, ParsingContainer pair){
        //PORYADOK DEISVIY
        //PEREDAVAI PARSING CONT V RECUSRSIU
        //System.out.println("parseT1 - " + pos);
        int endpos = pos;
        for (int i = pos; i < s.length(); i++){
            if (isSpace(s.charAt(i))) continue;
            if (isMultOperation(s.charAt(i))){
                ParsingContainer contF = parseF(i + 1);
                if (contF.index == -1) {
                    return new ParsingContainer(-1, 0, 0);
                }
                if ((int)s.charAt(i) == 42){
                    pair.value *= contF.value;
                    pair.index = contF.index;
                    //contF.value *= contT1.value;
                    //contF.index = contT1.index;
                }
                else if ((int)s.charAt(i) == 47){
                    pair.value /= contF.value;
                    pair.index = contF.index;
                }
                ParsingContainer contT1 = parseT1(pair.index, pair);
                if (contT1.index == -1){
                    return new ParsingContainer(-1, 0, 0);
                }
//                System.out.print(contF.toString());
//                System.out.println((char)contT1.operation + " " + contT1.operation);
//                System.out.println(contT1.toString());
//                if (contT1.operation == 42){
//                    contF.value *= contT1.value;
//                    contF.index = contT1.index;
//                }
//                else if (contT1.operation == 47){
//                    contF.value /= contT1.value;
//                    contF.index = contT1.index;
//                }
//                contF.operation = (int)s.charAt(i);
                return contT1;
            }
            else {
                endpos = i;
                break;
            }
        }
        //!!!
        //pair.index = endpos;
        return pair;
//        ParsingContainer cont = new ParsingContainer(endpos, 0, 0);
//        return cont;
//        ParsingContainer errcont = new ParsingContainer(-1, 0, 0);
//        return errcont;
    }

    public ParsingContainer parseF(int pos){
        //System.out.println("parseF - " + pos);
        for (int i = pos; i < s.length(); i++){
            if (isSpace(s.charAt(i))) continue;
            if (isNumber(s.charAt(i))){
                ParsingContainer cont = parseNumber(i);
                return cont;
            }
            if (isLatin(s.charAt(i))){
                ParsingContainer cont = parseVariable(i);
                return cont;
            }
            if (isParen(s.charAt(i))){
                ParsingContainer contE = parseE(i + 1);
                if (isValidIndex(contE.index) && isParen(s.charAt(contE.index))){
//                    if (contE.index < s.length() - 1) {
//                        contE.index++;
//                    }
                    contE.index++;
                    return contE;
                }
                else{
                    return new ParsingContainer(-1, 0, 0);
                }
            }
            if ((int)s.charAt(i) == 45){
                ParsingContainer contF = parseF(i + 1);
                contF.value *= -1;
                return contF;
            }
            break;
        }
        ParsingContainer errcont = new ParsingContainer(-1, 0, 0);
        return errcont;
    }

    public ParsingContainer parseVariable(int pos){
        //System.out.println("parseVar - " + pos);
        ParsingContainer cont;
        String var = "";
        int stop = 0;
        for (int i = pos; i < s.length(); i++){
            //System.out.println(s.charAt(i));
            if ((i == s.length() - 1) && (isLatin(s.charAt(i)) || isNumber(s.charAt(i)))){
                var = s.substring(pos);
                stop = i;
                break;
            }
            if (!isLatin(s.charAt(i)) && !isNumber(s.charAt(i))){
                var = s.substring(pos, i);
                stop = i;
                break;
            }
        }
        if (dictionary.containsKey(var)) {
            long number = dictionary.get(var);
            cont = new ParsingContainer(stop, number, 0);
        }
        else {
            //ERROR!
            cont = new ParsingContainer(-1, 0, 0);
        }
        return cont;
    }

    public ParsingContainer parseNumber(int pos){
        //System.out.println("parseNum - " + pos);
        ParsingContainer cont;
        long number = 0;
        int stop = 0;
        for (int i = pos; i < s.length(); i++){
            if ((i == s.length() - 1) && (isNumber(s.charAt(i)))){
                stop = i;
            }
            if (!isNumber(s.charAt(i))){
                stop = i;
                break;
            }
            number = number * 10 + (long)s.charAt(i) - 48;
        }
        cont = new ParsingContainer(stop, number, 0);
        return cont;
    }

    public boolean isValidIndex(int i){
        return (i > -1) && (i < s.length());
    }

    public boolean isNumber(char a){
        return ((int)a > 47) && ((int)a < 58);
    }

    public boolean isParen(char a){
        return ((int)a == 40) || ((int)a == 41);
    }

    public boolean isMultOperation(char a){
        int k = (int)a;
        return (k == 42) || (k == 47);
    }

    public boolean isAddOperation(char a){
        int k = (int)a;
        return (k == 43) || (k == 45);
    }

    public boolean isSpace(char a){
        return (int)a == 32;
    }

    public boolean isLatin(char a){
        return  (((int)a > 64) && ((int)a < 91)) || (((int)a > 96) && ((int)a < 123));
    }
}

class ParsingContainer{
    public int index;
    public long value;
    public int operation;

    public ParsingContainer(int i, long v, int o){
        index = i;
        value = v;
        operation = o;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Index = ");
        res.append(index);
        res.append('\n');
        res.append("Value = ");
        res.append(value);
        res.append('\n');
        return res.toString();
    }
}