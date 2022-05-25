package coffee.waffle.wakeuptime;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.text.TranslatableText;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;

public class WakeUpTime implements ClientModInitializer {
  private static final String CATEGORY = "key.categories.wakeUpTime";
  private static final MinecraftClient client = MinecraftClient.getInstance();
  private boolean pressed = false;

  @Override
  public void onInitializeClient(ModContainer mod) {
    MidnightConfig.init(mod.metadata().id(), Config.class);

    KeyBind sendStatusToActionBar = new KeyBind("key.wakeUpTime.sendStatus", GLFW_KEY_V, CATEGORY);
    KeyBindingHelper.registerKeyBinding(sendStatusToActionBar);

    ClientTickEvents.END.register(e -> {
      if (sendStatusToActionBar.wasPressed()) {
        pressed = !pressed;
        sendStatusToActionBar();
      }
      if (Config.persistent && pressed) sendStatusToActionBar();
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

  public static class Config extends MidnightConfig {
    @Entry public static boolean persistent = false;
  }
}
