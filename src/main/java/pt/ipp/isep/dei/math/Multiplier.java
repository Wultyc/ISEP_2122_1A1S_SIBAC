package pt.ipp.isep.dei.math;

import java.util.ArrayList;
import java.util.List;

public class Multiplier {
    private String prefix;
    private String symbol;
    private double base10Power;

    public Multiplier() {
        this.prefix = "";
        this.symbol = "";
        this.base10Power = 1;
    }

    public Multiplier(String prefix, String symbol, double base10Power) {
        this.prefix = prefix;
        this.symbol = symbol;
        this.base10Power = base10Power;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getBase10Power() {
        return base10Power;
    }

    public void setBase10Power(double base10Power) {
        this.base10Power = base10Power;
    }

    public static List<Multiplier> getDefaultListOfMultipiers(){

        ArrayList<Multiplier> lm = new ArrayList<Multiplier>();

        lm.add(new Multiplier("","", 1));

        return lm;
    }
}
