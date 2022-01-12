package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.model.helpers.Alternative;

public class Evidence extends Fact{

    public static final String TBJ_IN_CUT_OVER_ZONE = "BTJ is in Cut-Over Zone";
    public static final String TBJ_IN_ACTIVE_ZONE = "BTJ is in Active Zone";
    public static final String TBJ_IN_SATURATION_ZONE = "BTJ is in Saturation Zone";
    public static final String ZONE_UNKNOWN_VALIDATION = "VBB value is equal to VBE On. Need to test the physical component";
    public static final String CUT_OFF_ZONE_VALIDATION = "VBB is lower than VBE On - (VBB < VBE On)";
    public static final String ACTIVE_ZONE_VALIDATION = "VCE is in between VCC and VBE On - (VCC > VCE > VBE On)";

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
