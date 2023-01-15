package dev.triphora.compass_commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DefaultPosArgument;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

import static net.minecraft.server.command.CommandManager.literal;

public class CompassCommandsServer implements CommandRegistrationCallback {
	private static final int LIMIT = 29999872;

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext buildContext, CommandManager.RegistrationEnvironment environment) {
		LiteralCommandNode<ServerCommandSource> compassCommand = dispatcher.register(literal("compass")
			.then(literal("set")
				.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
					.executes(c -> setTarget(c.getArgument("pos", DefaultPosArgument.class)
						.toAbsolutePos(c.getSource()), c.getSource().getPlayer()))))
			.then(fixedLocation("north", 0, -LIMIT))
			.then(fixedLocation("south", 0, LIMIT))
			.then(fixedLocation("east", LIMIT, 0))
			.then(fixedLocation("west", -LIMIT, 0))
			.then(fixedLocation("northwest", -LIMIT, -LIMIT))
			.then(fixedLocation("northeast", LIMIT, -LIMIT))
			.then(fixedLocation("southwest", -LIMIT, LIMIT))
			.then(fixedLocation("southeast", LIMIT, LIMIT))
			.then(literal("spawn").executes(c -> setTarget(c.getSource().getPlayer().getSpawnPointPosition(), c.getSource().getPlayer())))
			.then(literal("current").executes(c -> setTarget(c.getSource().getPlayer().getPos(), c.getSource().getPlayer())))
		);
		dispatcher.register(literal("comp").redirect(compassCommand));
	}

	private static LiteralArgumentBuilder<ServerCommandSource> fixedLocation(String command, int x, int z) {
		return literal(command).executes(c -> setTarget(new BlockPos(x, 64, z), c.getSource().getPlayer()));
	}

	private static int setTarget(Vec3d vec, ServerPlayerEntity player) {
		return setTarget(new BlockPos(vec), player);
	}

	private static int setTarget(BlockPos pos, ServerPlayerEntity player) {
		player.networkHandler.sendPacket(new PlayerSpawnPositionS2CPacket(pos, 0));
		player.sendMessage(Text.of("§bSet compass target to §f" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), true);
		return 1;
	}
}
