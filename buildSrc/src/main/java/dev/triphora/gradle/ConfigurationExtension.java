package dev.triphora.gradle;

import lombok.Getter;
import org.gradle.api.Project;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;

public class ConfigurationExtension {
	@Getter private final Property<Boolean> midnightlib, qsl, mixin;
	@Getter private final Property<String> summary, spdx, modrinthSlug, mixinPlugin;
	@Getter private final Property<Side> side;
	@Getter private final Property<Integer> javaVersion;
	@Getter private final SetProperty<String> gameVersions;
	@Getter private final SetProperty<QuiltModJson.QuiltLoader.Dependency> depends;
	@Getter private final MapProperty<String, String> contributors, entrypoints, modMenuLinks;

	public ConfigurationExtension(Project project) {
		midnightlib = project.getObjects().property(Boolean.class).convention(false);
		qsl = project.getObjects().property(Boolean.class).convention(false);
		mixin = project.getObjects().property(Boolean.class).convention(true);
		summary = project.getObjects().property(String.class);
		spdx = project.getObjects().property(String.class).convention("Zlib");
		modrinthSlug = project.getObjects().property(String.class).convention(project.getName());
		mixinPlugin = project.getObjects().property(String.class).convention((String) null);
		side = project.getObjects().property(Side.class).convention(Side.ANY);
		javaVersion = project.getObjects().property(Integer.class).convention(17);
		gameVersions = project.getObjects().setProperty(String.class).empty();
		depends = project.getObjects().setProperty(QuiltModJson.QuiltLoader.Dependency.class).empty();
		contributors = project.getObjects().mapProperty(String.class, String.class).empty();
		entrypoints = project.getObjects().mapProperty(String.class, String.class).empty();
		modMenuLinks = project.getObjects().mapProperty(String.class, String.class).empty();
	}
}
