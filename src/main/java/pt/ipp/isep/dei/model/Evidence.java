package pt.ipp.isep.dei.model;

public class Evidence extends Fact{
    public static final String RC = "Collector resistance (Rc)";
    public static final String RE = "Emitter resistance (Re)";
    public static final String RB = "Base resistance (Rb)";
    public static final String VCE = "Voltage between Collector and Emitter (Vce)";
    public static final String VBE = "Voltage between Collector and Emitter (Vbe)";
    public static final String VBB = "Voltage source from base circuit (Vbb)";
    public static final String VCC = "Voltage source from collector circuit (Vcc)";
    public static final String IB  = "Current on base circuit (Ib)";
    public static final String IC  = "Current on collector circuit (Ic)";
    public static final String BJT_GAIN  = "BJT gain";
    public static final String VCE_ON  = "Tension to enable BTJ (Vce on)";

    public static final String ENABLE_GUIDED_MODE  = "Enable guided mode";


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

    public String toString() {
        return (evidence + " = " + value);
    }

}

