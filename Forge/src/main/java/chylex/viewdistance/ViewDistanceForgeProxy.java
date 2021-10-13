package chylex.viewdistance;
import net.minecraft.server.dedicated.DedicatedServer;

public record ViewDistanceForgeProxy(DedicatedServer server) implements ViewDistanceSidedProxy {
	@Override
	public DedicatedServer getDedicatedServer() {
		return server;
	}
}
