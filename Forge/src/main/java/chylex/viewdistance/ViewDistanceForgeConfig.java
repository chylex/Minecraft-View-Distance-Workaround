package chylex.viewdistance;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

final class ViewDistanceForgeConfig {
	public static final ForgeConfigSpec SPEC;
	
	private static final ConfigValue<Integer> CFG_LOGIN_VIEW_DISTANCE;
	private static final ConfigValue<Integer> CFG_LOGIN_DELAY_SECONDS;
	
	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		CFG_LOGIN_VIEW_DISTANCE = builder.defineInRange("loginViewDistance", ViewDistanceConfig.DEFAULT_LOGIN_VIEW_DISTANCE, 1, 32);
		CFG_LOGIN_DELAY_SECONDS = builder.defineInRange("loginDelaySeconds", ViewDistanceConfig.DEFAULT_LOGIN_DELAY_SECONDS, 1, 60);
		SPEC = builder.build();
	}
	
	public static ViewDistanceConfig get() {
		return new ViewDistanceConfig(CFG_LOGIN_VIEW_DISTANCE.get(), CFG_LOGIN_DELAY_SECONDS.get());
	}
}
