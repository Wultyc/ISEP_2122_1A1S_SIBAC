package pt.ipp.isep.dei.adapters;

import org.kie.api.runtime.KieSession;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;
import pt.ipp.isep.dei.model.Justification;
import pt.ipp.isep.dei.model.NumericEvidence;
import pt.ipp.isep.dei.model.Hypothesis;
import pt.ipp.isep.dei.model.Preference;
import pt.ipp.isep.dei.model.helpers.Alternative;
import pt.ipp.isep.dei.model.helpers.NumericAlternative;

import java.util.Map;

public interface Adapter {

    /** Initiates the objects required for this repository to work properly
     * @param KS
     * @param agendaEventListener
     */
    void init(KieSession KS, TrackingAgendaEventListener agendaEventListener);

    /**
     * Closes this object live connections
     */
    void close();

    /**
     * Load values into Work Memory
     */
    void loadWorkMemory();

    /**
     * Prints the explanation to the console
     * @param justifications justifications object
     * @param factNumber ID of the conclusion fact
     */
    void getHowExplanation(Map<Integer, Justification> justifications, Integer factNumber);

    /**
     * Retrieve an NumericEvidence to the inference engine
     * @param ev NumericAlternative object
     * @return NumericEvidence
     */
    NumericEvidence retrieveEvidence(NumericAlternative ev);

    /**
     * Retrieve an Preference to the inference engine
     * @param pref Preference
     * @return NumericEvidence
     */
    Preference retrievePreference(Alternative pref);

    /**
     * Prompt user to choose a new hypothesis
     * @return Hypothesis chosen by the user
     */
    Hypothesis chooseNewHypothesis();

    /**
     * Creates new evidence in case it is not defined in Work Memory
     * @param ev NumericAlternative object from NumericEvidence class
     * @return evidence if needs to be created
     */
    NumericEvidence insertNewNumericEvidence(NumericAlternative ev);

    /**
     * Creates new preference in case it is not defined in Work Memory
     * @param pref Alternative object from Preference class
     * @return preference if needs to be created
     */
    Preference insertNewPreference(Alternative pref);

    /**
     * Shows a list of facts in memory
     * @throws Exception method not implemented
     */
    void listAllFacts() throws Exception;

}
