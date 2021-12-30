package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.model.helpers.Multiplier;
import pt.ipp.isep.dei.model.helpers.Units;
import pt.ipp.isep.dei.model.helpers.NumericValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConsoleApp implements iRepository{

    private BufferedReader br;
    private List<Multiplier> listOfMultipiers;
    private Multiplier fundamentalUnitMultiplier;
    private String listOfMultipiersStr;

    public ConsoleApp(){
        br = new BufferedReader(new InputStreamReader(System.in));
        this.listOfMultipiers = Multiplier.getDefaultListOfMultipiers();
        this.listOfMultipiersStr = this.setListOfMultipiersStr();
    }

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

    @Override
    public void loadWorkMemory(){

    }

    public NumericValue readNumericValueFromConsole(String message, Units unit){

        Double value = readDoubleFromConsole(message);
        Multiplier multiplier = readMultiplierFromFromConsole();

        NumericValue nv = (unit == null)
                            ? new NumericValue(value, multiplier)
                            : new NumericValue(value, unit, multiplier);

        return nv;
    }

    public Double readDoubleFromConsole(String message){
        boolean keepLoop = true;
        Double value = 0.0;
        String valueStr = "";

        while (keepLoop){
            try {
                valueStr = readFromConsole(message);
                value = Double.parseDouble(valueStr);
                keepLoop = false;
            } catch (NumberFormatException e){
                System.out.println("Invalid value!! Please insert a valid number");
            }
        }

        return value;
    }

    public Multiplier readMultiplierFromFromConsole(){
        boolean keepLoop = true;
        int searchId = -1;
        String value = "";
        Multiplier multiplier = fundamentalUnitMultiplier;

        System.out.println("Select one element from bellow list");
        System.out.println(this.listOfMultipiersStr);
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
            for(Multiplier m : listOfMultipiers){
                searchId++;
                if(m.getPrefix().equalsIgnoreCase(value) || m.getSymbol().equals(value)){
                    multiplier = listOfMultipiers.get(searchId);
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

    public String readYesOrNoFromConsole(String message){
        String input = readFromConsole(message).toUpperCase();
        return (input.equals("NO") || input.equals("YES")) ? input : "NO";
    }

    public String readFromConsole(String message){
        String input = "";

        try {
            input = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    private String setListOfMultipiersStr(){
        String multipliersList = "";
        for(Multiplier m : this.listOfMultipiers){
            if(m.getBase10Power() != 1){
                multipliersList += "    " + m.getPrefix() + " ("+ m.getSymbol() +")" + "\n";
            } else {
                this.fundamentalUnitMultiplier = m;
            }
        }

        return multipliersList;
    }
}
