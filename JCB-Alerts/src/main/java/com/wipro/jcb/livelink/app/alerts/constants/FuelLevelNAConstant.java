package com.wipro.jcb.livelink.app.alerts.constants;

import lombok.Getter;

import java.util.*;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 *
 */
public class FuelLevelNAConstant {

    private static final Map<String, List<String>> fuelLevelNAConfig;
    @Getter
    private static final List<String> exceptionMachines;

    static {
        Map<String, List<String>> aMap = new HashMap<>();
        List<String> excavatorsModels = Arrays.asList("JD14B", "JD20B", "JD21B", "JD13C", "JS12B", "JS81B");
        aMap.put("Excavators", excavatorsModels);
        List<String> wheelLoadersModels = Arrays.asList("432Z0", "432UD", "430Z1");
        aMap.put("Wheel Loaders", wheelLoadersModels);
        fuelLevelNAConfig = Collections.unmodifiableMap(aMap);

        exceptionMachines = Arrays.asList("PUN430Z1AL2055169", "PUN432UDLL2946688","PUNJD14BAJ2575987","PUNJD14BAJ2575990","PUNJD14BCJ2575985"
                ,"PUNJD14BCJ2575994","PUNJD14BCJ2575999","PUNJD14BEJ2575993","PUNJD14BHJ2575989","PUNJD14BHJ2575992","PUNJD14BJJ2575984"
                ,"PUNJD14BKJ2575983","PUNJD14BKJ2575997","PUNJD14BLJ2575991","PUNJD14BPJ2575996","PUNJD14BTJ2575995","PUNJD14BVJ2575986"
                ,"PUNJD20BHJ2756523","PUNJD20BTJ2756526","PUNJD21BVJ2796711","PUNJS81BKL2917969","TST432UDV01301323");
    }

    public static Map<String, List<String>> getFuellevelnaconfig() {
        return fuelLevelNAConfig;
    }

}
