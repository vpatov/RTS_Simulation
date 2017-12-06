package pkg553_rts_simulation;

public class Rendering {
	final static int BLACK 	= 0;
	final static int BLUE_1 = 8900346;
	final static int BLUE_2 = 4620980;
	final static int BLUE_3 = 255;
	final static int BLUE_4 = 1644912;
	final static int GREEN 	= 65280;
	final static int RED_1	= 15761536;
	final static int RED_2	= 13458524;
	final static int RED_3 	= 16711680;
	final static int RED_4	= 11674146;
	final static int WHITE 	= 16777215;
	
	static int[] colors;
	static int[] buffer;
	
	public Rendering() {
		initColors();
		initBuffer();
	}
	
	public static void initColors() {
		colors = new int[Map.Terrain.values().length];
		colors[Map.Terrain.CLIFF.ordinal()] 		= BLACK;
    	colors[Map.Terrain.GRASS.ordinal()] 		= GREEN;
    	colors[Map.Terrain.STRUCTURE.ordinal()] 	= WHITE;
    	colors[Map.Terrain.NON_TERRAIN.ordinal()] 	= WHITE;
    }
    
    public static void initBuffer() {
    	buffer = new int[Map.MAP_WIDTH * Map.MAP_HEIGHT];
    	for(int row = 0; row < Map.MAP_HEIGHT; row++) {
    		for(int col = 0; col < Map.MAP_WIDTH; col++) {
    			buffer[col + (row * Map.MAP_WIDTH)] = colors[Map.global_map[row][col].ordinal()];
    		}
    	}
    }

    public static int[] getUpdatedDisplay() {
    	int[] map = buffer;
    	Sim_State state = Sim_Main.state_buffer[Sim_Main.ticks  % Sim_Main.STATE_BUFFER_SIZE];
    	
    	//Blue Team
    	for(Structure blue : state.blue_structures) {
    		for(int row = blue.top_left.y; row <= blue.bottom_left.y; row++) {
    			for(int col = blue.top_left.x; col <= blue.top_right.x; col++) {
    				map[col + (row * Map.MAP_WIDTH)] = BLUE_4;
    			}
    		}
    	}
    	for(Unit blue : state.blue_force) {
    		int color;
    		if(blue.unit_type.type_enum == Unit_Type.TYPE.TYPE_1) color = BLUE_1;
    		else color = (blue.unit_type.type_enum == Unit_Type.TYPE.TYPE_2) ? BLUE_2 : BLUE_3;
    		map[blue.location.x + (blue.location.y * Map.MAP_WIDTH)] = color;
    	}
    	
    	//Red Team
    	for(Structure red : state.red_structures) {
    		for(int row = red.top_left.y; row <= red.bottom_left.y; row++) {
    			for(int col = red.top_left.x; col <= red.top_right.x; col++) {
    				map[col + (row * Map.MAP_WIDTH)] = RED_4;
    			}
    		}
    	}
    	for(Unit red : state.red_force) {
    		int color;
    		if(red.unit_type.type_enum == Unit_Type.TYPE.TYPE_1) color = RED_1;
    		else color = (red.unit_type.type_enum == Unit_Type.TYPE.TYPE_2) ? RED_2 : RED_3;
    		map[red.location.x + (red.location.y * Map.MAP_WIDTH)]= color; 
    	}
    	
    	return map;
    }
}
