package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.model.helpers.Alternative;

public class Preference extends Fact{

    public static final Alternative ENABLE_GUIDED_MODE  = new Alternative("Enable guided mode");

    private Alternative preference;
    private String value;

    public Preference(Alternative p, String v) {
        preference = p;
        value = v;
    }

    public Alternative getPreference() {
        return preference;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return (preference + " = " + value);
    }
}
