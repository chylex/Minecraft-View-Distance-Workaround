package chylex.viewdistance.command;

import chylex.viewdistance.ViewDistanceConfig;
import chylex.viewdistance.ViewDistanceWorkaround;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.dedicated.DedicatedServer;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class SetViewDistanceCommand {
	private SetViewDistanceCommand() {}
	
	private static final int DEFAULT_VIEW_DISTANCE = 0;
	
	public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("view-distance")
			.requires(s -> s.hasPermission(2))
			.executes(c -> setViewDistance(c.getSource(), DEFAULT_VIEW_DISTANCE))
			.then(literal("reload")
				.executes(c -> reloadConfig(c.getSource())))
			.then(argument("chunks", integer(1, 32))
				.executes(c -> setViewDistance(c.getSource(), IntegerArgumentType.getInteger(c, "chunks"))))
		);
	}
	
	private static int setViewDistance(final CommandSourceStack s, int chunks) {
		final DedicatedServer dedi = ViewDistanceWorkaround.get().getDedicatedServer();
		if (dedi == null) {
			s.sendFailure(new TextComponent("Internal mod error, server instance not found!"));
			return 0;
		}
		
		if (chunks == DEFAULT_VIEW_DISTANCE) {
			chunks = dedi.getProperties().viewDistance;
		}
		
		dedi.getPlayerList().setViewDistance(chunks);
		s.sendSuccess(new TextComponent("View distance set to " + chunks), true);
		return 1;
	}
	
	private static int reloadConfig(final CommandSourceStack s) {
		final ViewDistanceConfig config = ViewDistanceWorkaround.get().getConfig(true);
		s.sendSuccess(new TextComponent("View distance configuration reloaded:"), true);
		s.sendSuccess(new TextComponent("  " + config), true);
		return 1;
	}
}
