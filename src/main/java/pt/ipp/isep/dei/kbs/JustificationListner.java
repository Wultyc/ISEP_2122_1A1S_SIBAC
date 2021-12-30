package pt.ipp.isep.dei.kbs;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Row;
import org.kie.api.runtime.rule.ViewChangedEventListener;

import pt.ipp.isep.dei.model.Conclusion;
import pt.ipp.isep.dei.model.Justification;
import pt.ipp.isep.dei.tbjStatus.TBJ_Status;

import java.util.Map;

public class JustificationListner implements ViewChangedEventListener {
    private KieSession KS;
    private Map<Integer, Justification> justifications;

    public JustificationListner(KieSession KS, Map<Integer, Justification> justifications) {
        this.KS = KS;
        this.justifications = justifications;
    }

    @Override
    public void rowDeleted(Row row) {
    }

    @Override
    public void rowInserted(Row row) {
        Conclusion conclusion = (Conclusion) row.get("$conclusion");
        System.out.println(">>>" + conclusion.toString());

        //System.out.println(TBJ_Status.justifications);
        How how = new How(this.justifications);
        System.out.println(how.getHowExplanation(conclusion.getId()));

        // stop inference Engine after as soon as got a conclusion
        this.KS.halt();
    }

    @Override
    public void rowUpdated(Row row) {
    }
}
