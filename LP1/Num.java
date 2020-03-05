/**
 *
 * Integer Arithmetic with Arbitrarily Large Numbers
 * @author Savan Amitbhai Visalpara (sxv180069)
 * @author Shrey Shah (sxs190184)
 * @author Tejas Markande Gupta (txg180021)
 * @author Harshita Rastogi (hxr190001)
 */


package sxv180069;

import java.util.*;

public class Num implements Comparable<Num> {

    static long defaultBase = 10;   //default base
    long base = 1000000000;        // big base for faster calculation
    long[] arr; // array to store arbitrarily large integers
    boolean isNegative;   // flag to represent neg num
    int len; // actual number of elements of array that are used
    int digits = (int) Math.log10(base());

    /**
     * Constructor for Num class. Takes string and creates a Num  obj representing in a chosen base.
     * @param s the input string
     * @throws NumberFormatException      // if invalid string
     */
    public Num(String s) throws NumberFormatException {
        validateString(s);
        isNegative = false;
        int origLen = s.length();
        int shortLen = origLen;
        int firstDigit= 0, countZero = 0;

        // check if number is negative
        if (s.charAt(0) == '-'){
            this.isNegative = true;
            shortLen = origLen - 1;
            firstDigit = 1;
        }

        char[] sChar = s.toCharArray();

        // find first nonzero char
        while (firstDigit < origLen && sChar[firstDigit] == '0') {
            firstDigit++;
            countZero++;
        }

        // remove leading zeros
        if (countZero > 0)
            shortLen = shortLen - countZero;

        // find correct length
        this.len = (int) Math.ceil((double) shortLen / digits);

        // handle case when s==0
        if (len == 0) {
            len = 1;
            arr = new long[1];
            arr[0] = Long.parseLong(s);
        }
        else {
            arr = new long[len];
            int i = 0, j = 0;
            int ind = origLen - 1;
            char[] cDigit = new char[digits];
            String strDigit;
            // enter char one at a time
            while (firstDigit <= ind && i < len) {

                j = digits - 1;
                while (-1 < j && firstDigit <= ind) {
                    cDigit[j--] = sChar[ind--];
                }
                while (-1 < j) { cDigit[j--] = '0'; }

                strDigit = new String(cDigit);
                this.arr[i++] = Long.parseLong(strDigit);
            }
        }
    }

    /**
     * Validates given string
     * @param s input string
     * @throws NumberFormatException
     */
    public static void validateString(String s) throws NumberFormatException{
        if (s.length() == 0 || s.isEmpty() || (s.length() > 1 && !s.substring(0, 2).matches("[-]([0-9]+)||([0-9]+)"))) {
            throw new NumberFormatException("String is not valid.");
        }
    }

    /**
     * another constructor for long
     * @param x long number
     */
    public Num(long x) {
        isNegative = false;
        //check if negative
        if (x < 0) {
            this.isNegative = true;
            x = -1 * x;
        }
        double length = 0;

        if (x == 0) {
            length = 1;
        }
        else {
            length = ((Math.log((double) x)) / (Math.log((double) base))) + 1;
        }

        this.len = (int) length;
        arr = new long[this.len];  // create array

        long n = x;
        for (int i = 0; i < len; i++) {
            arr[i] = n % base();
            n /= base();
        }
    }

    // constructor without any params
    private Num() {

    }

    /**
     * Adds two numbers Num a and Num b.
     * @param a Num number
     * @param b Num number
     * @return sum of a and b
     */
    public static Num add(Num a, Num b) {
        Num answer = new Num();

        // when both numbers are negative
        if (a.isNegative && b.isNegative) {
            answer = answer.normalAdd(a, b);
            answer.isNegative = true;
        }
        // when both are positive
        else if (!a.isNegative && !b.isNegative) {
            answer = answer.normalAdd(a, b);
            answer.isNegative = false;
        }
        // when one is negative
        else {
            // find difference and assign sign
            if (a.compareTo(b) < 0) {
                answer = answer.normalSubtract(b, a);
                answer.isNegative = b.isNegative;
            } else {
                answer = answer.normalSubtract(a, b);
                answer.isNegative = a.isNegative;
            }
        }
        return answer;
    }

