package coffee.waffle.wakeuptime;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;

public class WakeUpTime implements ClientModInitializer, ClientTickEvents.End {
  private static final String CATEGORY = "key.categories.wakeUpTime";
  private static final MinecraftClient client = MinecraftClient.getInstance();
  private boolean pressed = false;
  private static final KeyBind sendStatusToActionBar;

  static {
    sendStatusToActionBar = new KeyBind("key.wakeUpTime.sendStatus", GLFW_KEY_V, CATEGORY);
  }

  @Override
  public void onInitializeClient(ModContainer mod) {
    MidnightConfig.init(mod.metadata().id(), Config.class);
    KeyBindingHelper.registerKeyBinding(sendStatusToActionBar);
  }

  @Override
  public void endClientTick(MinecraftClient client) {
    if (sendStatusToActionBar.wasPressed()) {
      pressed = !pressed;
      sendStatusToActionBar();
    }
    if (Config.persistent && pressed) sendStatusToActionBar();
  }

  private void sendStatusToActionBar() {
    if (client.world == null || client.player == null) return;
    client.player.sendMessage(Text.translatable("key.wakeUpTime.status", getStage()), true);
  }

  private MutableText getStage() {
    @SuppressWarnings("ConstantConditions") long time = client.world.getTimeOfDay();
    while (time > 24000) time = time - 24000;

    if (time < 9000 && time >= 2000) {
      return Text.translatable("key.wakeUpTime.working", "§a" + (9000 - time) / 20);
    }

    MutableText status = Text.translatable("key.wakeUpTime.wandering");
    if (time >= 12000 || time < 10) status = Text.translatable("key.wakeUpTime.sleeping");
    if (time >= 9000 && time < 11000) status = Text.translatable("key.wakeUpTime.gathering");

    final long timeUntilWork = time > 9000 ? 26000 - time : 2000 - time;

    return Text.translatable("key.wakeUpTime.lazyBums", "§a" + status.getString(), "§a" + timeUntilWork / 20);
  }

  public static class Config extends MidnightConfig {
    @Entry public static boolean persistent = false;
  }
}
