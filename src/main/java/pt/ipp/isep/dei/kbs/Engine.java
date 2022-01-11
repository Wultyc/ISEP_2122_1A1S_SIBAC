package pt.ipp.isep.dei.kbs;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.LiveQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipp.isep.dei.kbs.bjt.BTJStatus;
import pt.ipp.isep.dei.model.Justification;
import pt.ipp.isep.dei.adapters.JOPApp;
import pt.ipp.isep.dei.adapters.Adapter;

import java.util.Map;
import java.util.TreeMap;

public class Engine {
    private KieSession KS;
    private LiveQuery query;
    private TrackingAgendaEventListener agendaEventListener;
    private Map<Integer, Justification> justifications;
    private Adapter adapter;
    private BTJStatus btjStatus;

    private static Logger logger = LoggerFactory.getLogger(Engine.class);

    public Engine(){
        logger.info("Starting KBS Engine");

        //Create Justifications structure
        this.justifications = new TreeMap<Integer, Justification>();

        // load up the knowledge base
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        this.KS = kContainer.newKieSession("ksession-rules");
        this.agendaEventListener = new TrackingAgendaEventListener(this.justifications);
        this.KS.addEventListener(this.agendaEventListener);

        //Load adapter
        this.adapter = new JOPApp();
        this.adapter.init(this.KS, this.agendaEventListener);

        //Create Justification Listener instance
        JustificationListner listener = new JustificationListner(this.KS, this.justifications, this.adapter);
        this.query = this.KS.openLiveQuery("Conclusions", null, listener);

        //Load BTJStatus
        this.btjStatus = new BTJStatus(this.KS, this.agendaEventListener, this.adapter);

        logger.info("KBS Engine ready");
    }

    public void runEngine(){
        logger.info("KBS Engine running");

        //Load work memory
        loadWorkMemory();

        //Pass the repository as global to Drools context in case it is required
        this.KS.setGlobal("btjStatus",this.btjStatus);

        // Fire rules
        //this.KS.fireAllRules();
         this.KS.fireUntilHalt();

        //Close the application
        this.query.close();
        this.adapter.close();

        logger.info("KBS Engine closing");
    }

    private void loadWorkMemory(){
        this.adapter.loadWorkMemory();
    }

    public KieSession getKS() {
        return KS;
    }

    public TrackingAgendaEventListener getAgendaEventListener() {
        return agendaEventListener;
    }

    public Map<Integer, Justification> getJustifications() {
        return justifications;
    }

    public Adapter getAdapter() {
        return adapter;
    }
}
