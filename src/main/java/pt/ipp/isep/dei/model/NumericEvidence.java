package pt.ipp.isep.dei.model;

import org.jetbrains.annotations.NotNull;
import pt.ipp.isep.dei.model.helpers.NumericAlternative;
import pt.ipp.isep.dei.model.helpers.NumericValue;
import pt.ipp.isep.dei.model.helpers.Units;

import java.math.BigDecimal;

public class NumericEvidence extends Fact{
    public static final NumericAlternative RC = new NumericAlternative("Collector resistor (Rc)", true, Units.Ω);
    public static final NumericAlternative RE = new NumericAlternative("Emitter resistor (Re)", true, Units.Ω);
    public static final NumericAlternative RBB = new NumericAlternative("Base resistor (Rbb)", true, Units.Ω);
    public static final NumericAlternative VCE = new NumericAlternative("Voltage between Collector and Emitter (Vce)", true, Units.V);
    public static final NumericAlternative VBE = new NumericAlternative("Voltage between Collector and Emitter (Vbe)", true, Units.V);
    public static final NumericAlternative VBB = new NumericAlternative("Voltage source from base circuit (Vbb)", true, Units.V);
    public static final NumericAlternative VCC = new NumericAlternative("Voltage source from collector circuit (Vcc)", true, Units.V);
    public static final NumericAlternative IB  = new NumericAlternative("Current on base circuit (Ib)", true, Units.A);
    public static final NumericAlternative IC  = new NumericAlternative("Current on collector circuit (Ic)", true, Units.A);
    public static final NumericAlternative BJT_GAIN  = new NumericAlternative("BJT gain", true, null);
    public static final NumericAlternative VBE_ON  = new NumericAlternative("Tension to enable BTJStatus (Vbe on)", true, Units.V);

    private NumericAlternative evidence;
    private String value;
    private NumericValue numericValue;

    public NumericEvidence(NumericAlternative ev, String v, NumericValue nv) {
        evidence = ev;
        value = v;
        numericValue = nv;
    }

    public NumericAlternative getEvidence() {
        return evidence;
    }

    public String getValue() {
        return value;
    }

    public NumericValue getNumericValue() {
        return numericValue;
    }

    public BigDecimal getNormValue() {
        return numericValue.getValueToMachine();
    }

    public String toString() {
        return (evidence + " = " + value);
    }

}

