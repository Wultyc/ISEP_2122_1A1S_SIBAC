package pt.ipp.isep.dei.math;

import java.math.BigDecimal;
import java.math.MathContext;

public class Calculator {

    public static final MathContext mc = MathContext.DECIMAL64;

    public static boolean greaterThan(BigDecimal bd1, BigDecimal bd2){
        return bd1.compareTo(bd2) > 0;
    }
    public static boolean lowerThan(BigDecimal bd1, BigDecimal bd2){return bd1.compareTo(bd2) < 0;}
    public static boolean equal(BigDecimal bd1, BigDecimal bd2){
        return bd1.compareTo(bd2) == 0;
    }
    public static boolean greaterThanOrEqual(BigDecimal bd1, BigDecimal bd2){
        return bd1.compareTo(bd2) >= 0;
    }
    public static boolean lowerThanOrEqual(BigDecimal bd1, BigDecimal bd2){
        return bd1.compareTo(bd2) <= 0;
    }

    public static BigDecimal sum(BigDecimal ... bds){

        BigDecimal res = new BigDecimal("0");

        for (BigDecimal bd : bds) {
            res = res.add(bd);
        }

        return res;
    }

    public static BigDecimal subtract(BigDecimal ... bds){

        BigDecimal res = new BigDecimal("0");

        for (BigDecimal bd : bds) {
            res = res.subtract(bd);
        }

        return res;
    }

    public static BigDecimal multiply(BigDecimal ... bds){

        BigDecimal res = new BigDecimal("0");

        for (BigDecimal bd : bds) {
            res = res.multiply(bd);
        }

        return res;
    }

    public static BigDecimal divide(BigDecimal ... bds){

        BigDecimal res = new BigDecimal("0");

        for (BigDecimal bd : bds) {
            res = res.divide(bd, Calculator.mc);
        }

        return res;
    }

    public static BigDecimal fraction(BigDecimal bd1, BigDecimal bd2){
        return divide(bd1,bd2);
    }
}
