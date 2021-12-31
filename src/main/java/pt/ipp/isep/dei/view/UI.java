package pt.ipp.isep.dei.view;

public class UI {
    /*
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
        Collection<Evidence> evidences = (Collection<Evidence>) Main.KS.getObjects(new ClassObjectFilter(Evidence.class));

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
        if (questionFound) {
            return;
        }

        //Ask for the value
        System.out.print(ev + ": ");
        Double value = Double.parseDouble(readLine());

        //Ask for the multiplier
        List<Multiplier> listOfMultipiers = Multiplier.getDefaultListOfMultipiers();
        String multipliersList = "";
        for(Multiplier m : listOfMultipiers){
            multipliersList += m.getPrefix() + " ("+ m.getSymbol() +")" + " ";
        }

        System.out.println("Choose one multiplier: " + multipliersList);
        System.out.println("If the value is in Fundamental Unit, send it blank");
        String multiplierStr = readLine();

        Multiplier multiplier = new Multiplier();

        for(Multiplier m : listOfMultipiers){
            if(m.getPrefix().equalsIgnoreCase(multiplierStr) || m.getSymbol().equalsIgnoreCase(multiplierStr)){
                multiplier = m;
                break;
            }
        }

        //Create a evidence Object and store it in the work memory
        NumericValue nv = (unit == null) ? new NumericValue(value, multiplier) : new NumericValue(value, unit, multiplier);;
        Evidence e = new Evidence(ev, nv.getValueToHuman(), nv);
        Main.KS.insert(e);

    }

    public static boolean answer(String ev, String v) {
        @SuppressWarnings("unchecked")

        //Get evidences collection from work memory
        Collection<Evidence> evidences = (Collection<Evidence>) Main.KS.getObjects(new ClassObjectFilter(Evidence.class));

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
                Main.agendaEventListener.addLhs(evidence);
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
        Main.KS.insert(e);

        //Make the evaluation
        if (value.compareTo(v) == 0) {
            Main.agendaEventListener.addLhs(e);
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
    */
}
