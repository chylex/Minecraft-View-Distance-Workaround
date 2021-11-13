package chylex.viewdistance.mixin;
import chylex.viewdistance.ViewDistanceWorkaround;
import chylex.viewdistance.track.ChunkTrackerThread;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {
	private final Map<UUID, ChunkTrackerThread> playerThreads = new HashMap<>();
	private boolean active = false;
	
	@Shadow
	int viewDistance;
	
	@Inject(method = "updatePlayerStatus", at = @At("HEAD"))
	private void hookUpdatePlayerStatus(final ServerPlayer serverPlayer, final boolean isAdding, final CallbackInfo ci) {
		final UUID uuid = serverPlayer.getUUID();
		
		if (!isAdding) {
			var thread = playerThreads.remove(uuid);
			if (thread != null) {
				thread.interrupt();
			}
			
			active = false;
		}
		else if (!playerThreads.containsKey(uuid)) {
			final DedicatedServer dedi = ViewDistanceWorkaround.get().getDedicatedServer();
			if (dedi == null) {
				active = false;
				return;
			}
			
			active = true;
			
			@SuppressWarnings("ConstantConditions")
			final ChunkMap chunkMap = (ChunkMap)(Object)this;
			final ChunkTrackerThread thread = new ChunkTrackerThread(dedi, chunkMap, serverPlayer, viewDistance);
			playerThreads.put(uuid, thread);
			thread.start();
		}
		else {
			active = false;
		}
	}
	
	@Inject(method = "updatePlayerStatus", at = @At("RETURN"))
	private void hookUpdatePlayerStatusEnd(final ServerPlayer serverPlayer, final boolean isAdding, final CallbackInfo ci) {
		active = false;
	}
	
	@Redirect(method = "updatePlayerStatus", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ChunkMap;viewDistance:I"))
	private int getViewDistance(final ChunkMap instance) {
		return active ? 1 : viewDistance;
	}
}
