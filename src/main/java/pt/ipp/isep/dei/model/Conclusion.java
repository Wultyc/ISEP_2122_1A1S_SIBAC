package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.Main;

public class Conclusion extends Fact{
    public static final String ZONE_CUTOVER = "BJT current zone is Cut Over";
    public static final String ZONE_ACTIVE = "BJT current zone is Active";
    public static final String ZONE_SATURATION = "BJT current zone is Saturation";
    public static final String ZONE_UNKNOWN = "Cannot determinate BJT current zone";

    private String description;

    public Conclusion(String description) {
        this.description = description;
        Main.agendaEventListener.addRhs(this);
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return ("Conclusion: " + description);
    }

}
