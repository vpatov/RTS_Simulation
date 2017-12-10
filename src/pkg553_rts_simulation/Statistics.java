package pkg553_rts_simulation;

public class Statistics {
	public static int damageDealtBlue = 0;
	public static int damageDealtRed = 0;
	public int totalGold = 0;
	public int unitsBuiltBlue = 0;
	public int unitsBuiltRed = 0;
	
	public static void updateDamage(boolean isBlue, int damage) {
		if (isBlue) damageDealtBlue += damage;
		else damageDealtRed += damage;
	}
}
