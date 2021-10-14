package chylex.viewdistance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.dedicated.DedicatedServer;

class ViewDistanceFabricProxy implements ViewDistanceSidedProxy {
	// TODO
	private final ViewDistanceConfig config = new ViewDistanceConfig(ViewDistanceConfig.DEFAULT_LOGIN_VIEW_DISTANCE, ViewDistanceConfig.DEFAULT_LOGIN_DELAY_SECONDS);
	
	@Override
	public DedicatedServer getDedicatedServer() {
		final Object server = FabricLoader.getInstance().getGameInstance();
		if (server instanceof DedicatedServer) {
			return (DedicatedServer)server;
		}
		
		return null;
	}
	
	@Override
	public ViewDistanceConfig getConfig(boolean reload) {
		return config;
	}
}
