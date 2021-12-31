package pt.ipp.isep.dei.model.helpers;

import pt.ipp.isep.dei.Main;
import pt.ipp.isep.dei.math.Calculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class NumericValue {
    private BigDecimal value;
    private Multiplier multiplier;
    private Units unit;

    public NumericValue(BigDecimal value, Multiplier multiplier, Units unit) {
        this.value = value;
        this.multiplier = multiplier;
        this.unit = unit;
    }

    public NumericValue(BigDecimal value, Multiplier multiplier) {
        this.value = value;
        this.multiplier = multiplier;
    }

    public NumericValue(BigDecimal value) {
        this.value = value;
        this.multiplier = new Multiplier();
    }

    public Units getUnit() { return unit; }

    public void setUnit(Units unit) { this.unit = unit; }

    public BigDecimal getValue() { return value; }

    public void setValue(BigDecimal value) { this.value = value; }

    public Multiplier getMultiplier() { return multiplier; }

    public void setMultiplier(Multiplier multiplier) { this.multiplier = multiplier; }


    /**
     * Return the numeric value in human readable format. e.g 10 mV
     * @return numeric value in human readable format
     */
    public String getValueToHuman(){
        return value + " " + multiplier.getSymbol() + ((unit == null) ? "" : unit.toString());
    }

    public BigDecimal getValueToMachine(){
        return Calculator.multiply(this.value, this.multiplier.getBase10Power());
    }

    public void applyBestMultiplier(){

        BigDecimal realValue = this.value.multiply(this.multiplier.getBase10Power());
        BigDecimal one = new BigDecimal("1");
        BigDecimal ten = new BigDecimal("10");

        List<Multiplier> listOfMultipliers = Multiplier.getDefaultListOfMultipliers();

        List<Multiplier> filteredListOfMultipliers = (Calculator.greaterThan(realValue,one))
                ? listOfMultipliers.stream().filter(multiplier -> Calculator.greaterThanOrEqual(multiplier.getBase10Power(), one)).collect(Collectors.toList())
                : listOfMultipliers.stream().filter(multiplier -> Calculator.lowerThanOrEqual(multiplier.getBase10Power(), one)).collect(Collectors.toList());

        for(Multiplier m : filteredListOfMultipliers){

            BigDecimal proposedValue = Calculator.fraction(realValue,m.getBase10Power());

            boolean lowerValueToComparison  = Calculator.greaterThanOrEqual(proposedValue, one);
            boolean higherValueToComparison = Calculator.lowerThan(proposedValue, ten);

            if(lowerValueToComparison && higherValueToComparison){
                System.out.println(m.getPrefix() + "(" + m.getSymbol() + ") chosen");
                this.value = realValue.divide(m.getBase10Power(), Main.mc);
                this.multiplier = m;
                return;
            }
        }

    }

}
