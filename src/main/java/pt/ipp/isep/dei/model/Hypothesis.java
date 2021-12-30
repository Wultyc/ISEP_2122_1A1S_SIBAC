package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.model.helpers.Status;
import pt.ipp.isep.dei.tbjStatus.TBJ_Status;

public class Hypothesis extends Fact{
    public static final String ZONE = "BJT current Zone";
    public static final String ZONE_ACTIVE = "Active";
    public static final String ZONE_CUT_OVER = "Cut-over";
    public static final String ZONE_SATURATION = "Saturation";

    private String description;
    private String value;
    private Status status;

    public Hypothesis(String description, String value) {
        this.description = description;
        this.value = value;
        this.status = Status.Proposed;
        TBJ_Status.agendaEventListener.addRhs(this);
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) {
        this.status = status;
        TBJ_Status.agendaEventListener.addRhs(this);
    }

    public String toString() {
        return ( "Hypothesis '" + description + " is " + value + "' is " + status);
    }
}

