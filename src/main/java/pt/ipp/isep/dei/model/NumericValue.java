package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.math.Multiplier;
import pt.ipp.isep.dei.math.Units;

public class NumericValue {
    private Units unit;
    private double value;
    private Multiplier multiplier;

    public NumericValue(double value, Units unit, Multiplier multiplier) {
        this.unit = unit;
        this.value = value;
        this.multiplier = multiplier;
    }

    public Units getUnit() { return unit; }

    public void setUnit(Units unit) { this.unit = unit; }

    public double getValue() { return value; }

    public void setValue(double value) { this.value = value; }

    public Multiplier getMultiplier() { return multiplier; }

    public void setMultiplier(Multiplier multiplier) { this.multiplier = multiplier; }


    /**
     * Return the numeric value in human readable format. e.g 10 mV
     * @return numeric value in human readable format
     */
    public String getToHuman(){
        return value*multiplier.getBase10Power() + " " + multiplier.getSymbol() + unit.toString();
    }

}
