package chylex.viewdistance;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;

@Mod("viewdistanceworkaround")
public final class ViewDistanceForgeMod {
	public ViewDistanceForgeMod() {
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}
}
