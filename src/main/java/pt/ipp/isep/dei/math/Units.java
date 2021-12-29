package pt.ipp.isep.dei.math;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Units {
    A,V,Ω,W;

    public static String getList(){
        return Stream.of(Units.values()).
                map(Units::name).
                collect(Collectors.joining(", "));
    }
}
