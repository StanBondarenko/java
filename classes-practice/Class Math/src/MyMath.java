import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public  class MyMath {
    //********************** Addition methods *********************//
    public static int mySum(int firstNumber, int secondNumber) {
        return firstNumber + secondNumber;
    }
    public static double mySum(double firstNumber, double secondNumber) {
        return firstNumber + secondNumber;
    }
    //********************** Subtraction methods *********************//
    public static int mySub(int firstNumber, int secondNumber) {
        return firstNumber - secondNumber;
    }
    public static double mySub(double firstNumber, double secondNumber) {
        return firstNumber - secondNumber;
    }
    //********************** Division methods *********************//
    public static int myDiv(int firstNumber, int secondNumber) {
        if (secondNumber == 0) {
            throw new IllegalArgumentException("Division by zero");
        }
        return firstNumber / secondNumber;
    }
    public static double myDiv(double firstNumber, double secondNumber) {
        if (secondNumber == 0.0) {
            throw new IllegalArgumentException("Division by zero");
        }
        return firstNumber / secondNumber;
    }
    //********************** Multiplication methods *********************//
    public static int myMult (int firstNumber, int secondNumber){
        return firstNumber * secondNumber;
    }
    public static double myMult (double firstNumber, double secondNumber){
        return firstNumber * secondNumber;
    }

    //********************** Method max *********************//
    public static int myMax(int num1, int num2){
        if (num1>=num2){
            return num1;
        }
        return num2;
    }
    public static double myMax(double num1, double num2){
        if (num1>=num2){
            return num1;
        }
        return num2;
    }
    public static int myMaxFromArray(int [] numbers){
        int result = numbers[0];
        for (int i = 1; i<numbers.length; i++){
            if (numbers[i]>=result){
                result= numbers[i];
            }
        }
        return result;
    }

    public static double myMaxFromArray(double [] numbers){
        double result = numbers[0];
        for (int i = 1; i<numbers.length; i++){
            if (numbers[i]>=result){
                result= numbers[i];
            }
        }
        return result;
    }
    //********************** Method average from array  *********************//
    public static double myAverage(double[] array){
        if (array.length==0){
            return 0.0;
        }
        double summ =0;
        for(int i =0;i<array.length;i++){
            summ+=array[i];
        }
        return summ/array.length;

    }
    //*********************** Decimal to binary ***********************//
    public static int myDecToBi(int number){
        List<Integer> resultList = new LinkedList<>();
        int rest = number;

        while (rest>0) {
            resultList.add(rest % 2);
            rest = rest / 2;
        }
        StringBuilder total = new StringBuilder();
        for (int i =resultList.size()-1;i>=0; i--){
            total.append(resultList.get(i));
        }
        String result= total.toString();
        return Integer.parseInt(result);
    }

    //********************** Method min *********************//
    public static int myMin(int num1, int num2){
        if (num1<=num2){
            return num1;
        }
        return num2;
    }
    public static double myMin(double num1, double num2){
        if (num1<=num2){
            return num1;
        }
        return num2;
    }
    public static int myMinFromArray(int [] numbers){
        int result = numbers[0];
        for (int i = 1; i<numbers.length; i++){
            if (numbers[i]<=result){
                result= numbers[i];
            }
        }
        return result;
    }
    public static double myMinFromArray(double [] numbers){
        double result = numbers[0];
        for (int i = 1; i<numbers.length; i++){
            if (numbers[i]<=result){
                result= numbers[i];
            }
        }
        return result;
    }

    //********************** Method of raising to a power *********************//
    public static double myRaiseToPower (double number, int degree ){
        double result=number;
        if (degree==0){
            return 1.0;
        }
        if (number==0){
            return 0.0;
        }
        if (degree<0){
            for (int i=1; i<MyMath.myAsb(degree); i++){

                number=number* result;
            }
            return 1/number;
        }
        for (int i=1; i<degree; i++){

            number=number* result;
        }
        return number;
    }
    //********************** Absolute number methods *********************//
    public static int myAsb (int number){
        return number <0 ? -number:number;
    }
    public static double myAsb (double number){
        return number <0 ? -number:number;
    }
    //********************** Square root method *********************//
    public static double mySqrtNewton(double number){
        if (number<0){
            throw new IllegalArgumentException("Can't compute square root of negative number");
        }
        double guess = number/2;
        double diference = 0.0001;
        while (MyMath.myAsb(guess*guess-number) > diference){
                guess=(guess + number / guess) /2;
        }
        return guess;
    }
    //********************** Factorial method *********************//
    public static BigInteger myFactorial(int number){

        if (number <0){
            throw  new IllegalArgumentException("Factorial cannot be less than zero!");
        }
        BigInteger result =BigInteger.ONE;
        for (int i =1; i<= number;i++){
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
    //********************** Quadratic equation method *********************//
    //**** Exemple: 3x^2 -14x-5=0 ------> a=3, b=-14 c=-5*******************//
    public static String myQuadEquation (int a, int b, int c){
        double discriminant= MyMath.myRaiseToPower(b,2) - 4*a*c;
        double x1;
        double x2;
        if( discriminant < 0){
            return "There are no real roots!";
        }
        double sqrFromDiscriminant = MyMath.mySqrtNewton(discriminant);
        if (discriminant == 0){
            x1 = (double) (-b)/(2*a);
            return "The root is this:  " +x1;
        }else {
            x1 =(-b + sqrFromDiscriminant) / (2*a);
            x2 = (-b - sqrFromDiscriminant) / (2*a);
            return String.format("First root is: %.2f Second root is: %.2f", x1, x2);
        }
    }


}
