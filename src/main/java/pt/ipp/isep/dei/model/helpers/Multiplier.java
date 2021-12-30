package pt.ipp.isep.dei.model.helpers;

import java.util.ArrayList;
import java.util.List;

public class Multiplier {
    private String description;
    private String prefix;
    private String symbol;
    private double base10Power;

    public Multiplier() {
        this.description = "";
        this.prefix = "";
        this.symbol = "";
        this.base10Power = 1;
    }

    public Multiplier(String description, String prefix, String symbol, double base10Power) {
        this.description = description;
        this.prefix = prefix;
        this.symbol = symbol;
        this.base10Power = base10Power;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getPrefix() { return prefix; }

    public void setPrefix(String prefix) { this.prefix = prefix; }

    public String getSymbol() { return symbol; }

    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getBase10Power() {
        return base10Power;
    }

    public void setBase10Power(double base10Power) {
        this.base10Power = base10Power;
    }

    public static List<Multiplier> getDefaultListOfMultipiers(){

        ArrayList<Multiplier> lm = new ArrayList<Multiplier>();

        lm.add(new Multiplier("giga", "giga","G", 1000000000));
        lm.add(new Multiplier("mega", "mega","M", 1000000));
        lm.add(new Multiplier("kilo", "kilo","k", 1000));
        lm.add(new Multiplier("hecto", "hecto","h", 100));
        lm.add(new Multiplier("deca", "deca","da", 1));
        lm.add(new Multiplier("Fundamental Unit", "","", 1));
        lm.add(new Multiplier("deci", "deci","d", 0.1));
        lm.add(new Multiplier("centi", "centi","c", 0.01));
        lm.add(new Multiplier("milli", "milli","m", 0.001));
        lm.add(new Multiplier("micro", "micro","µ", 0.000001));
        lm.add(new Multiplier("nano", "nano","n", 0.000000001));

        return lm;
    }
}
