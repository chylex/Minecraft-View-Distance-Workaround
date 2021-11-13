package chylex.viewdistance.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkMap.class)
public interface ChunkMapAccessor {
	@Invoker
	void callUpdateChunkTracking(final ServerPlayer serverPlayer, final ChunkPos pos, final Packet<?>[] packetArrayForReuse, final boolean stopTracking, final boolean startTracking);
}
