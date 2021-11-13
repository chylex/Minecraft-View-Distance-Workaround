package chylex.viewdistance.track;

import chylex.viewdistance.mixin.ChunkMapAccessor;
import net.minecraft.core.SectionPos;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.mutable.MutableObject;
import java.util.PriorityQueue;
import java.util.Queue;
import static java.util.Comparator.comparingInt;

public class ChunkTrackerThread extends Thread {
	private final DedicatedServer dedi;
	private final ChunkMapAccessor chunkMap;
	private final ServerPlayer player;
	private final Queue<ChunkPos> remainingChunks;
	
	public ChunkTrackerThread(final DedicatedServer dedi, final ChunkMap chunkMap, final ServerPlayer player, final int viewDistance) {
		super("ViewDistanceWorkaround / " + player.getScoreboardName());
		
		this.dedi = dedi;
		this.chunkMap = (ChunkMapAccessor) chunkMap;
		this.player = player;
		
		final int centerChunkX = SectionPos.blockToSectionCoord(player.getBlockX());
		final int centerChunkZ = SectionPos.blockToSectionCoord(player.getBlockZ());
		final ChunkPos centerChunk = new ChunkPos(centerChunkX, centerChunkZ);
		
		this.remainingChunks = new PriorityQueue<>(comparingInt(pos -> distanceSquared(pos, centerChunk)));
		
		for(int x = centerChunkX - viewDistance; x <= centerChunkX + viewDistance; ++x) {
			for(int z = centerChunkZ - viewDistance; z <= centerChunkZ + viewDistance; ++z) {
				this.remainingChunks.add(new ChunkPos(x, z));
			}
		}
	}
	
	@SuppressWarnings("BusyWait")
	@Override
	public void run() {
		try {
			Thread.sleep(1250L);
		} catch (InterruptedException e) {
			return;
		}
		
		System.out.println("[ViewDistanceWorkaround] Starting delayed chunk tracking for " + player.getScoreboardName() + " (" + remainingChunks.size() + " chunks)");
		
		ChunkPos nextChunk;
		while ((nextChunk = remainingChunks.poll()) != null && player.connection.connection.isConnected()) {
			ChunkPos pos = nextChunk;
			
			dedi.executeBlocking(() -> {
				chunkMap.callUpdateChunkTracking(player, pos, new MutableObject<>(), false, true);
			});
			
			try {
				Thread.sleep(5L);
			} catch (InterruptedException e) {
				return;
			}
		}
		
		System.out.println("[ViewDistanceWorkaround] Finished delayed chunk tracking for " + player.getScoreboardName());
	}
	
	private static int distanceSquared(ChunkPos p1, ChunkPos p2) {
		int xDiff = p1.x - p2.x;
		int zDiff = p1.z - p2.z;
		return (xDiff * xDiff) + (zDiff * zDiff);
	}
}
