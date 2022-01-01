package pt.ipp.isep.dei.model.helpers;

public class Alternative {
    public static final String TYPE_YES_OR_NO = "Yes or No";

    private String label;
    private String type;

    public Alternative(String label, String type) {
        this.label = label;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isYesOrNo(){
        return this.type.equals(TYPE_YES_OR_NO);
    }

    @Override
    public String toString() {
        return label;
    }
}
