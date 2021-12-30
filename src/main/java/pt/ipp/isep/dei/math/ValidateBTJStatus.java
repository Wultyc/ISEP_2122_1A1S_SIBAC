package pt.ipp.isep.dei.math;

import pt.ipp.isep.dei.model.helpers.NumericValue;
import pt.ipp.isep.dei.tbjStatus.TBJ_Status;

import java.math.BigDecimal;

public class ValidateBTJStatus {

    public static boolean testCutOverZone(NumericValue vbb, NumericValue vbe_on){
        return vbe_on.getValueToMachine().compareTo(vbb.getValueToMachine()) > 0;
    }

    public static boolean testActiveZone(BigDecimal vcc, BigDecimal vbb, BigDecimal vbe, BigDecimal rbb, BigDecimal rc, BigDecimal re, BigDecimal beta){

        BigDecimal numerator = vbb.subtract(vbe, TBJ_Status.mc);
        BigDecimal denominator = beta.multiply(re,TBJ_Status.mc).add(rbb, TBJ_Status.mc);

        BigDecimal ib = numerator.divide(denominator, TBJ_Status.mc);
        BigDecimal ic = beta.multiply(ib, TBJ_Status.mc);

        BigDecimal vce = vcc.subtract(ic.multiply(rc.add(re, TBJ_Status.mc), TBJ_Status.mc), TBJ_Status.mc);

        return (vcc.compareTo(vce) >= 0) && (vce.compareTo(vbe) >= 0);
    }

    public static boolean testSaturationZone(BigDecimal vcc, BigDecimal vbb, BigDecimal vbe, BigDecimal rbb, BigDecimal rc, BigDecimal re){

        BigDecimal ten = new BigDecimal("10");
        BigDecimal zero = new BigDecimal("0");
        int nrTests = (int) vbe.doubleValue() * 10;

        BigDecimal vce_sat;
        BigDecimal numerator;
        BigDecimal denominator;
        BigDecimal ic;
        BigDecimal ib;

        boolean validResult = false;

        for (int i = 1; i <= nrTests; i++ ){
            vce_sat = (new BigDecimal(i)).divide(ten, TBJ_Status.mc);

            numerator = vcc.subtract(vce_sat, TBJ_Status.mc);
            denominator = re.add(re, TBJ_Status.mc);

            ic = numerator.divide(denominator, TBJ_Status.mc);

            if (ic.compareTo(zero) <= 0)
                continue;

            numerator = vbb.subtract(vbe.subtract(rc.multiply(ic, TBJ_Status.mc), TBJ_Status.mc), TBJ_Status.mc);
            denominator = rbb;

            ib = numerator.divide(denominator, TBJ_Status.mc);

            validResult = validResult || (ib.compareTo(zero) <= 0);
        }

        return validResult;
    }
}
