package pt.ipp.isep.dei.adapters;

import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;
import pt.ipp.isep.dei.model.*;
import pt.ipp.isep.dei.model.helpers.*;

import java.math.BigDecimal;
import java.util.*;
import javax.swing.*;

public class JOPApp implements Adapter {

    private KieSession KS;
    private TrackingAgendaEventListener agendaEventListener;

    private List<Multiplier> listOfMultipliers;
    private Multiplier fundamentalUnitMultiplier;

    private String windowTitle = "BTJ Status";

    private static Logger logger = LoggerFactory.getLogger(JOPApp.class);

    public JOPApp(){

    }

    /** Initiates the objects required for this repository to work properly
     * @param KS
     * @param agendaEventListener
     */
    public void init(KieSession KS, TrackingAgendaEventListener agendaEventListener){
        this.listOfMultipliers = Multiplier.getDefaultListOfMultipliers();
        this.fundamentalUnitMultiplier = new Multiplier("Fundamental Unit", "","", new BigDecimal(1));
        this.KS = KS;
        this.agendaEventListener = agendaEventListener;
    }

    /**
     * Closes this object live connections
     */
    @Override
    public void close() {

    }

    /**
     * Load values into Work Memory
     */
    @Override
    public void loadWorkMemory(){
        //Load parameters
        NumericValue nv_vb_on = new NumericValue(new BigDecimal("0.7"),this.fundamentalUnitMultiplier,Units.V);
        NumericEvidence e_vb_on = new NumericEvidence(NumericEvidence.VBE_ON,nv_vb_on.getValueToHuman(), nv_vb_on);
        this.KS.insert(e_vb_on);

        //Load preferences
        this.KS.insert(insertNewPreference(Preference.ENABLE_GUIDED_MODE));
    }

    /**
     * Prints the explanation to the console
     * @param justifications justifications object
     * @param factNumber ID of the conclusion fact
     */
    @Override
    public void getHowExplanation(Map<Integer, Justification> justifications, Integer factNumber) {
        logger.info("Start getting explanations from execution");
        String explanation = (processExplanation(justifications, factNumber, 0));
        logger.info("Explanations ready");
        printMessage(explanation);
    }

    /**
     * Retrieve an NumericEvidence to the inference engine
     * @param ev NumericAlternative object
     * @return NumericEvidence
     */
    @Override
    public NumericEvidence retrieveEvidence(NumericAlternative ev) {
        @SuppressWarnings("unchecked")
        Collection<NumericEvidence> numericEvidences = (Collection<NumericEvidence>) this.KS.getObjects(new ClassObjectFilter(NumericEvidence.class));

        //Search for the numericEvidence on the work memory
        for (NumericEvidence e: numericEvidences) {
            if (e.getEvidence().getLabel().equals(ev.getLabel())) {
                return e;
            }
        }

        NumericEvidence e = insertNewNumericEvidence(ev);

        this.KS.insert(e);

        return e;
    }

    /**
     * Retrieve an Preference to the inference engine
     * @param pref Preference
     * @return NumericEvidence
     */
    @Override
    public Preference retrievePreference(Alternative pref) {
        @SuppressWarnings("unchecked")
        Collection<Preference> preferences = (Collection<Preference>) this.KS.getObjects(new ClassObjectFilter(Preference.class));

        //Search for the numericEvidence on the work memory
        for (Preference p: preferences) {
            if (p.getPreference().getLabel().equals(pref.getLabel())) {
                return p;
            }
        }

        Preference p = insertNewPreference(pref);

        this.KS.insert(p);

        return p;
    }

    /**
     * Prompt user to choose a new hypothesis
     * @return Hypothesis chosen by the user
     */
    @Override
    public Hypothesis chooseNewHypothesis() {
        Collection<Hypothesis> hypothesis = (Collection<Hypothesis>) this.KS.getObjects(new ClassObjectFilter(Hypothesis.class));

        ArrayList<String> hypothesisList = new ArrayList<>();
        hypothesisList.add(Hypothesis.ZONE_ACTIVE);
        hypothesisList.add(Hypothesis.ZONE_CUT_OFF);
        hypothesisList.add(Hypothesis.ZONE_SATURATION);

        for(Hypothesis h : hypothesis){
            String hyp = h.getValue();
            hypothesisList.remove(hyp);
        }

        int position = Integer.parseInt(String.valueOf(readFromJOPWithListButtons("Select one Hypothesis", hypothesisList.toArray(), 0)));

        return new Hypothesis(Hypothesis.ZONE, hypothesisList.get(position), HypothesisPhase.In_Test);
    }

    /**
     * Creates new evidence in case it is not defined in Work Memory
     * @param ev NumericAlternative object from NumericEvidence class
     * @return evidence if needs to be created
     */
    public NumericEvidence insertNewNumericEvidence(NumericAlternative ev){
        NumericEvidence numericEvidence;

        NumericValue nv = readNumericValueFromJOP(ev.getLabel(), ev.getUnit());
        numericEvidence = new NumericEvidence(ev, nv.getValueToHuman(), nv);

        logger.info("{}",numericEvidence.toString());

        return numericEvidence;
    }

