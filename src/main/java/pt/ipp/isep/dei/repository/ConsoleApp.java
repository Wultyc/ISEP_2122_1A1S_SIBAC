package pt.ipp.isep.dei.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;

import pt.ipp.isep.dei.kbs.TrackingAgendaEventListener;
import pt.ipp.isep.dei.model.*;
import pt.ipp.isep.dei.model.helpers.*;

public class ConsoleApp implements iRepository{

    private KieSession KS;
    private TrackingAgendaEventListener agendaEventListener;

    private BufferedReader br;
    private List<Multiplier> listOfMultipliers;
    private Multiplier fundamentalUnitMultiplier;
    private String listOfMultipliersStr;

    private final String breakOutLine;

    public ConsoleApp(){
        this.breakOutLine = new String(new char[100]).replace("\0", "=");
    }

    /** Initiates the objects required for this repository to work properly
     * @param KS
     * @param agendaEventListener
     */
    public void init(KieSession KS, TrackingAgendaEventListener agendaEventListener){
        br = new BufferedReader(new InputStreamReader(System.in));
        this.listOfMultipliers = Multiplier.getDefaultListOfMultipliers();
        this.listOfMultipliersStr = this.processListOfMultipliers();

        this.KS = KS;
        this.agendaEventListener = agendaEventListener;
    }

