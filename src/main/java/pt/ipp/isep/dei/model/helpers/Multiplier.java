package pt.ipp.isep.dei.model.helpers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Multiplier {
    private String description;
    private String prefix;
    private String symbol;
    private BigDecimal base10Power;

    public Multiplier() {
        this.description = "";
        this.prefix = "";
        this.symbol = "";
        this.base10Power = new BigDecimal("1");
    }

    public Multiplier(String description, String prefix, String symbol, BigDecimal base10Power) {
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

    public BigDecimal getBase10Power() {
        return base10Power;
    }

    public void setBase10Power(BigDecimal base10Power) {
        this.base10Power = base10Power;
    }

    public String toString(){
        return description + (symbol.isEmpty() ? "" : " (" + symbol + ") ");
    }

    public static List<Multiplier> getDefaultListOfMultipliers(){

        ArrayList<Multiplier> lm = new ArrayList<Multiplier>();

        lm.add(new Multiplier("Fundamental Unit", "","", new BigDecimal(1)));
        lm.add(new Multiplier("Giga", "giga","G", new BigDecimal(1000000000)));
        lm.add(new Multiplier("Mega", "mega","M", new BigDecimal(1000000)));
        lm.add(new Multiplier("Kilo", "kilo","k", new BigDecimal(1000)));
        lm.add(new Multiplier("Milli", "milli","m", new BigDecimal(0.001)));
        lm.add(new Multiplier("Micro", "micro","µ", new BigDecimal(0.000001)));
        lm.add(new Multiplier("Nano", "nano","n", new BigDecimal(0.000000001)));

        return lm;
    }
}
