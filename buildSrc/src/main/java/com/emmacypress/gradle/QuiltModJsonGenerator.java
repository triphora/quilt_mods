package com.emmacypress.gradle;

import org.apache.commons.text.WordUtils;
import org.gradle.api.Project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.emmacypress.gradle.Constants.*;

public class QuiltModJsonGenerator implements Generator {
	@Override
	public void create(Project project) throws IOException {
		var extension = Generator.getExtension(project);

		final var side = extension.getSide().get();
		Map<String, String> modMenuLinks = new HashMap<>();
		modMenuLinks.put("modmenu.github_sponsors", DONATE_LINK);
		modMenuLinks.put("modmenu.discord", DISCORD_LINK);
		modMenuLinks.put("modmenu.modrinth", "https://modrinth.com/mod/" + extension.getModrinthSlug().get());
		modMenuLinks.putAll(extension.getModMenuLinks().get());

		var qmj = new QuiltModJson(
			1,
			quiltLoader(extension, project),
			new QuiltModJson.Minecraft(side.toString()),
			new QuiltModJson.ModMenu(modMenuLinks),
			extension.getMixin().get() ? project.getName() + ".mixins.json" : null
		);

		Generator.writeFinalResult(project, "quilt.mod.json", qmj);
	}

	private QuiltModJson.QuiltLoader quiltLoader(ConfigurationExtension extension, Project project) {
		var version = project.getVersion().toString();
		if (version.equals("unspecified")) {
			throw new UnsupportedOperationException("Version for " + project.getName() + " is unspecified");
		}

		return new QuiltModJson.QuiltLoader(
			GROUP,
			project.getName(),
			version,
			metadata(extension, project.getName()),
			INTERMEDIATE_MAPPINGS,
			extension.getEntrypoints().getOrNull(),
			depends(extension, project)
		);
	}

	private QuiltModJson.QuiltLoader.Metadata metadata(ConfigurationExtension extension, String projectName) {
		Map<String, String> contributors = new HashMap<>();
		contributors.put("triphora", "Owner");
		contributors.putAll(extension.getContributors().get());

		Map<String, String> contact = new HashMap<>();
		contact.put("email", EMAIL);
		contact.put("homepage", "https://modrinth.com/mod/" + extension.getModrinthSlug().get());
		contact.put("issues", ISSUES_LINK);
		contact.put("sources", SOURCE_LINK);
		contact.put("discord", DISCORD_LINK);
		contact.put("donate", DONATE_LINK);

		return new QuiltModJson.QuiltLoader.Metadata(
			WordUtils.capitalize(projectName.replaceAll("_", " ")),
			extension.getSummary().get(),
			contributors,
			contact,
			extension.getSpdx().get(),
			"assets/" + projectName + "/icon.png"
		);
	}

	private List<QuiltModJson.QuiltLoader.Dependency> depends(ConfigurationExtension extension, Project project) {
		List<QuiltModJson.QuiltLoader.Dependency> list = new ArrayList<>();
		if (extension.getMidnightlib().get()) {
			list.add(new QuiltModJson.QuiltLoader.Dependency(
				"midnightlib",
				">=" + project.getConfigurations().getByName("include")
					.getResolvedConfiguration().getResolvedArtifacts().stream().filter(
						file -> file.getName().equals("midnightlib")
					).iterator().next().getModuleVersion().getId().getVersion(),
				"Configuration library"
			));
			list.add(new QuiltModJson.QuiltLoader.Dependency(
				"modmenu",
				">=" + project.getConfigurations().getByName("modLocalRuntime")
					.getResolvedConfiguration().getResolvedArtifacts().stream().filter(
						file -> file.getName().equals("modmenu")
					).iterator().next().getModuleVersion().getId().getVersion(),
				"To configure the mod",
				"client",
				true
			));
		}
		list.add(new QuiltModJson.QuiltLoader.Dependency("java", ">=" + extension.getJavaVersion().get()));
		list.addAll(extension.getDepends().get());
		return list;
	}
}
