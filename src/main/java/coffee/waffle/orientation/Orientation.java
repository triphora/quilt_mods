package coffee.waffle.orientation;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.TranslatableText;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSLASH;

public class Orientation implements ClientModInitializer {
  public static KeyBinding alignPlayer;

  public static double normalizeHeadYaw(double yaw) {
    yaw = yaw % 360;
    if (yaw > 180 || yaw < -180) {
      double mod = yaw % 180;
      if (mod > 0) {
        yaw = -180 + mod;
      } else if (mod < 0) {
        yaw = 180 + mod;
      }
    }
    return yaw;
  }

  public static double roundYaw(double yaw) {
    if (yaw >= 0 && yaw < 22.5) {
      yaw = 0;
    }
    if (yaw >= 22.5 && yaw < 67.5) {
      yaw = 45;
    }
    if (yaw >= 67.5 && yaw < 112.5) {
      yaw = 90;
    }
    if (yaw >= 112.5 && yaw < 157.5) {
      yaw = 135;
    }
    if (yaw >= 157.5 && yaw <= 180) {
      yaw = 180;
    }
    if (yaw <= 0 && yaw > -22.5) {
      yaw = 0;
    }
    if (yaw <= -22.5 && yaw > -67.5) {
      yaw = -45;
    }
    if (yaw <= -67.5 && yaw > -112.5) {
      yaw = -90;
    }
    if (yaw <= -112.5 && yaw > -157.5) {
      yaw = -135;
    }
    if (yaw <= -157.5 && yaw >= -180) {
      yaw = 180;
    }

    return yaw;
  }

  @Override
  public void onInitializeClient() {
    final String category = "key.categories.orientation";
    alignPlayer = new KeyBinding("key.orientation.align", GLFW_KEY_BACKSLASH, category);
    KeyBindingHelper.registerKeyBinding(alignPlayer);
    ClientTickEvents.END_CLIENT_TICK.register(e -> execute());
    ClientCommandManager.DISPATCHER.register(
            ClientCommandManager.literal("align").executes(c -> {
              this.execute();
              return 1;
            })
    );
  }

  public void execute() {
    ClientTickEvents.END_CLIENT_TICK.register(e ->
    {
      if (alignPlayer.wasPressed()) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        assert player != null;
        player.sendMessage(new TranslatableText("msg.orientation.align"), true);

        assert e.player != null;
        double yaw = e.player.getHeadYaw();

        System.out.printf("Yaw %s adjusts to %s which rounds to %s%n", yaw, normalizeHeadYaw(yaw),
                roundYaw(normalizeHeadYaw(yaw)));

        yaw = roundYaw(normalizeHeadYaw(yaw));

        e.player.refreshPositionAndAngles(e.player.getX(), e.player.getY(), e.player.getZ(), (float) yaw, e.player.getPitch(0));
      }
    });
  }
}
