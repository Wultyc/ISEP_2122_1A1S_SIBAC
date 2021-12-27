package pt.ipp.isep.dei.model;

import pt.ipp.isep.dei.haemorrhage.Haemorrhage;

public class Hypothesis extends Fact{
    private String description;
    private String value;

    public Hypothesis(String description, String value) {
        this.description = description;
        this.value = value;
        Haemorrhage.agendaEventListener.addRhs(this);
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return (description + " = " + value);
    }
}