    /**
     * Creates new preference in case it is not defined in Work Memory
     * @param pref Alternative object from Preference class
     * @return preference if needs to be created
     */
    @Override
    public Preference insertNewPreference(Alternative pref){

        String value = (pref.isYesOrNo()) ? readYesOrNoFromJOP(pref.getLabel()) : readFromJOP(pref.getLabel());
        Preference preference = new Preference(pref, value);

        logger.info("{}",preference.toString());

        return preference;
    }

    /**
     * Shows a list of facts in memory
     * @throws Exception method not implemented
     */
    @Override
    public void listAllFacts() throws Exception {
        throw new Exception("Method not implemented");
    }

    public void printMessage(String message) {
        JOptionPane.showMessageDialog(null, message, windowTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Reads a Numeric value from console
     * @param message message to be presented to the user
     * @param unit unit of measurement of this numeric value. If none, send null
     * @return NumericValue object
     */
    private NumericValue readNumericValueFromJOP(String message, Units unit){

        BigDecimal value = readDoubleFromJOP(message);
        Multiplier multiplier = readMultiplierFromFromJOP();

        NumericValue nv = (unit == null)
                            ? new NumericValue(value, multiplier)
                            : new NumericValue(value, multiplier, unit);

        return nv;
    }

    /**
     * Reads a double from console
     * @param message message to be presented to the user
     * @return number in double format
     */
    private BigDecimal readDoubleFromJOP(String message){
        boolean keepLoop = true;
        BigDecimal value = new BigDecimal("0");
        String valueStr = "";

        while (keepLoop){
            try {
                valueStr = readFromJOP(message);
                value = new BigDecimal(valueStr);
                keepLoop = false;
            } catch (NumberFormatException e){
                printMessage("Invalid value!! Please insert a valid number");
            }
        }

        return value;
    }

    /**
     * Shows a list of multipliers on console and asks user to select one
     * @return multiplier object equivalent to the multiplier user choose
     */
    private Multiplier readMultiplierFromFromJOP(){
        Multiplier multiplier = null;
        String msg = "Select one element from bellow list";

        multiplier = (Multiplier) readFromJOPWithList(msg, listOfMultipliers.toArray(), 0);

        return multiplier;
    }

    /**
     * Reads an Yes or No response from console. If the value is invalid, NO will be assumed
     * @param message message to be presented to the user
     * @return YES or NO strings
     */
    private String readYesOrNoFromJOP(String message){
        int input = JOptionPane.showConfirmDialog(null,message,windowTitle,JOptionPane.YES_NO_OPTION);

        return (input == JOptionPane.YES_OPTION ? "YES" : "NO");
    }

    /**
     * Shows a message and reads a String from console
     * @param message message to be presented to the user
     * @return User typed response
     */
    private String readFromJOP(String message){
        String input = JOptionPane.showInputDialog(null,message,windowTitle,JOptionPane.QUESTION_MESSAGE);

        return input;
    }

    /**
     * Shows a message and reads a String from console
     * @param message message to be presented to the user
     * @return User typed response
     */
    private Object readFromJOPWithList(String message, Object[] list, int defaultID){
        Object input = JOptionPane.showInputDialog(null,message,windowTitle,JOptionPane.QUESTION_MESSAGE, null, list, list[defaultID]);

        return input;
    }



    /**
     * Shows a message and reads a String from console
     * @param message message to be presented to the user
     * @return User typed response
     */
    private Object readFromJOPWithListButtons(String message, Object[] list, int defaultID){
        Object input = JOptionPane.showOptionDialog(null, message, windowTitle,
                list.length == 2 ? JOptionPane.YES_NO_OPTION : JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                list,
                list[defaultID]);
        return input;
    }

    /**
     * Process the justification Map to retrieve the explanation
     * @param justifications justification object
     * @param factNumber fact id in analysis
     * @param level indentation level
     * @return justification of fact
     */
    @SuppressWarnings("DuplicatedCode")
    private String processExplanation(Map<Integer, Justification> justifications, Integer factNumber, int level) {
        StringBuilder sb = new StringBuilder();
        Justification j = justifications.get(factNumber);
        if (j != null) { // justification for Fact factNumber was found

            logger.info("Getting justifications for {}",j.getRuleName());

            sb.append(getIdentation(level));

            //Write the conclusions
            sb.append(j.getConclusion() + " was obtained by rule '" + j.getRuleName() + "' because");
            sb.append('\n');

            //Write the LHS memory details
            int l = level + 1;
            for (Fact f : j.getLhs()) {
                sb.append(getIdentation(l));
                sb.append("- " + f);
                sb.append('\n');
                if (f instanceof Hypothesis && f.getId() != factNumber) {
                    String s = processExplanation(justifications, f.getId(), l + 1);
                    sb.append(s);
                }
            }
        }

        return sb.toString();
    }

    /**
     * Add the indentation for the explanation
     * @param level indentation level
     * @return
     */
    private String getIdentation(int level) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < level; i++) {
            sb.append(' ');
            sb.append(' ');
            sb.append(' ');
            sb.append(' ');
        }
        return sb.toString();
    }
}
