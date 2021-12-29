package pt.ipp.isep.dei.model;

public class Evidence extends Fact{
    public static final String EVIDENCE_NAME = "Question to appear on the console";


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

