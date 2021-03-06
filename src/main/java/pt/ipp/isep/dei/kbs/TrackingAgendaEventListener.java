package pt.ipp.isep.dei.kbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.runtime.rule.Match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipp.isep.dei.model.Fact;
import pt.ipp.isep.dei.model.Justification;

@SuppressWarnings("restriction")
public class TrackingAgendaEventListener extends DefaultAgendaEventListener{
    private Map<Integer, Justification> justifications;
    private List<Match> matchList = new ArrayList<Match>();
    private List<Fact> lhs = new ArrayList<Fact>();
    private List<Fact> rhs = new ArrayList<Fact>();

    private static Logger logger = LoggerFactory.getLogger(TrackingAgendaEventListener.class);

    public TrackingAgendaEventListener(Map<Integer, Justification> justifications) {
        this.justifications = justifications;

        logger.info("Starting Tracking Agenda Event Listener");
    }

    public void resetLhs() {
        lhs.clear();
    }

    public void addLhs(Fact f) {
        lhs.add(f);
    }

    public void resetRhs() {
        rhs.clear();
    }

    public void addRhs(Fact f) {
        rhs.add(f);
    }

   /* @Override
    public void matchCancelled(MatchCancelledEvent event) {
        resetLhs();
        resetRhs();
    }*/

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        Rule rule = event.getMatch().getRule();

        String ruleName = rule.getName();
        Map<String, Object> ruleMetaDataMap = rule.getMetaData();

        logger.info("Match on rule '{}'", ruleName);

        List <Object> list = event.getMatch().getObjects();
        for (Object e : list) {
            if (e instanceof Fact) {
                lhs.add((Fact)e);
            }
        }

        for (Fact f: rhs) {
            Justification j = new Justification(ruleName, lhs, f);
            this.justifications.put(f.getId(), j);
        }

        logger.info("Justification for rule '{}' stored", ruleName);

        resetLhs();
        resetRhs();

        matchList.add(event.getMatch());
        /*
        StringBuilder sb = new StringBuilder();
        sb.append("Rule fired: " + ruleName);

        if (ruleMetaDataMap.size() > 0) {
            sb.append("\n  With [" + ruleMetaDataMap.size() + "] meta-data:");
            for (String key : ruleMetaDataMap.keySet()) {
                sb.append("\n    key=" + key + ", value=" + ruleMetaDataMap.get(key));
            }
        }

        //System.out.println(sb.toString());
         */
    }
}
