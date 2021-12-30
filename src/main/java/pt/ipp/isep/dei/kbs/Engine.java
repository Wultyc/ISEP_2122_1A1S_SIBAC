package pt.ipp.isep.dei.kbs;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.LiveQuery;
import pt.ipp.isep.dei.model.Justification;
import pt.ipp.isep.dei.repository.ConsoleApp;
import pt.ipp.isep.dei.repository.iRepository;

import java.util.Map;
import java.util.TreeMap;

public class Engine {
    private KieSession KS;
    private LiveQuery query;
    private TrackingAgendaEventListener agendaEventListener;
    private Map<Integer, Justification> justifications;
    private iRepository repository;

    public Engine(){
        //Create Justifications structure
        this.justifications = new TreeMap<Integer, Justification>();

        // load up the knowledge base
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        this.KS = kContainer.newKieSession("ksession-rules");
        this.agendaEventListener = new TrackingAgendaEventListener(this.justifications);
        this.KS.addEventListener(this.agendaEventListener);

        //Create Justification Listener instance
        JustificationListner listener = new JustificationListner(this.KS, this.justifications);
        this.query = this.KS.openLiveQuery("Conclusions", null, listener);

        //Load repository
        this.repository = new ConsoleApp();
    }

    public void runEngine(){
        //Load work memory
        loadWorkMemory();

        //Run the inference engine
        this.KS.fireAllRules();
        // kSession.fireUntilHalt();

        //Close the application
        this.query.close();
        this.repository.close();
    }

    private void loadWorkMemory(){
        this.repository.loadWorkMemory();
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

    public iRepository getRepository() {
        return repository;
    }
}
