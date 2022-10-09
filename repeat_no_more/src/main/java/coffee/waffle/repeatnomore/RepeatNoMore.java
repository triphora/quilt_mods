package coffee.waffle.repeatnomore;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;

public class RepeatNoMore implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    MidnightConfig.init("repeat-no-more", Config.class);
  }

  public static class Config extends MidnightConfig {
    @Entry public static boolean enableMod = true;
  }
}
