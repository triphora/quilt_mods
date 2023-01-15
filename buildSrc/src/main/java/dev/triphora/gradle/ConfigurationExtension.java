package dev.triphora.gradle;

import lombok.Getter;
import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConfigurationExtension {
	@Getter private final Property<Boolean> midnightlib, qsl, mixin;
	@Getter private final Property<String> summary, spdx, modrinthSlug;
	@Getter private final Property<Side> side;
	@Getter private final Property<Integer> javaVersion;
	@Getter private final ListProperty<String> gameVersions;
	@Getter private final ListProperty<QuiltModJson.QuiltLoader.Dependency> depends;
	@Getter private final MapProperty<String, String> contributors, entrypoints, modMenuLinks;

	public ConfigurationExtension(Project project) {
		midnightlib = project.getObjects().property(Boolean.class).convention(false);
		qsl = project.getObjects().property(Boolean.class).convention(false);
		mixin = project.getObjects().property(Boolean.class).convention(true);
		summary = project.getObjects().property(String.class);
		spdx = project.getObjects().property(String.class).convention("Zlib");
		modrinthSlug = project.getObjects().property(String.class).convention(project.getName());
		side = project.getObjects().property(Side.class).convention(Side.ANY);
		javaVersion = project.getObjects().property(Integer.class).convention(17);
		gameVersions = project.getObjects().listProperty(String.class).empty();
		depends = project.getObjects().listProperty(QuiltModJson.QuiltLoader.Dependency.class).empty();
		contributors = project.getObjects().mapProperty(String.class, String.class).empty();
		entrypoints = project.getObjects().mapProperty(String.class, String.class).empty();
		modMenuLinks = project.getObjects().mapProperty(String.class, String.class).empty();
	}
}
