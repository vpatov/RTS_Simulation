package pkg553_rts_simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Statistics {
        private static Statistics singleton;
    
	static String dir = System.getProperty("user.dir") + "/data/";
	
	static final String LOSS_FILE_PATH = dir + "L";
	static final String WIN_FILE_PATH = dir + "W";
        static final String STATS_FILE_PATH = dir + "stats";
        
        static File WIN_FILE;
        static File LOSS_FILE;
        static File STATS_FILE;
	
	public static final String STATS_FILE_NAME = "stats";
	public static final String FILE_EXTENSION = ".csv";
	public String statsFile = "";
	
        private static BufferedWriter win_writer;
        private static BufferedWriter loss_writer;
        private static BufferedWriter stats_writer;

        
	private static int runId = 0;
        private static int iter_count = 0;
	
	public static int damageDealtBlue = 0;
	public static int damageDealtRed = 0;
	public int totalGold = 0;
	public int unitsBuiltBlue = 0;
	public int unitsBuiltRed = 0;
	
	private Statistics() {
            set_iter_count();
            init_win_loss_files();
            init_stats_file();
            Runtime.getRuntime().addShutdownHook(new ProcessorHook());

	}
        
        class ProcessorHook extends Thread {

            @Override
            public void run(){
                try {
                    System.out.println("Terminated prematurely, wrapping up files...");
                    
                    win_writer.flush();
                    loss_writer.flush();
                    stats_writer.flush();
                    
                    // may not be necessary to flush and close.
                    
                    win_writer.close();
                    loss_writer.close();
                    stats_writer.close();
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
    }
        
        public static Statistics get_statistics(){
            if (singleton == null){
                singleton = new Statistics();
            }
            return singleton;
        }
        

	private void init_win_loss_files() {
		String header = "Run ID,Total Ticks,Timestamp,Red Policy (G1/X1/G2/X2/G3/X3/X4),Blue Policy (G1/X1/G2/X2/G3/X3/X4),Num Ticks\n";
		
		try {
			LOSS_FILE = new File(LOSS_FILE_PATH +  + iter_count + ".csv");
                        WIN_FILE = new File(WIN_FILE_PATH +  + iter_count + ".csv");
                        
			if (LOSS_FILE.createNewFile()){
                            loss_writer = new BufferedWriter(new FileWriter(LOSS_FILE, true));
                            append_to_file(LOSS_FILE, header);
                        }
			if (WIN_FILE.createNewFile()){
                            win_writer = new BufferedWriter(new FileWriter(WIN_FILE, true));
                            append_to_file(WIN_FILE, header);
                        }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
        
        private void set_iter_count(){
            File[] files = new File(dir).listFiles();

	    if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().contains(STATS_FILE_NAME))
                            iter_count++;
                }
	    }
        }
	
        private void init_stats_file(){
            String header = "Ticks,Gold Collected Blue,Gold Spent Blue,Units Built Blue,Units Lost Blue,Buildings Standing Blue,"
	    		+ "Building Health Blue,Damage Dealt Blue,Units in Enemy Territory Blue," +
	    		"Gold Collected Red,Gold Spent Red,Units Built Red,Units Lost Red,Buildings Standing Red,Building Health Red,"
	    		+ "Damage Dealt Red,Units in Enemy Territory Red\n";
            try {
                STATS_FILE = new File(STATS_FILE_PATH + iter_count + ".csv");
                if (STATS_FILE.createNewFile()){
                    stats_writer = new BufferedWriter(new FileWriter(STATS_FILE, true));
                    append_to_file(STATS_FILE, header);
                }
            }
            catch (IOException e) {
			e.printStackTrace();
            }
	    
<<<<<<< HEAD
=======
	    append_to_file(STATS_FILE, header);
>>>>>>> 72d583dbf70f3fcdfb92c43d526c78a04b860b72
        }
        
	
	
	public void append_to_file(File f, String text) {
            BufferedWriter bfwriter = f == WIN_FILE  ? win_writer  : 
                                      f == LOSS_FILE ? loss_writer : stats_writer;
            try {
                bfwriter.write(text);
                bfwriter.flush();
            }
            catch (IOException e){
                e.printStackTrace();
            }
	}
        
        
  
	public static void updateDamage(boolean isBlue, int damage) {
		if (isBlue) damageDealtBlue += damage;
		else damageDealtRed += damage;
	}
}
