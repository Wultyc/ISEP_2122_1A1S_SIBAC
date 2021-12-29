package pt.ipp.isep.dei.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.kie.api.runtime.ClassObjectFilter;

import pt.ipp.isep.dei.math.Multiplier;
import pt.ipp.isep.dei.math.Units;
import pt.ipp.isep.dei.model.NumericValue;
import pt.ipp.isep.dei.tbjStatus.TBJ_Status;
import pt.ipp.isep.dei.model.Evidence;

public class UI {
    private static BufferedReader br;

    public static void uiInit() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void uiClose() {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void askValue(String ev, Units unit) throws Exception {
        @SuppressWarnings("unchecked")

        //Get evidences collection from work memory
        Collection<Evidence> evidences = (Collection<Evidence>) TBJ_Status.KS.getObjects(new ClassObjectFilter(Evidence.class));

        //Define control variables
        boolean questionFound = false;
        Evidence evidence = null;

        //Search for evidence ev on the work memory
        for (Evidence e: evidences) {
            if (e.getEvidence().compareTo(ev) == 0) {
                questionFound = true;
                evidence = e;
                break;
            }
        }

        // Value not found. Need to ask the user for it
        if (!questionFound) {

            //Ask for the value
            System.out.print(ev + ": ");
            Double value = Double.parseDouble(readLine());

            //Ask for the multiplier
            List<Multiplier> listOfMultipiers = Multiplier.getDefaultListOfMultipiers();
            String multipliersList = "";
            for(Multiplier m : listOfMultipiers){
                multipliersList += m.getPrefix() + " ";
            }

            String multiplierStr = readLine();
            Multiplier multiplier = new Multiplier();

            System.out.println("Choose one multiplier: " + multipliersList);
            for(Multiplier m : listOfMultipiers){
                if(m.getPrefix().equalsIgnoreCase(multiplierStr)){
                    multiplier = m;
                    break;
                }
            }

            //Create a evidence Object and store it in the work memory
            NumericValue nv = new NumericValue(value, unit, multiplier);
            Evidence e = new Evidence(ev, nv.getToHuman(), nv);
            TBJ_Status.KS.insert(e);

        }

    }

    public static void chooseHypothesis(){

    }

    public static boolean answer(String ev, String v) {
        @SuppressWarnings("unchecked")

        //Get evidences collection from work memory
        Collection<Evidence> evidences = (Collection<Evidence>) TBJ_Status.KS.getObjects(new ClassObjectFilter(Evidence.class));

        //Define control variables
        boolean questionFound = false;
        Evidence evidence = null;

        //Search for evidence ev on the work memory
        for (Evidence e: evidences) {
            if (e.getEvidence().compareTo(ev) == 0) {
                questionFound = true;
                evidence = e;
                break;
            }
        }

        //If evidence is found, compare with the value passed as argument and return
        if (questionFound) {
            if (evidence.getValue().compareTo(v) == 0) {
                TBJ_Status.agendaEventListener.addLhs(evidence);
                return true;
            } else {
                return false;
            }
        }

        //Evidence not found. Ask for the value
        System.out.print(ev + ": ");
        String value = readLine();

        //Create a new Evidence object and store it in work memory
        Evidence e = new Evidence(ev, value);
        TBJ_Status.KS.insert(e);

        //Make the evaluation
        if (value.compareTo(v) == 0) {
            TBJ_Status.agendaEventListener.addLhs(e);
            return true;
        } else {
            return false;
        }
    }

    private static String readLine() {
        String input = "";

        try {
            input = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

}