    /**
     * Normal sum of two numbers Num a and Num b.
     * @param a Num number
     * @param b Num number
     * @return sum of a and b
     */
    private Num normalAdd(Num x, Num y) {
        int i=0; // pointer to keep track of arr

        long carry = 0;
        long sum = 0;

        // remove unnecessary zeros
        Num a = removeZeros(x);
        Num b = removeZeros(y);

        Num out = new Num();
        out.len = Math.max(a.len, b.len);
        out.arr = new long[out.len + 1]; // NOTE: out.arr initialized to long[out.len + 1]*


        // for corresponding nums in a and b
        while (i < a.len && i < b.len) {

            sum = a.arr[i] + b.arr[i] + carry;
            out.arr[i] = (sum % base);
            carry = (sum / base);
            i++;
        }

        // for remaining nums in a
        while (i < a.len) {
            sum = a.arr[i] + carry;
            out.arr[i] = (sum % base);
            carry = (sum / base);
            i++;
        }

        // for b's remaining elements
        while (i < b.len) {
            sum = b.arr[i] + carry;
            out.arr[i] = (sum % base);
            carry = (sum / base);
            i++;
        }

        // handle carry
        if (carry > 0) {
            out.arr[i] = carry;
            out.len++;
        }
        return removeZeros(out);
    }

    /**
     * remove leading zeros from x.
     * @param x  input number
     * @return trimmed number a
     */
    public static Num removeZeros(Num x) {
        Num a = new Num();
        a.isNegative = x.isNegative;

        int aLen = x.len;

        // loop until non-zero elem is found
        while (x.arr[aLen - 1] == 0) {
            aLen--;
            if (aLen == 0) {
                aLen++;
                break;
            }
        }
        // create array a and return
        a.arr = new long[aLen];
        for (int i = 0; i < aLen; i++) {
            a.arr[i] = x.arr[i];
        }
        a.len = aLen;
        return a;
    }

    /**
     * Subtraction of a and b
     * @param a Num number
     * @param b Num number
     * @return subtraction
     */
    public static Num subtract(Num a, Num b) {
        Num answer = new Num();

        // When a is neg and b is pos
        if (a.isNegative && !b.isNegative) {
            answer = answer.normalAdd(a, b);
            answer.isNegative = true;
        }
        // a is pos and b is neg
        else if (!a.isNegative && b.isNegative) {
            answer = answer.normalAdd(a, b);
            answer.isNegative = false;
        }
        // When both a and b are of same sign
        else {
            // a < b
            if (a.compareTo(b) < 0) {
                answer = answer.normalSubtract(b, a); // find the difference
                answer.isNegative = !b.isNegative;
            } else {
                answer = answer.normalSubtract(a, b);
                answer.isNegative = a.isNegative;
            }
        }
        return answer;
    }

    /**
     * Normal Difference of two numbers
     * @param x Num number
     * @param y Num number
     * @return the subtraction
     */
    private Num normalSubtract(Num x, Num y) {

        int i = 0, j = 0; // pointers as indices of array of numbers

        long borrow = 0;
        long digitDiff = 0;

        // remove unnecessary zeros
        Num a = removeZeros(x);
        Num b = removeZeros(y);

        Num out = new Num();
        out.len = Math.max(a.len, b.len);
        out.arr = new long[out.len];

        int index = 0;

        // for both array's corresponding elems
        while (i < a.len && j < b.len) {
            digitDiff = a.arr[i] - b.arr[j] - borrow;

            // borrow not required
            if (digitDiff >= 0) {
                out.arr[index] = digitDiff;
                borrow = 0;

                i++; j++; index++;
            }
            // borrow
            else {
                out.arr[index] = digitDiff + base;
                borrow = 1;

                i++; j++; index++;
            }
        }
        // for a's remaining elems
        while (i < a.len) {
            if (borrow == 1) {
                if (a.arr[i] == 0) {
                    digitDiff = base - 1;
                    borrow = 1;
                }
                // subtract borrow
                else {
                    digitDiff = a.arr[i] - borrow;
                    borrow = 0; // No more borrows
                }

                out.arr[index] = digitDiff;
                i++; index++;
            }
            // no borrow
            else {
                out.arr[index] = a.arr[i];
                borrow = 0;
                i++; index++;
            }
        }

        return removeZeros(out);
    }

