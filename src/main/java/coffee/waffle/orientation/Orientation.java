/*
 * Copyright (c) 2020 Bert Shuler
 * Copyright (c) 2021 wafflecoffee
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package coffee.waffle.orientation;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSLASH;

public class Orientation implements ClientModInitializer {
  private static KeyBinding alignPlayer;

  private static double roundYaw(double yaw) {
    yaw = yaw % 360;
    if (yaw > 180 || yaw < -180) {
      double mod = yaw % 180;
      if (mod > 0) yaw = -180 + mod;
      else if (mod < 0) yaw = 180 + mod;
    }

    if (yaw >= 0 && yaw < 22.5) yaw = 0;
    if (yaw >= 22.5 && yaw < 67.5) yaw = 45;
    if (yaw >= 67.5 && yaw < 112.5) yaw = 90;
    if (yaw >= 112.5 && yaw < 157.5) yaw = 135;
    if (yaw >= 157.5 && yaw <= 180) yaw = 180;
    if (yaw <= 0 && yaw > -22.5) yaw = 0;
    if (yaw <= -22.5 && yaw > -67.5) yaw = -45;
    if (yaw <= -67.5 && yaw > -112.5) yaw = -90;
    if (yaw <= -112.5 && yaw > -157.5) yaw = -135;
    if (yaw <= -157.5 && yaw >= -180) yaw = 180;

    return yaw;
  }

  @Override
  public void onInitializeClient() {
    final String category = "key.categories.orientation";
    alignPlayer = new KeyBinding("key.orientation.align", GLFW_KEY_BACKSLASH, category);
    KeyBindingHelper.registerKeyBinding(alignPlayer);
    checkBeforeAlign();

    ClientCommandManager.DISPATCHER.register(
            ClientCommandManager.literal("align").executes(c -> {
              this.align();
              return 1;
            })
    );
  }

  private void align() {
    ClientPlayerEntity player = requireNonNull(MinecraftClient.getInstance().player);

    player.sendMessage(new TranslatableText("msg.orientation.align"), true);

    double yaw = player.getHeadYaw();

    LogManager.getLogger("orientation")
            .printf(Level.DEBUG, "Yaw %s rounds to %s%n", yaw, roundYaw(yaw));

    yaw = roundYaw(yaw);

    player.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), (float) yaw, player.getPitch(0));
  }

  private void checkBeforeAlign() {
    ClientTickEvents.END_CLIENT_TICK.register(e -> {
      if (alignPlayer.wasPressed()) { align(); }
    });
  }
}
