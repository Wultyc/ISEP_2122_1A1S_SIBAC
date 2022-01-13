package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.Main;
import pt.ipp.isep.dei.model.helpers.HypothesisPhase;
import pt.ipp.isep.dei.model.helpers.Status;

public class Hypothesis extends Fact{
    public static final String ZONE = "BJT current Zone";
    public static final String ZONE_ACTIVE = "Active";
    public static final String ZONE_CUT_OFF = "Cut-Off";
    public static final String ZONE_SATURATION = "Saturation";

    private String description;
    private String value;
    private HypothesisPhase hypothesisPhase;
    private Status status;

    public Hypothesis(String description, String value) {
        this.description = description;
        this.value = value;
        this.hypothesisPhase = HypothesisPhase.Proposed;
        this.status = Status.Active;
        Main.agendaEventListener.addRhs(this);
    }

    public Hypothesis(String description, String value, HypothesisPhase hypothesisPhase) {
        this.description = description;
        this.value = value;
        this.hypothesisPhase = hypothesisPhase;
        this.status = Status.Active;
        Main.agendaEventListener.addRhs(this);
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public HypothesisPhase getHypothesisPhase() { return hypothesisPhase; }

    public void setHypothesisPhase(HypothesisPhase hypothesisPhase) {
        this.hypothesisPhase = hypothesisPhase;
        Main.agendaEventListener.addRhs(this);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toString() {
        return ( "Hypothesis '" + description + " is " + value + " is " + hypothesisPhase);
    }
}

