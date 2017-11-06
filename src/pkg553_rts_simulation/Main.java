/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg553_rts_simulation;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.Random;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Slider;
import java.lang.Thread;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
 

/**
 *
 * @author Vasia
 */
public class Main extends Application {
    
    static Simulation_Object[][] simul_grid;
    static Rectangle [][] cells;
    static int SCREEN_WIDTH = 550;
    static int SCREEN_HEIGHT = 650;
    static int X_CELLS = 200;
    static int Y_CELLS = 200;
    static int TERRAIN_SEED = 15;
    static Slider slider;
    static Random random = new Random();
    static Timer timer;
    
    //TODO kill all threads on application close
    
    
    public Scene initialize_gui(){
        initialize_simulation();
        
        VBox root = new VBox();
        Pane control_pane = initializeControls(800);
        Pane grid_pane = initializeGrid(X_CELLS,Y_CELLS);
        root.getChildren().add(control_pane);
        root.getChildren().add(grid_pane);
        
        
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        return scene;
    }
    
    public static void initialize_simulation(){
        simul_grid = Map.procedurally_generate_map(TERRAIN_SEED, X_CELLS, Y_CELLS);
    }
    
    
    public static Pane initializeControls(int screen_size){
        
        //Slider for time ticks
        slider = new Slider(0, 1, 0.5);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.25f);
        slider.setBlockIncrement(0.1f);
        
        // This might be too responsive, as the listener is triggered about
        // 50 times when you make one slide. Maybe better to replace with some
        // OnDragged Event handler or something
//        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Slider changed to " + newValue.doubleValue());
//            if (timer != null) {
//                timer.cancel();
//            }
//            update_gui();
//        });
        
        slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isNowChanging) {
                if (!isNowChanging) {
                    if (timer != null)
                        timer.cancel();
                    update_gui();
                }
            }
        });
        
        
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
        hbox.setMinHeight(100);
        return hbox;
    }
    
    public static void update_gui(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TERRAIN_SEED++;
                simul_grid = Map.procedurally_generate_map(TERRAIN_SEED, X_CELLS, Y_CELLS);
                for (int i = 0; i < X_CELLS; i++){
                    for (int j = 0; j < Y_CELLS; j++){
                        cells[i][j].setFill(getTerrainColor(simul_grid[i][j].terrain));
                    }
                }
            }
        }, 0, (long)(1000 * slider.getValue()));
           
            

    }

    public static Color getRandomColor(){
        return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }
    
    public static Color getTerrainColor(Simulation_Object.Terrain terrain){
        int r,g,b, r_off, g_off, b_off;
        
        r_off = 10 - random.nextInt(21);
        g_off = 10 - random.nextInt(21);
        b_off = 10 - random.nextInt(21);
        
        switch (terrain){
            case WATER: r = 112;    g = 205;    b = 239;    break;
            case GRASS: r = 57;     g = 145;    b = 27;     break;
            case CLIFF: r = 81;     g = 64;     b = 29;     break;
            default:
                r = 0; g = 0; b = 0;
        }
        return Color.rgb(r + r_off, g + g_off, b + b_off);
    }
    
    public static Pane initializeGrid(int width, int height){
        Pane pane = new Pane();
       
        int cell_width = SCREEN_WIDTH / width;
        cells = new Rectangle [width][height];

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                cells[i][j] = new Rectangle();
                cells[i][j].setX(i * cell_width);
                cells[i][j].setY(j * cell_width);    
                cells[i][j].setWidth(cell_width);
                cells[i][j].setHeight(cell_width);
                cells[i][j].setFill(getTerrainColor(simul_grid[i][j].terrain));
                //cells[i][j].setStroke(Color.BLACK);
                
                pane.getChildren().add(cells[i][j]);
            }
        }
        pane.setStyle("-fx-border-color: black");
        return pane;
    }

    
    /**
     * @param args the command line arguments
     */
    @Override
    public void start(Stage primaryStage) {
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
