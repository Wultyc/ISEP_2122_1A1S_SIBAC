package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.model.helpers.NumericValue;

public class Evidence extends Fact{
    public static final String RC = "Collector resistance (Rc)";
    public static final String RE = "Emitter resistance (Re)";
    public static final String RBB = "Base resistance (Rbb)";
    public static final String VCE = "Voltage between Collector and Emitter (Vce)";
    public static final String VBE = "Voltage between Collector and Emitter (Vbe)";
    public static final String VBB = "Voltage source from base circuit (Vbb)";
    public static final String VCC = "Voltage source from collector circuit (Vcc)";
    public static final String IB  = "Current on base circuit (Ib)";
    public static final String IC  = "Current on collector circuit (Ic)";
    public static final String BJT_GAIN  = "BJT gain";
    public static final String VBE_ON  = "Tension to enable BTJ (Vbe on)";


    private String evidence;
    private String value;
    private NumericValue numericValue;

    public Evidence(String ev, String v) {
        evidence = ev;
        value = v;
    }

    public Evidence(String ev, String v, NumericValue nv) {
        evidence = ev;
        value = v;
        numericValue = nv;
    }

    public String getEvidence() {
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

}

