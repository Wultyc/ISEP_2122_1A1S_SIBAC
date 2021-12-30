package pt.ipp.isep.dei.model.helpers;

import pt.ipp.isep.dei.tbjStatus.TBJ_Status;

import java.math.BigDecimal;
import java.math.MathContext;
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
        return this.value.multiply(this.multiplier.getBase10Power());
    }

    public void applyBestMultiplier(){

        BigDecimal realValue = this.value.multiply(this.multiplier.getBase10Power());

        List<Multiplier> listOfMultipliers = Multiplier.getDefaultListOfMultipliers();

        List<Multiplier> filteredListOfMultipliers = (realValue.compareTo(new BigDecimal("1")) > 0)
                ? listOfMultipliers.stream().filter(multiplier -> multiplier.getBase10Power().compareTo(new BigDecimal("1")) >= 0).collect(Collectors.toList())
                : listOfMultipliers.stream().filter(multiplier -> multiplier.getBase10Power().compareTo(new BigDecimal("1")) <= 0).collect(Collectors.toList());

        for(Multiplier m : filteredListOfMultipliers){

            BigDecimal proposedValue = realValue.divide(m.getBase10Power(), TBJ_Status.mc);

            int lowerValueToComparison  = proposedValue.compareTo(new BigDecimal("1"));
            int higherValueToComparison = proposedValue.compareTo(new BigDecimal("10"));

            System.out.println(m.getSymbol() + ": "+ proposedValue);
            System.out.println(">0  :" + lowerValueToComparison);
            System.out.println("<10 :" + higherValueToComparison);

            if(lowerValueToComparison >= 0 && higherValueToComparison < 0){
                System.out.println(m.getPrefix() + "(" + m.getSymbol() + ") chosen");
                this.value = realValue.divide(m.getBase10Power(), TBJ_Status.mc);
                this.multiplier = m;
                return;
            }
        }

    }

}
