package chylex.viewdistance;

import net.fabricmc.api.DedicatedServerModInitializer;

public class ViewDistanceFabricMod implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		ViewDistanceWorkaround.load(new ViewDistanceFabricProxy());
	}
}
