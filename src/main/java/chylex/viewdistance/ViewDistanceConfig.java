package chylex.viewdistance;

public record ViewDistanceConfig(int loginViewDistance, int loginDelaySeconds) {
	public static final int DEFAULT_LOGIN_VIEW_DISTANCE = 1;
	public static final int DEFAULT_LOGIN_DELAY_SECONDS = 3;
	
	@Override
	public String toString() {
		return "[loginViewDistance=" + loginViewDistance + ", loginDelaySeconds=" + loginDelaySeconds + ']';
	}
}