    /**
     * Closes this object live connections
     */
    @Override
    public void close() {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

        ////Load default hypothesis
        //this.KS.insert(new Hypothesis(Hypothesis.ZONE, Hypothesis.ZONE_ACTIVE));
        //this.KS.insert(new Hypothesis(Hypothesis.ZONE, Hypothesis.ZONE_CUT_OVER));
        //this.KS.insert(new Hypothesis(Hypothesis.ZONE, Hypothesis.ZONE_SATURATION));

        ////Load values from problem
        //this.KS.insert(insertNewEvidence(NumericEvidence.RC));
        //this.KS.insert(insertNewEvidence(NumericEvidence.RE));
        //this.KS.insert(insertNewEvidence(NumericEvidence.RBB));
        //this.KS.insert(insertNewEvidence(NumericEvidence.VCE));
        //this.KS.insert(insertNewEvidence(NumericEvidence.VBE));
        //this.KS.insert(insertNewEvidence(NumericEvidence.VBB));
        //this.KS.insert(insertNewEvidence(NumericEvidence.VCC));
        //this.KS.insert(insertNewEvidence(NumericEvidence.IB));
        //this.KS.insert(insertNewEvidence(NumericEvidence.IC));
        //this.KS.insert(insertNewEvidence(NumericEvidence.BJT_GAIN));
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
        boolean questionFound = false;
        NumericEvidence numericEvidence = null;

        //Search for the numericEvidence on the work memory
        for (NumericEvidence e: numericEvidences) {
            if (e.getEvidence().getLabel().equals(ev.getLabel())) {
                questionFound = true;
                numericEvidence = e;
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
    public Preference retrievePreference(String pref) {
        return null;
    }

    /**
     * Prompt user to choose a new hypothesis
     * @return Hypothesis chosen by the user
     */
    @Override
    public Hypothesis chooseNewHypothesis() {
        Collection<Hypothesis> hypothesis = (Collection<Hypothesis>) this.KS.getObjects(new ClassObjectFilter(Hypothesis.class));

        String newHypothesis = "";

        Map<String, String> hypothesisList = new HashMap<>();
        hypothesisList.put(Hypothesis.ZONE_ACTIVE, Hypothesis.ZONE_ACTIVE);
        hypothesisList.put(Hypothesis.ZONE_CUT_OVER, Hypothesis.ZONE_CUT_OVER);
        hypothesisList.put(Hypothesis.ZONE_SATURATION, Hypothesis.ZONE_SATURATION);

        for(Hypothesis h : hypothesis){
            String hyp = h.getValue();
            hypothesisList.remove(hyp);
        }

        while (newHypothesis.isEmpty()){
            for(String s : hypothesisList.values()){
                String question = "Test hypothesis '" + Hypothesis.ZONE + " = " + s + "' (yes/no)";
                if (readYesOrNoFromConsole(question).equals("YES")){
                    newHypothesis = s;
                    break;
                }
            }
        }

        return new Hypothesis(Hypothesis.ZONE, newHypothesis);
    }

    /**
     * Creates new evidence in case it is not defined in Work Memory
     * @param ev NumericAlternative object from NumericEvidence class
     * @return evidence if needs to be created
     */
    public NumericEvidence insertNewNumericEvidence(NumericAlternative ev){
        NumericEvidence numericEvidence;

        NumericValue nv = readNumericValueFromConsole(ev.getLabel(), ev.getUnit());
        numericEvidence = new NumericEvidence(ev, nv.getValueToHuman(), nv);

        System.out.println(numericEvidence.toString());
        System.out.println(this.breakOutLine);

        return numericEvidence;
    }

    /**
     * Creates new preference in case it is not defined in Work Memory
     * @param pref Alternative object from Preference class
     * @return preference if needs to be created
     */
    @Override
    public Preference insertNewPreference(Alternative pref){

        String value = (pref.isYesOrNo()) ? readYesOrNoFromConsole(pref.getLabel()) : readFromConsole(pref.getLabel());
        Preference preference = new Preference(pref, value);

        System.out.println(preference.toString());
        System.out.println(this.breakOutLine);

        return preference;
    }

    @Override
    public void listAllFacts() throws Exception {
        throw new Exception("Method not implemented");
    }

    /**
     * Reads a Numeric value from console
     * @param message message to be presented to the user
     * @param unit unit of measurement of this numeric value. If none, send null
     * @return NumericValue object
     */
    private NumericValue readNumericValueFromConsole(String message, Units unit){

        BigDecimal value = readDoubleFromConsole(message);
        Multiplier multiplier = readMultiplierFromFromConsole();

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
    private BigDecimal readDoubleFromConsole(String message){
        boolean keepLoop = true;
        BigDecimal value = new BigDecimal("0");
        String valueStr = "";

        while (keepLoop){
            try {
                valueStr = readFromConsole(message);
                value = new BigDecimal(valueStr);
                keepLoop = false;
            } catch (NumberFormatException e){
                System.out.println("Invalid value!! Please insert a valid number");
            }
        }

        return value;
    }

    /**
     * Shows a list of multipliers on console and asks user to select one
     * @return multiplier object equivalent to the multiplier user choose
     */
    private Multiplier readMultiplierFromFromConsole(){
        boolean keepLoop = true;
        int searchId = -1;
        String value = "";
        Multiplier multiplier = fundamentalUnitMultiplier;

        System.out.println("Select one element from bellow list");
        System.out.println(this.listOfMultipliersStr);
        System.out.println("You can insert either the name or the symbol");
        System.out.println("If you dont what to send a multiplier, send it blank");
        while (keepLoop){
            value = readFromConsole("");

            //Sends an empty value
            if(value.equals("")){
                keepLoop = false;
                break;
            }

            //Search for the chosen multiplier
            for(Multiplier m : listOfMultipliers){
                searchId++;
                if(m.getPrefix().equalsIgnoreCase(value) || m.getSymbol().equals(value)){
                    multiplier = listOfMultipliers.get(searchId);
                    keepLoop = false;
                    break;
                }
            }

            //Validates if an error happen
            if (keepLoop){
                searchId = -1;
                System.out.println("Invalid option. Please make sure you type one element from the list");
            }
        }

        return multiplier;
    }

    /**
     * Reads an Yes or No response from console. If the value is invalid, NO will be assumed
     * @param message message to be presented to the user
     * @return YES or NO strings
     */
    private String readYesOrNoFromConsole(String message){
        String input = readFromConsole(message).toUpperCase();
        return (input.equals("NO") || input.equals("YES")) ? input : "NO";
    }

    /**
     * Shows a message and reads a String from console
     * @param message message to be presented to the user
     * @return User typed response
     */
    private String readFromConsole(String message){
        String input = "";

        System.out.print(message + ": ");

        try {
            input = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * Process listOfMultipliers object to generate the string to be used on readMultiplierFromFromConsole() and store the object for the Fundamental Unit
     * @return string to be used on readMultiplierFromFromConsole()
     */
    private String processListOfMultipliers(){
        String multipliersList = "";
        boolean isBeginningOfString = true;
        for(Multiplier m : this.listOfMultipliers){
            if(m.getBase10Power().compareTo(new BigDecimal("1")) != 0){
                multipliersList += (isBeginningOfString ? "    " : ", ") + m.getPrefix() + " ("+ m.getSymbol() +")";
                isBeginningOfString = false;
            } else {
                this.fundamentalUnitMultiplier = m;
            }
        }

        return multipliersList;
    }
}
