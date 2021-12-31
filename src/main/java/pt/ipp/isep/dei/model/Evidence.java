package pt.ipp.isep.dei.model;

import org.jetbrains.annotations.NotNull;
import pt.ipp.isep.dei.model.helpers.Alternative;
import pt.ipp.isep.dei.model.helpers.NumericValue;
import pt.ipp.isep.dei.model.helpers.Units;

import java.util.HashMap;
import java.util.Map;

public class Evidence extends Fact implements Comparable<Evidence>{
    public static final Alternative RC = new Alternative("Collector resistance (Rc)", true, Units.Ω);
    public static final Alternative RE = new Alternative("Emitter resistance (Re)", true, Units.Ω);
    public static final Alternative RBB = new Alternative("Base resistance (Rbb)", true, Units.Ω);
    public static final Alternative VCE = new Alternative("Voltage between Collector and Emitter (Vce)", true, Units.V);
    public static final Alternative VBE = new Alternative("Voltage between Collector and Emitter (Vbe)", true, Units.V);
    public static final Alternative VBB = new Alternative("Voltage source from base circuit (Vbb)", true, Units.V);
    public static final Alternative VCC = new Alternative("Voltage source from collector circuit (Vcc)", true, Units.V);
    public static final Alternative IB  = new Alternative("Current on base circuit (Ib)", true, Units.A);
    public static final Alternative IC  = new Alternative("Current on collector circuit (Ic)", true, Units.A);
    public static final Alternative BJT_GAIN  = new Alternative("BJT gain", true, null);
    public static final Alternative VBE_ON  = new Alternative("Tension to enable BTJ (Vbe on)", true, Units.V);

    private Alternative evidence;
    private String value;
    private NumericValue numericValue;

    public Evidence(Alternative ev, String v) {
        evidence = ev;
        value = v;
    }

    public Evidence(Alternative ev, String v, NumericValue nv) {
        evidence = ev;
        value = v;
        numericValue = nv;
    }

    public Alternative getEvidence() {
        return evidence;
    }

    public String getValue() {
        return value;
    }

    public NumericValue getNumericValue() {
        return numericValue;
    }

    public String toString() {
        return (evidence + " = " + value);
    }


    @Override
    public int compareTo(@NotNull Evidence o) {
        return 0;
    }
}

