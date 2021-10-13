package chylex.viewdistance;

public final class ViewDistanceWorkaround {
	private static ViewDistanceSidedProxy instance;
	
	public static void load(final ViewDistanceSidedProxy proxy) {
		if (instance != null) {
			throw new IllegalStateException("ViewDistanceWorkaround is already loaded!");
		}
		else {
			instance = proxy;
		}
	}
	
	public static ViewDistanceSidedProxy get() {
		return instance;
	}
}
