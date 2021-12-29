package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.math.Multiplier;
import pt.ipp.isep.dei.math.Units;

public class NumericValue {
    private Units unit;
    private double value;
    private Multiplier multiplier;

    public NumericValue(double value, Units unit, Multiplier multiplier) {
        this.value = value;
        this.unit = unit;
        this.multiplier = multiplier;
    }

    public NumericValue(double value, Multiplier multiplier) {
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
    public String getValueToHuman(){
        return value + " " + multiplier.getSymbol() + ((unit == null) ? "" : unit.toString());
    }

    public double getValueToMachine(){
        return value * multiplier.getBase10Power();
    }

}
