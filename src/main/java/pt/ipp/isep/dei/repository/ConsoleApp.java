package pt.ipp.isep.dei.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

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
        Evidence e_vb_on = new Evidence(Evidence.VBE_ON,nv_vb_on.getValueToHuman(), nv_vb_on);
        this.KS.insert(e_vb_on);

        //Load preferences
        this.KS.insert(insertNewPreference(Preference.ENABLE_GUIDED_MODE, true));

        //Load default hypothesis
        this.KS.insert(new Hypothesis(Hypothesis.ZONE, Hypothesis.ZONE_ACTIVE));
        this.KS.insert(new Hypothesis(Hypothesis.ZONE, Hypothesis.ZONE_CUT_OVER));
        this.KS.insert(new Hypothesis(Hypothesis.ZONE, Hypothesis.ZONE_SATURATION));

        //Load values from problem
        this.KS.insert(insertNewEvidence(Evidence.RC, true, Units.Ω));
        this.KS.insert(insertNewEvidence(Evidence.RE, true, Units.Ω));
        this.KS.insert(insertNewEvidence(Evidence.RBB, true, Units.Ω));
        this.KS.insert(insertNewEvidence(Evidence.VCE, true, Units.V));
        this.KS.insert(insertNewEvidence(Evidence.VBE, true, Units.V));
        this.KS.insert(insertNewEvidence(Evidence.VBB, true, Units.V));
        this.KS.insert(insertNewEvidence(Evidence.VCC, true, Units.V));
        this.KS.insert(insertNewEvidence(Evidence.IB, true, Units.A));
        this.KS.insert(insertNewEvidence(Evidence.IC, true, Units.A));
        this.KS.insert(insertNewEvidence(Evidence.BJT_GAIN, true, null));
    }

    /**
     * Retrieve an Evidence to the inference engine
     * @param ev Evidence
     * @return Evidence
     */
    @Override
    public Evidence retrieveEvidence(Alternative ev, boolean isNumeric, Units u) {

        @SuppressWarnings("unchecked")
        Collection<Evidence> evidences = (Collection<Evidence>) this.KS.getObjects(new ClassObjectFilter(Evidence.class));
        boolean questionFound = false;
        Evidence evidence = null;

        //Search for the evidence on the work memory
        for (Evidence e: evidences) {
            if (e.getEvidence().getLabel().compareTo(ev.getLabel()) == 0) {
                questionFound = true;
                evidence = e;
                break;
            }
        }

        Evidence e = insertNewEvidence(ev, isNumeric, u);

        this.KS.insert(e);

        return e;
    }

    /**
     * Retrieve an Preference to the inference engine
     * @param pref Preference
     * @return Evidence
     */
    @Override
    public Preference retrievePreference(String pref) {
        return null;
    }

    /**
     * Creates new evidence in case it is not defined in Work Memory
     * @param ev evidence from Evidence class
     * @param isNumeric defines if its a numeric evidence
     * @param u unit of this evidence if applied
     * @return evidence if needs to be created
     */
    @Override
    public Evidence insertNewEvidence(Alternative ev, boolean isNumeric, Units u){
        Evidence evidence;

        if(isNumeric) {
            NumericValue nv = readNumericValueFromConsole(ev.getLabel(), u);
            evidence = new Evidence(ev, nv.getValueToHuman(), nv);
        } else {
            String value = readFromConsole(ev.getLabel());
            evidence = new Evidence(ev, value);
        }

        System.out.println(evidence.toString());
        System.out.println(this.breakOutLine);

        return evidence;
    }

    /**
     * Creates new preference in case it is not defined in Work Memory
     * @param pref preference from Preference class
     * @param isYesOrNo defines if its a yes/no evidence
     * @return preference if needs to be created
     */
    @Override
    public Preference insertNewPreference(Alternative pref, boolean isYesOrNo){

        String value = (isYesOrNo) ? readYesOrNoFromConsole(pref.getLabel()) : readFromConsole(pref.getLabel());
        Preference preference = new Preference(pref, value);

        System.out.println(preference.toString());
        System.out.println(this.breakOutLine);

        return preference;
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
