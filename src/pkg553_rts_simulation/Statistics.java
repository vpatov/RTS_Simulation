package pkg553_rts_simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Statistics {
	String dir = System.getProperty("user.dir") + "/data/";
	public static final String FILE_NAME = "StatsSummary";
	public static final String FILE_EXTENSION = ".csv";
	String file = "";
	
	public static int damageDealtBlue = 0;
	public static int damageDealtRed = 0;
	public int totalGold = 0;
	public int unitsBuiltBlue = 0;
	public int unitsBuiltRed = 0;
	
	public Statistics() {
		assureStatsFile();
	}
	
	public void assureStatsFile() {
		File f = new File(dir);
	    File[] files = f.listFiles();

	    int count = 0;
	    if (files != null) {
		    for (int i = 0; i < files.length; i++) {
		    	if (files[i].getName().contains(FILE_NAME))
		    		count++;
		    }
	    }
	    
	    file = dir + FILE_NAME + count + FILE_EXTENSION;
	    
	    String header = "Gold Collected Blue,Gold Spent Blue,Units Built Blue,Units Lost Blue,Buildings Standing Blue,"
	    		+ "Building Health Blue,Damage Dealt Blue,Units in Enemy Territory Blue," +
	    		"Gold Collected Red,Gold Spent Red,Units Built Red,Units Lost Red,Buildings Standing Red,Building Health Red,"
	    		+ "Damage Dealt Red,Units in Enemy Territory Red\n";
	    appendToStatsFile(header);
	}
	
	public void appendToStatsFile(String text) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
			out.write(text);
	        out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateDamage(boolean isBlue, int damage) {
		if (isBlue) damageDealtBlue += damage;
		else damageDealtRed += damage;
	}
}