    /**
     * Calculate product of two numers
     * @param a Num number
     * @param b Num number
     * @return multiplication
     */
    public static Num product(Num a, Num b) {
        Num out = new Num();
        Num zero = new Num(0);

        //when both numbers are zero
        if(a.compareTo(zero)==0 || b.compareTo(zero)==0)
            return zero;

        out.len = a.len + b.len;
        out.arr = new long[out.len];

        long carry = 0;

        for(int i=0; i<b.len ; i++){
            carry=0;
            for(int j=0; j<a.len ; j++){
                out.arr[i+j] += carry + a.arr[j] * b.arr[i];
                carry = out.arr[i+j] / a.base;
                out.arr[i+j] = out.arr[i+j] % a.base;
            }
            out.arr[i + a.len] = carry;
        }

        // find the appropriate sign
        if (a.isNegative == b.isNegative) {
            out.isNegative = false;
        }
        else {
            out.isNegative = true;
        }
        return removeZeros(out);
    }

    /*
    * Calculates power of a given number
    * @param a Num number
    * @param n long number
    * @return a^n
     */
    public static Num power(Num a, long n) {
        if( n == 0)    //base case
            return new Num(1);
        else if(n % 2 == 0 )    //if n is even
            return product(power(a,n/2),power (a,n/2));
        else                  // for all other cases
            return product(a,product(power(a,n/2),power (a,n/2)));
    }


    /**
     * Division using divide and conquer
     * @param a the dividend
     * @param b the divisor
     * @return the integer quotient
     * @throws ArithmeticException when exception arise.
     */
    public static Num divide(Num a, Num b) throws ArithmeticException {

        Num out = new Num();
        Num zero = new Num(0);
        Num one = new Num(1);
        Num negOne = new Num(-1);
        Num two = new Num(2);

        // dividend is zero
        if (a.compareTo(zero) == 0)
            return zero;

        // division by zero exception
        if (b.compareTo(zero) == 0)
            throw new ArithmeticException("divison by zero.");

        // Denominator is one/ minusOne
        if (b.compareTo(one) == 0) {
            out.isNegative = (b.isNegative)? !a.isNegative: a.isNegative;

            out.len = a.len;
            out.arr = new long[out.len];
            int i=0;
            for (long e : a.arr) { out.arr[i++] = e; }
            return out;
        }

        // When division is by 2
        if (b.compareTo(two) == 0) {
            out.isNegative = (b.isNegative) ? !a.isNegative : a.isNegative;
            out = a.by2();
            return out;
        }

        if (a.compareTo(b) < 0) {
            if (a.isNegative == b.isNegative) return zero;
            else return negOne;
        }

        // when magnitude of a and b is same
        if (a.compareTo(b) == 0) {
            if (a.isNegative == b.isNegative) return one;
            else return negOne;
        }
        // low and high for binary search
        Num low = new Num(1);
        Num high = new Num();

        high.len = a.len;
        high.arr = new long[high.len];

        int i = 0;
        for (long e : a.arr)
        {
            high.arr[i++] = e;
        }

        Num mid = new Num();

        while (low.compareTo(high) < 0) {

            Num sum = out.normalAdd(low, high);
            mid = sum.by2();

            if (mid.compareTo(low) == 0)
                break; // When mid = low

            Num prod = out.product(mid, b);

            if (prod.compareTo(a) == 0)
                break;

            else if (prod.compareTo(a) > 0)
                high = mid;
            else
                low = mid;
        }

        // When a, b has different signs
        if (a.isNegative != b.isNegative)
            mid.isNegative = true;

        return mid;
    }

    /**
     * Returns modulo a mod b
     * @param a the first Num
     * @param b the second Num
     * @return the modulo
     * @throws ArithmeticException
     */
    public static Num mod(Num a, Num b) throws ArithmeticException {
        Num zero = new Num(0);
        Num one = new Num(1);

        // When Undefined modulo operation
        if (a.isNegative || b.isNegative || b.compareTo(zero) == 0)
            throw new ArithmeticException("Undefined mod!");

        // NOTE: b wouldn't be minusOne
        if (b.compareTo(one) == 0)
            return a;

        Num quot = divide(a, b);
        Num product = product(quot, b);

        Num remainder = subtract(a, product);

        return remainder;
    }

    /**
     * Calculates squareroot of a given num
     * @param a input number
     * @return square root of a
     * @throws ArithmeticException for undefined numbers
     */
    public static Num squareRoot(Num a) throws ArithmeticException {
        if (a.isNegative)
            throw new ArithmeticException("invalid number for squareroot.");
        Num zero = new Num(0);
        Num one = new Num(1);

        // when input is zero or one
        if (a.compareTo(zero) == 0 || a.compareTo(one)==0)
            return zero;

        Num low = new Num(0);
        Num high = new Num();

        high.isNegative = a.isNegative;
        high.len = a.len;
        high.arr = new long[high.len];

        int i = 0;
        for (long e : a.arr) { high.arr[i++] = e; }

        Num mid = new Num();
        Num sum = new Num(0);

        while (low.compareTo(high) < 0) {

            sum = add(low, high);
            mid = sum.by2();

            if (low.compareTo(mid) == 0)
                return mid;

            Num prod = product(mid, mid);

            if (prod.compareTo(a) == 0)
                return mid;

            else if (prod.compareTo(a) < 0)
                low = mid;
            else
                high = mid;
        }
        return mid;
    }

