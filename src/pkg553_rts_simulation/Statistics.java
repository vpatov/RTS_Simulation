package pkg553_rts_simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Statistics {
	static String dir = System.getProperty("user.dir") + "/data/";
	
	public static final String LOSS_FILE = dir + "L.csv";
	public static final String WIN_FILE = dir + "W.csv";
	
	public static final String STATS_FILE_NAME = "StatsSummary";
	public static final String FILE_EXTENSION = ".csv";
	public String statsFile = "";
	
	public int runId = 0;
	
	public static int damageDealtBlue = 0;
	public static int damageDealtRed = 0;
	public int totalGold = 0;
	public int unitsBuiltBlue = 0;
	public int unitsBuiltRed = 0;
	
	public Statistics() {
		assureWinLossFiles();
		assureStatsFile();
	}
	
	public void assureWinLossFiles() {
		String header = "Run ID,Timestamp,Red Policy (G1/X1/G2/X2/G3/X3/X4),Blue Policy (G1/X1/G2/X2/G3/X3/X4),Num Ticks\n";
		
		try {
			File f = new File(LOSS_FILE);
			if (f.createNewFile()) appendToFile(LOSS_FILE, header);
			
			f = new File(WIN_FILE);
			if (f.createNewFile()) appendToFile(WIN_FILE, header);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void assureStatsFile() {
            File f = new File(dir);
	    File[] files = f.listFiles();

	    int count = 0;
	    if (files != null) {
		    for (int i = 0; i < files.length; i++) {
		    	if (files[i].getName().contains(STATS_FILE_NAME))
		    		count++;
		    }
	    }
	    runId = count;
	    
	    statsFile = dir + STATS_FILE_NAME + count + FILE_EXTENSION;
	    
	    String header = "Gold Collected Blue,Gold Spent Blue,Units Built Blue,Units Lost Blue,Buildings Standing Blue,"
	    		+ "Building Health Blue,Damage Dealt Blue,Units in Enemy Territory Blue," +
	    		"Gold Collected Red,Gold Spent Red,Units Built Red,Units Lost Red,Buildings Standing Red,Building Health Red,"
	    		+ "Damage Dealt Red,Units in Enemy Territory Red\n";
	    appendToFile(statsFile, header);
	}
	
	public void appendToFile(String file, String text) {
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
