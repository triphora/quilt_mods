package coffee.waffle.wakeuptime;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.TranslatableText;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;

public class WakeUpTime implements ClientModInitializer {
  private static final String CATEGORY = "key.categories.wakeUpTime";
  private static final MinecraftClient client = MinecraftClient.getInstance();

  @Override
  public void onInitializeClient() {
    KeyBinding sendStatusToActionBar = new KeyBinding("key.wakeUpTime.sendStatus", GLFW_KEY_V, CATEGORY);
    KeyBindingHelper.registerKeyBinding(sendStatusToActionBar);

    ClientTickEvents.END_CLIENT_TICK.register(e -> {
      while (sendStatusToActionBar.wasPressed()) sendStatusToActionBar();
    });
  }

  private void sendStatusToActionBar() {
    if (client.world == null || client.player == null) return;
    client.player.sendMessage(new TranslatableText("key.wakeUpTime.status", getStage()), true);
  }

  private TranslatableText getStage() {
    @SuppressWarnings("ConstantConditions") long time = client.world.getTimeOfDay();
    while (time > 24000) time = time - 24000;

    if (time < 9000 && time >= 2000) {
      return new TranslatableText("key.wakeUpTime.working", "§a" + (9000 - time) / 20);
    }

    TranslatableText status = new TranslatableText("key.wakeUpTime.wandering");
    if (time >= 12000 || time < 10) status = new TranslatableText("key.wakeUpTime.sleeping");
    if (time >= 9000 && time < 11000) status = new TranslatableText("key.wakeUpTime.gathering");

    final long timeUntilWork = time > 9000 ? 26000 - time : 2000 - time;

    return new TranslatableText("key.wakeUpTime.lazyBums", "§a" + status.getString(), "§a" + timeUntilWork / 20);
  }
}