    /**
     * Compares magnitude of two numbers.
     * @param other Num to compare magnitude with
     */
    public int compareTo(Num other) {

        // when other is bigger
        if (this.len < other.len) return -1;
        // when this is bigger
        else if (this.len > other.len) return 1;
        // when both are same in length
        else {

            int index = this.len - 1;
            while (index > -1) {
                if (this.arr[index] < other.arr[index])
                    return -1;

                if (other.arr[index] < this.arr[index])
                    return 1;

                index--;
            }
        }
        // When Equal Numbers
        return 0;
    }

    /**
     * Outputs given array in specific format considering base.
     */
    public void printList() {
        System.out.print("Base ("+ base()+") : ");

        if (this.isNegative) System.out.print("- ");
        int i=0;
        while (i<this.len) {
            if (i == this.len - 1) System.out.print(this.arr[i]);
            else System.out.print(this.arr[i]+", ");
            i++;
        }
        System.out.println();
    }

    /**
     * Returns given number in decimal.
     */
    public String toString() {

        // get the number in current base()
        this.arr = convertToOriginalBase();
        this.len = this.arr.length;

        StringBuilder sb = new StringBuilder();
        if (isNegative) sb.append("-");

        long x, count = 0, maxNo = base - 1;
        long a = 0;

        for (int i = len-1; i >= 0; i--) {
            count = 0;
            if (i == len - 1) {
                sb.append(arr[i]);
                continue;
            }

            // counting no of digits
            x = arr[i];
            while (x < maxNo && x > 0) {
                x = x / 10;
                count++;
            }
            a = digits - count;

            // append the zeros
            while (a > 0) {
                sb.append("0");
                a--;
            }
            sb.append(arr[i]);
        }

        return sb.toString();
    }

    // Returns base
    public long base() {
        return base;
    }

    // Returns given number in newBase
    public Num convertBase(int newBase) {
        Num n = new Num(newBase);
        Num zero = new Num(0);

        Num out = new Num();
        out.isNegative = this.isNegative;

        double bOld = Math.log10(this.base);
        double bNew = Math.log10(newBase);

        double l2 = (bOld / bNew) * this.len;
        out.len = (int) l2 + 1; // trim zero

        out.arr = new long[out.len];
        int index=0;


        Num temp = new Num();
        temp.len = this.len;
        temp.arr = new long[this.len];
        for (long e: this.arr) temp.arr[index++] = e;
        index = 0;

        // use mod to find new num
        while (temp.compareTo(zero) > 0) {
            Num r = mod(temp, n);
            String s = r.toString();
            out.arr[index] = Long.parseLong(s);
            Num q = divide(temp, n);
            temp = q; index++;
        }

        out = zero.removeZeros(out);
        out.base = newBase;
        return out;
    }

    /**
     * Convert the base of Num to Num.base
     * @return the Num
     */
    private long[] convertToOriginalBase() {

        Num num = new Num(this.arr[len-1]);
        Num base = new Num(this.base());

        // When the base is same
        if (num.base == this.base)
            return this.arr;

        // convert from this.base() to base()
        for (int i = len-2;i>=0;i--){
            num = product(num,base);
            num = add(num,new Num(arr[i]));
        }

        this.base = num.base;
        return num.arr;
    }

    /**
     * Divide by 2, used in binary search
     * @return a new Num half of calling Num
     */
    public Num by2() {
        Num half = new Num();
        long carry = 0;
        Num two = new Num(2);
        Num zero = new Num(0);

        if (this.compareTo(two) < 0)
            return zero;

        // most significant digit = 1, half.len = this.len - 1
        if (this.arr[len - 1] == 1) {
            half.len = this.len - 1;
            carry = 1;
        }
        // most significant digit > 1
        else half.len = this.len;

        half.isNegative = this.isNegative;
        half.arr = new long[half.len];

        int index = half.len - 1;

        while (index > -1) {
            long sum = 0;

            if (carry == 1) {
                sum = this.arr[index] + base;
            } else {
                sum = this.arr[index];
            }

            half.arr[index] = sum / 2;
            carry = (sum % 2 == 1) ? 1 : 0;

            index--;
        }
        return half;
    }

