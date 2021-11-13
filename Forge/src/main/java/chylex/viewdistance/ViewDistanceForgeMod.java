package chylex.viewdistance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;

@Mod("viewdistanceworkaround")
public final class ViewDistanceForgeMod {
	public ViewDistanceForgeMod() {
		MinecraftForge.EVENT_BUS.addListener(this::onServerAboutToStart);
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}
	
	@SubscribeEvent
	public void onServerAboutToStart(final FMLServerAboutToStartEvent e) {
		final MinecraftServer server = e.getServer();
		ViewDistanceWorkaround.load(new ViewDistanceForgeProxy(server instanceof DedicatedServer ? (DedicatedServer)server : null));
	}
}
