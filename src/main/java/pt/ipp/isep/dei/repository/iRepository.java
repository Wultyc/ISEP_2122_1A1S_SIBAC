package pt.ipp.isep.dei.repository;

import org.kie.api.runtime.KieSession;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;
import pt.ipp.isep.dei.model.Evidence;
import pt.ipp.isep.dei.model.Preference;
import pt.ipp.isep.dei.model.helpers.Alternative;
import pt.ipp.isep.dei.model.helpers.NumericAlternative;

public interface iRepository {
    void init(KieSession KS, TrackingAgendaEventListener agendaEventListener);

    void close();

    void loadWorkMemory();

    Evidence retrieveEvidence(NumericAlternative ev);

    Preference retrievePreference(String pref);

    Evidence insertNewEvidence(NumericAlternative ev);

    Preference insertNewPreference(Alternative pref);

}
