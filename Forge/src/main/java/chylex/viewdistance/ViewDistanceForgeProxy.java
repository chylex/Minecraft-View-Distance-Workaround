package chylex.viewdistance;
import net.minecraft.server.dedicated.DedicatedServer;

final class ViewDistanceForgeProxy implements ViewDistanceSidedProxy {
	private final DedicatedServer server;
	private ViewDistanceConfig config;
	
	public ViewDistanceForgeProxy(DedicatedServer server) {
		this.server = server;
		getConfig(true);
	}
	
	@Override
	public DedicatedServer getDedicatedServer() {
		return server;
	}
	
	@Override
	public ViewDistanceConfig getConfig(boolean reload) {
		if (reload) {
			config = ViewDistanceForgeConfig.get();
		}
		
		return config;
	}
}
