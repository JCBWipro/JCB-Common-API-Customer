package com.wipro.jcb.livelink.app.reports.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * This Class is to Get Data on FuelLevel Config
 */
public class FuelLevelNAConfig {
	
	private static final Map<String, List<String>> fuelLevelNAConfig;
	private static final List<String> exceptionMachines;
	
	static {
		Map<String, List<String>> aMap = new HashMap<String, List<String>>();
		List<String> excavatorsModels = Arrays.asList("JD14B", "JD20B", "JD21B", "JD13C", "JS12B", "JS81B");
		aMap.put("Excavators", excavatorsModels);
		List<String> wheelLaodersModels = Arrays.asList("432Z0", "432UD", "430Z1");
		aMap.put("Wheel Loaders", wheelLaodersModels);
		fuelLevelNAConfig = Collections.unmodifiableMap(aMap);
		
		exceptionMachines = Arrays.asList("PUN430Z1AL2055169", "PUN432UDLL2946688","PUNJD14BAJ2575987","PUNJD14BAJ2575990","PUNJD14BCJ2575985"
				,"PUNJD14BCJ2575994","PUNJD14BCJ2575999","PUNJD14BEJ2575993","PUNJD14BHJ2575989","PUNJD14BHJ2575992","PUNJD14BJJ2575984"
				,"PUNJD14BKJ2575983","PUNJD14BKJ2575997","PUNJD14BLJ2575991","PUNJD14BPJ2575996","PUNJD14BTJ2575995","PUNJD14BVJ2575986"
				,"PUNJD20BHJ2756523","PUNJD20BTJ2756526","PUNJD21BVJ2796711","PUNJS81BKL2917969","TST432UDV01301323");
	}
	
	public static Map<String, List<String>> getFuellevelnaconfig() {
		return fuelLevelNAConfig;
	}
	
	public static List<String> getExceptionMachines() {
		return exceptionMachines;
	}

}
