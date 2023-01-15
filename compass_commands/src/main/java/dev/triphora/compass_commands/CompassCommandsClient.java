package dev.triphora.compass_commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DefaultPosArgument;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.quiltmc.qsl.command.api.client.ClientCommandManager;
import org.quiltmc.qsl.command.api.client.ClientCommandRegistrationCallback;
import org.quiltmc.qsl.command.api.client.QuiltClientCommandSource;

import static org.quiltmc.qsl.command.api.client.ClientCommandManager.literal;

public class CompassCommandsClient implements ClientCommandRegistrationCallback {
	private static final int LIMIT = 29999872;
	private static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void registerCommands(CommandDispatcher<QuiltClientCommandSource> dispatcher, CommandBuildContext buildContext, CommandManager.RegistrationEnvironment environment) {
		LiteralCommandNode<QuiltClientCommandSource> compassCommand = dispatcher.register(literal("compass")
			.then(literal("set").then(ClientCommandManager.argument("pos", BlockPosArgumentType.blockPos())
				.executes(c -> {
					ServerCommandSource fakeSource = new ServerCommandSource(null, client.player.getPos(), null, null, 0, null, null, null, null);
					return setTarget(c.getArgument("pos", DefaultPosArgument.class).toAbsolutePos(fakeSource));
				})))
			.then(fixedLocation("north", 0, -LIMIT))
			.then(fixedLocation("south", 0, LIMIT))
			.then(fixedLocation("east", LIMIT, 0))
			.then(fixedLocation("west", -LIMIT, 0))
			.then(fixedLocation("northwest", -LIMIT, -LIMIT))
			.then(fixedLocation("northeast", LIMIT, -LIMIT))
			.then(fixedLocation("southwest", -LIMIT, LIMIT))
			.then(fixedLocation("southeast", LIMIT, LIMIT))
			.then(literal("spawn").executes(c -> setTarget(c.getSource().getPlayer().clientWorld.getSpawnPos())))
			.then(literal("current").executes(c -> setTarget(c.getSource().getPlayer().getPos())))
		);
		dispatcher.register(literal("comp").redirect(compassCommand));
	}

	private static LiteralArgumentBuilder<QuiltClientCommandSource> fixedLocation(String command, int x, int z) {
		return literal(command).executes(c -> setTarget(new BlockPos(x, 64, z)));
	}

	private static int setTarget(Vec3d vec) {
		return setTarget(new BlockPos(vec));
	}

	private static int setTarget(BlockPos pos) {
		client.world.setSpawnPos(pos, 0);
		client.player.sendMessage(Text.translatable("compass_commands.success", pos.getX(), pos.getY(), pos.getZ()), true);
		return 1;
	}
}
