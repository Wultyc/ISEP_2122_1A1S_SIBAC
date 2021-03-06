package pt.ipp.isep.dei.kbs;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Row;
import org.kie.api.runtime.rule.ViewChangedEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipp.isep.dei.adapters.Adapter;
import pt.ipp.isep.dei.model.Conclusion;
import pt.ipp.isep.dei.model.Justification;

import java.util.Map;

public class JustificationListner implements ViewChangedEventListener {
    private KieSession KS;
    private Map<Integer, Justification> justifications;
    private Adapter adapter;

    private static Logger logger = LoggerFactory.getLogger(JustificationListner.class);

    public JustificationListner(KieSession KS, Map<Integer, Justification> justifications, Adapter adapter) {
        this.KS = KS;
        this.justifications = justifications;
        this.adapter = adapter;
    }

    @Override
    public void rowDeleted(Row row) {
    }

    @Override
    public void rowInserted(Row row) {
        Conclusion conclusion = (Conclusion) row.get("$conclusion");
        logger.info("Conclusion setted to '{}'", conclusion.toString());

        //Send the explanation to the used
        this.adapter.getHowExplanation(this.justifications, conclusion.getId());

        // stop inference Engine after as soon as got a conclusion
        this.KS.halt();
    }

    @Override
    public void rowUpdated(Row row) {
    }
}
