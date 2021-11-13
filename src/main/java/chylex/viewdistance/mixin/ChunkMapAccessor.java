package chylex.viewdistance.mixin;

import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkMap.class)
public interface ChunkMapAccessor {
	@Invoker
	void callUpdateChunkTracking(final ServerPlayer serverPlayer, final ChunkPos pos, final MutableObject<ClientboundLevelChunkWithLightPacket> chunkPacket, final boolean stopTracking, final boolean startTracking);
}
