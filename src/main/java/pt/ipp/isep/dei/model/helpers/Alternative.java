package pt.ipp.isep.dei.model.helpers;

public class Alternative {
    private String label;
    private boolean isNumeric;
    private Units unit;

    public Alternative(String label) {
        this.label = label;
        this.isNumeric = false;
        this.unit = null;
    }

    public Alternative(String label, boolean isNumeric, Units unit) {
        this.label = label;
        this.isNumeric = isNumeric;
        this.unit = unit;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void setNumeric(boolean numeric) {
        isNumeric = numeric;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return label;
    }
}
