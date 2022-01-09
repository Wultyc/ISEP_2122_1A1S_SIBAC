package pt.ipp.isep.dei;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipp.isep.dei.kbs.Engine;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;

import java.math.MathContext;

public class Main {
    public static Engine kbsEngine;
    public static TrackingAgendaEventListener agendaEventListener;
    public static final  MathContext mc = new MathContext(5);

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        kbsEngine = new Engine();

        //FIXME:
        //  - Implement proper Java Event Listner class to avoid this global reference
        //  - As workaround, the agendaEventListener is returned from the engine class
        Main.agendaEventListener = kbsEngine.getAgendaEventListener();

        try {
            kbsEngine.runEngine();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}

