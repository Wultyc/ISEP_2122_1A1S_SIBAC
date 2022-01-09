package pt.ipp.isep.dei.repository;

import org.kie.api.runtime.KieSession;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;
import pt.ipp.isep.dei.model.NumericEvidence;
import pt.ipp.isep.dei.model.Hypothesis;
import pt.ipp.isep.dei.model.Preference;
import pt.ipp.isep.dei.model.helpers.Alternative;
import pt.ipp.isep.dei.model.helpers.NumericAlternative;

public interface iRepository {
    void init(KieSession KS, TrackingAgendaEventListener agendaEventListener);

    void close();

    void loadWorkMemory();

    NumericEvidence retrieveEvidence(NumericAlternative ev);

    Preference retrievePreference(Alternative pref);

    Hypothesis chooseNewHypothesis();

    NumericEvidence insertNewNumericEvidence(NumericAlternative ev);

    Preference insertNewPreference(Alternative pref);

    void listAllFacts() throws Exception;

}