    /**
     * Evaluates given expression using postfix method
     * @param expr an expression expressed in a list of string
     * @return evaluated output
     */
    public static Num evaluatePostfix(String[] expr) {
        // empty stack
        Stack<Num> stack = new Stack<>();
        // loop through each elem
        for(int i=0 ; i<expr.length ; i++){
            if(Character.isDigit(expr[i].charAt(0))){  //operand
                stack.push(new Num(expr[i]));
            }
            else{     //operator
                Num val1 = stack.pop();
                Num val2 = stack.pop();

                switch(expr[i]){
                    case "+":
                        stack.push(add(val2,val1));
                        break;

                    case "-":
                        stack.push(subtract(val2,val1));
                        break;

                    case "/":
                        stack.push(divide(val2,val1));
                        break;

                    case "*":
                        stack.push(product(val2,val1));
                        break;

                    case "%":
                        stack.push(mod(val2,val1));
                        break;

                    case "^":
                        stack.push(power(val2,Long.parseLong(val1.toString())));
                        break;
                }
            }
        }

        return stack.pop();
    }

    /**
     * Method to evaluate E of the equation
     * @param qt queue
     * @return evaluated num
     */
    public static Num evalE(Queue<String> qt){
        Num val1 = evalT(qt);
        while(qt.size()>0 && (qt.peek().equals("+")||qt.peek().equals("-"))){
            String oper = qt.remove();
            Num val2 = evalT(qt);
            if(oper.equals("+"))
                val1 = add(val1,val2);
            else
                val1 = subtract(val1,val2);
        }
        return val1;
    }

    /**
     * Method to evaluate T of the equation
     * @param qt queye
     * @return evaluated num
     */
    public static Num evalT(Queue<String> qt){
        Num val1 = evalF(qt);
        while(qt.size()>0 && (qt.peek().equals("*")||qt.peek().equals("/"))){
            String oper = qt.remove();
            Num val2 = evalF(qt);
            if(oper.equals("*"))
                val1 = product(val1,val2);
            else
                val1 = divide(val1,val2);
        }
        return val1;
    }

    /**
     * Method to evaluate F of the function
     * @param qt queue
     * @return evaluated num
     */
    public static Num evalF(Queue<String> qt){
        Num result = new Num();
        if(qt.peek().equals("(")){
            String oper = qt.remove();
            result = evalE(qt);
            oper = qt.remove();
        }
        else{
            String num = qt.remove();
            result = new Num(num);
        }
        return result;
    }

    /**
     * Evaluates given infix expression
     * @param expr expression to be evaluated
     * @return evaluated num
     */
    public static Num evaluateExp(String expr)  {

        StringBuilder sb = new StringBuilder(expr);
        int j = 0;
        //loop through each element
        for(int i = 0; i < sb.length(); i++) {
            if (!Character.isWhitespace(sb.charAt(i))) {
                sb.setCharAt(j++, sb.charAt(i));
            }
        }
        sb.delete(j, sb.length());
        String[] result = sb.toString().split("(?<=[-+*/\\\\(\\\\)])|(?=[-+*/\\\\(\\\\)])");
        Queue<String> qt = new LinkedList<>();
        for(int i=0;i<result.length;i++){
            qt.add(result[i]);
        }
        Num answer = evalE(qt);   //call evalE
        return answer;

    }

    public static void main(String[] args) {
        Num s = new Num("100");
        Num t = new Num("2");
        System.out.println("Given numbers are: "+s+" and "+t);
        Num z = Num.power(s, 4);
        Num x = Num.add(s, t);
        System.out.println("Power :"+z);
        System.out.println("Add :"+x);
        Num q = Num.product(s, t);
        System.out.println("Product :"+q);
        Num o = Num.subtract(s, t);
        System.out.println("Subtract :" +o);
        System.out.println("Squareroot :"+Num.squareRoot(s));
        Num d = Num.divide(s, t);
        System.out.println("Divide :"+d);
        String[] input = { "9","3","*","5","*","5","+"};
        Num p = evaluatePostfix(input);
        System.out.println("Postfix :"+ (p.isNegative?"-":"")+p);
        System.out.println("evaluateExp :"+evaluateExp("(3+4)*5"));
        t.convertBase(50).printList();

    }
}