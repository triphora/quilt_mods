package dev.triphora.gradle;

import com.google.gson.Gson;
import org.gradle.api.Project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

interface Generator {
	void create(Project project) throws IOException;

	static void writeFinalResult(Project project, String filename, Object o) throws IOException {
		Path output = project.getBuildDir().toPath().resolve("generated").resolve("resources");

		//noinspection ResultOfMethodCallIgnored
		output.toFile().mkdirs();
		output = output.resolve(filename);
		if (Files.exists(output)) Files.delete(output);

		try (var writer = new FileWriter(output.toFile())) {
			new Gson().toJson(o, writer);
		}
	}

	static ConfigurationExtension getExtension(Project project) {
		return project.getExtensions().getByType(ConfigurationExtension.class);
	}
}
