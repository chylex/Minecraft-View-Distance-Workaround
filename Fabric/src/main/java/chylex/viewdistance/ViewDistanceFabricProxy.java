package chylex.viewdistance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.dedicated.DedicatedServer;

public class ViewDistanceFabricProxy implements ViewDistanceSidedProxy {
	@Override
	public DedicatedServer getDedicatedServer() {
		final Object server = FabricLoader.getInstance().getGameInstance();
		if (server instanceof DedicatedServer) {
			return (DedicatedServer)server;
		}
		
		return null;
	}
}
