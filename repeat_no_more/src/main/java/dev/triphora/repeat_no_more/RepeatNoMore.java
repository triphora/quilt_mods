package dev.triphora.repeat_no_more;

import eu.midnightdust.lib.config.MidnightConfig;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class RepeatNoMore implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		MidnightConfig.init(mod.metadata().id(), Config.class);
	}

	public static class Config extends MidnightConfig {
		@Entry public static boolean enableMod = true;
	}
}
