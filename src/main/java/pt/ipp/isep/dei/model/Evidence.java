package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.model.helpers.Alternative;

public class Evidence extends Fact{

    public static final String TBJ_IN_CUT_OVER_ZONE = "BTJ is in Cut-Over Zone";
    public static final String TBJ_IN_ACTIVE_ZONE = "BTJ is in Active Zone";
    public static final String TBJ_IN_SATURATION_ZONE = "BTJ is in Saturation Zone";

    private String evidence;
    private String value;

    public Evidence(String ev, String v) {
        evidence = ev;
        value = v;
    }

    public String getEvidence() {
        return evidence;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return (evidence + ": " + value);
    }
}
