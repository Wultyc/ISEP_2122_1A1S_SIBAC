package pt.ipp.isep.dei.model;

public class Preference extends Fact{

    public static final String ENABLE_GUIDED_MODE  = "Enable guided mode";

    private String preference;
    private String value;

    public Preference(String p, String v) {
        preference = p;
        value = v;
    }


    public String getPreference() {
        return preference;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return (preference + " = " + value);
    }
}
