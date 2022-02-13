package coffee.waffle.compasscommands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DefaultPosArgument;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CompassCommands implements ClientModInitializer {
  private static final int LIMIT = 29999872;
  private static final MinecraftClient client = MinecraftClient.getInstance();

  @Override
  public void onInitializeClient() {
    LiteralCommandNode<FabricClientCommandSource> compassCommand = ClientCommandManager.DISPATCHER.register(
            ClientCommandManager.literal("compass").executes(c -> {
              client.player.sendMessage(Text.of("a"), false);
              return 1;
            })
                    .then(ClientCommandManager.literal("set").then(ClientCommandManager.argument("pos", BlockPosArgumentType.blockPos())
                            .executes(c -> {
                              var fakeSource = new ServerCommandSource(null, client.player.getPos(), null, null, 0, null, null, null, null);
                              return setTarget(c.getArgument("pos", DefaultPosArgument.class).toAbsolutePos(fakeSource));
                            })))
                    .then(ClientCommandManager.literal("north").executes(c -> setTarget(0, -LIMIT)))
                    .then(ClientCommandManager.literal("south").executes(c -> setTarget(0, LIMIT)))
                    .then(ClientCommandManager.literal("east").executes(c -> setTarget(LIMIT, 0)))
                    .then(ClientCommandManager.literal("west").executes(c -> setTarget(-LIMIT, 0)))
                    .then(ClientCommandManager.literal("northwest").executes(c -> setTarget(-LIMIT, -LIMIT)))
                    .then(ClientCommandManager.literal("northeast").executes(c -> setTarget(LIMIT, -LIMIT)))
                    .then(ClientCommandManager.literal("southwest").executes(c -> setTarget(-LIMIT, LIMIT)))
                    .then(ClientCommandManager.literal("southeast").executes(c -> setTarget(LIMIT, LIMIT)))
    );

    ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("comp").redirect(compassCommand));
  }

  private static int setTarget(Vec3d vec) {
    return setTarget(new BlockPos(vec));
  }

  private static int setTarget(int x, int z) {
    return setTarget(new BlockPos(x, 64, z));
  }

  private static int setTarget(BlockPos pos) {
    client.world.setSpawnPos(pos, 0);
    client.player.sendMessage(new TranslatableText("msg.compass-commands.compass-target", pos.getX(), pos.getY(), pos.getZ()), true);
    return 1;
  }
}
