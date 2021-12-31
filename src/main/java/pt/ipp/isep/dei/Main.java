package pt.ipp.isep.dei;

import pt.ipp.isep.dei.kbs.Engine;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;

import java.math.MathContext;

public class Main {
    public static Engine kbsEngine;
    public static TrackingAgendaEventListener agendaEventListener;
    public static final  MathContext mc = new MathContext(5);

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

