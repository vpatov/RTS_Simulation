/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg553_rts_simulation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

import java.util.Random;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Slider;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.input.MouseEvent;

import static pkg553_rts_simulation.Sim_Main.gold_disbursal;
import static pkg553_rts_simulation.Sim_Main.policies;
import static pkg553_rts_simulation.Sim_Main.policy_enactment;
import static pkg553_rts_simulation.Sim_Main.winner;
 

/**
 *
 * @author Vasia
 */
public class Main extends Application {
    
//    static Sim_Obj[][] simul_grid;
    static Map.Terrain[][] simul_grid;
    static Rectangle [][] cells;
    static int SCREEN_WIDTH = 600;
    static int SCREEN_HEIGHT = 650;
    static int X_CELLS = 200;
    static int Y_CELLS = 200;
    static int TERRAIN_SEED = 15;
    static Slider slider;
    static Random random = new Random();
    static Timer timer;
    static boolean first = true;
    
    private final static WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
    private Canvas canvas;
    private static GraphicsContext gc;
    private static Rendering rendering;
    static int CELL_SIZE = 3;
    static int CANVAS_WIDTH = X_CELLS * CELL_SIZE;
    static int CANVAS_HEIGHT = Y_CELLS * CELL_SIZE;
    static int UNIT_SIZE = 2;
    

    public Scene initialize_gui(){   
        VBox root = new VBox();
        Pane control_pane = initializeControls(800);
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    	gc = canvas.getGraphicsContext2D();
    	rendering = new Rendering(CANVAS_WIDTH, CANVAS_HEIGHT, CELL_SIZE, UNIT_SIZE);
    	
        root.getChildren().add(control_pane);
        root.getChildren().add(canvas);
        
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Mouse click: " + (int)((event.getSceneY() - 50) / 3) + "," + (int)((event.getSceneX()) / 3));
        }});
        
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        return scene;
    }
    

    public static Pane initializeControls(int screen_size){
        
        //Slider for time ticks
        slider = new Slider(0.05, 3, 2);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.50f);
        slider.setBlockIncrement(0.05f);
               
        //Play Button
        Button play_button = new Button("Play");
        play_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println("Play!");
                update_gui();
            }
        });
        
        //Pause Button
        Button pause_button = new Button("Pause");
        pause_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println("Pause!");
                if (timer != null)
                    timer.cancel();
            }
        });
        
        HBox hbox = new HBox();
        hbox.getChildren().add(slider);
        hbox.getChildren().add(play_button);
        hbox.getChildren().add(pause_button);
        
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(30);
        hbox.setMinHeight(50);
        return hbox;
    }
    
    public static void update_gui(){
       
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Sim_Main.update_state();
                Sim_Main.StatsSummary();
            	gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                int[] buffer = rendering.getUpdatedDisplay();
                PixelWriter p = gc.getPixelWriter();
                p.setPixels(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, pixelFormat, buffer, 0, CANVAS_WIDTH);

                if (Sim_Main.winner != null){
                    timer.cancel();
                    System.out.println((Sim_Main.winner == Sim_Main.red ? "Red":"Blue") + " won.");
                            
                    return;
                }
                System.out.println(Sim_Main.ticks);
                if (first){
                    timer.cancel();
                    first = false;
                }
            }
        }, 0, 30);
    }
    

    
    /**
     * @param args the command line arguments
     */
    @Override
    public void start(Stage primaryStage) {

        
        
        Sim_Main.init_simulation();
        Sim_Main.winner = null;
        Sim_Main.stch = new StochasticInput();
 
        Sim_Main.ticks = 0;
        Sim_Main.ticks_until_next_arrival = 0;
        
//        Sim_Main.policies = Policy.generate_configurations();
//        Sim_Main.stchs = StochasticInput.generate_configurations();
        
//        60,160,400,5,10,5,20	Blue policy: 180,240,400,15,10,15,8	 Stochastic Input: 100, 100, 1, 1, 1, 10
        
//        Sim_Main.init_players(new Policy(60,160,400,5,10,5,20),new Policy(180,240,400,15,10,15,8));
//        Sim_Main.stch = new StochasticInput(100,100,1,1,1,10);
        
        Sim_Main.init_players(new Policy(30,50,200,0.3,0.2,0.5,20),new Policy(180,240,400,15,10,15,8));
        Sim_Main.stch = new StochasticInput(100,100,1,1,1,10);
        
        
        Scene scene = initialize_gui();
        
        primaryStage.setTitle("RTS Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        update_gui();
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
