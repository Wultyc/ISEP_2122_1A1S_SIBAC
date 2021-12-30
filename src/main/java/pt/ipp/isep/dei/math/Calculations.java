package pt.ipp.isep.dei.math;

import pt.ipp.isep.dei.model.helpers.NumericValue;

public class Calculations {
    public static boolean testCutOverZone(NumericValue vbb, NumericValue vbe_on){
        return vbe_on.getValueToMachine() > vbb.getValueToMachine();
    }

    public static boolean testActiveZone(){
        return false;
    }

    public static boolean testSaturationZone(){
        return false;
    }
}
