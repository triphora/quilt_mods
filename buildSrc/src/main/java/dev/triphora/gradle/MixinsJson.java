package dev.triphora.gradle;

import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class MixinsJson {
	boolean required = true;
	String minVersion = "0.8";
	@Nonnull @SerializedName("package") String mixinPackage;
	String compatibilityLevel = "JAVA_17";
	@Nonnull List<String> mixins;
	@Nonnull List<String> client;
	@Nonnull List<String> server;
	Map<String, Integer> injectors = Map.of("defaultRequire", 1);
}
