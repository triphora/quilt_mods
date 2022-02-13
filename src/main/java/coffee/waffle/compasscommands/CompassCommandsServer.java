package coffee.waffle.compasscommands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DefaultPosArgument;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.literal;

public class CompassCommandsServer implements DedicatedServerModInitializer {
  private static final int LIMIT = 29999872;

  @Override
  public void onInitializeServer() {
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      LiteralCommandNode<ServerCommandSource> compassCommand = dispatcher.register(literal("compass")
              .then(literal("set")
                      .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                              .executes(c -> setTarget(c.getArgument("pos", DefaultPosArgument.class)
                                      .toAbsolutePos(c.getSource()), c.getSource().getPlayer()))))
              .then(literal("north").executes(c -> setTarget(0, -LIMIT, c.getSource().getPlayer())))
              .then(literal("south").executes(c -> setTarget(0, LIMIT, c.getSource().getPlayer())))
              .then(literal("east").executes(c -> setTarget(LIMIT, 0, c.getSource().getPlayer())))
              .then(literal("west").executes(c -> setTarget(-LIMIT, 0, c.getSource().getPlayer())))
              .then(literal("northwest").executes(c -> setTarget(-LIMIT, -LIMIT, c.getSource().getPlayer())))
              .then(literal("northeast").executes(c -> setTarget(LIMIT, -LIMIT, c.getSource().getPlayer())))
              .then(literal("southwest").executes(c -> setTarget(-LIMIT, LIMIT, c.getSource().getPlayer())))
              .then(literal("southeast").executes(c -> setTarget(LIMIT, LIMIT, c.getSource().getPlayer())))
      );
      dispatcher.register(literal("comp").redirect(compassCommand));
    });
  }

  private static int setTarget(Vec3d vec, ServerPlayerEntity player) {
    return setTarget(new BlockPos(vec), player);
  }

  private static int setTarget(int x, int z, ServerPlayerEntity player) {
    return setTarget(new BlockPos(x, 64, z), player);
  }

  private static int setTarget(BlockPos pos, ServerPlayerEntity player) {
    player.networkHandler.sendPacket(new PlayerSpawnPositionS2CPacket(pos, 0));
    player.sendMessage(Text.of("§bSet compass target to §f" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), true);
    return 1;
  }
}
