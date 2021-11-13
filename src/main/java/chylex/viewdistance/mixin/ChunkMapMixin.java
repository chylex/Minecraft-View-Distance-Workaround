package chylex.viewdistance.mixin;
import chylex.viewdistance.ViewDistanceWorkaround;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {
	private final Set<UUID> players = new HashSet<>();
	private boolean active = false;
	
	@Shadow
	int viewDistance;
	
	@Shadow
	void updatePlayerStatus(final ServerPlayer serverPlayer, final boolean isRemoving) {}
	
	@Inject(method = "updatePlayerStatus", at = @At("HEAD"))
	private void hookUpdatePlayerStatus(final ServerPlayer serverPlayer, final boolean isAdding, final CallbackInfo ci) {
		if (!isAdding) {
			players.remove(serverPlayer.getUUID());
			active = false;
		}
		else if (players.add(serverPlayer.getUUID())) {
			final DedicatedServer dedi = ViewDistanceWorkaround.get().getDedicatedServer();
			if (dedi == null) {
				active = false;
				return;
			}
			
			active = true;
			
			new Thread(() -> {
				try {
					
					Thread.sleep(2_000L);
					dedi.execute(() -> {
						if (players.contains(serverPlayer.getUUID())) {
							updatePlayerStatus(serverPlayer, isAdding);
						}
					});
					
				} catch (final InterruptedException ignored) {
				}
			}).start();
		}
		else {
			active = false;
		}
	}
	
	@Redirect(method = "updatePlayerStatus", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ChunkMap;viewDistance:I"))
	private int getViewDistance(final ChunkMap instance) {
		return active ? 2 : viewDistance;
	}
}
