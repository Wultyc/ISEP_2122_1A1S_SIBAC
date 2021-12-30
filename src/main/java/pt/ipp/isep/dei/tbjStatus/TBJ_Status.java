package pt.ipp.isep.dei.tbjStatus;

import pt.ipp.isep.dei.kbs.Engine;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;

public class TBJ_Status {
    public static Engine kbsEngine;
    public static TrackingAgendaEventListener agendaEventListener;

    public static final void main(String[] args) {

        kbsEngine = new Engine();

        //FIXME:
        //  - Implement proper Java Event Listner class to avoid this global reference
        //  - As workaround, the agendaEventListener is returned from the engine class
        TBJ_Status.agendaEventListener = kbsEngine.getAgendaEventListener();

        try {
            kbsEngine.runEngine();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}

