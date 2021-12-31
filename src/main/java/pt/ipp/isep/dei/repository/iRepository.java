package pt.ipp.isep.dei.repository;

import org.kie.api.runtime.KieSession;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;
import pt.ipp.isep.dei.model.Evidence;
import pt.ipp.isep.dei.model.Preference;
import pt.ipp.isep.dei.model.helpers.Alternative;
import pt.ipp.isep.dei.model.helpers.Units;

public interface iRepository {
    void init(KieSession KS, TrackingAgendaEventListener agendaEventListener);

    void close();

    void loadWorkMemory();

    Evidence retrieveEvidence(Alternative ev, boolean isNumeric, Units u);

    Preference retrievePreference(String pref);

    Evidence insertNewEvidence(Alternative ev, boolean isNumeric, Units u);

    Preference insertNewPreference(Alternative pref, boolean isYesOrNo);

}
