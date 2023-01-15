package dev.triphora.gradle;

import org.gradle.api.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MixinsJsonGenerator implements Generator {
	public void create(Project project) throws IOException {
		var extension = Generator.getExtension(project);

		if (!extension.getMixin().get()) return;

		final var mixinPackage = "dev.triphora." + project.getName() + ".mixin";

		List<String> mixins = new ArrayList<>();
		List<String> client = new ArrayList<>();
		List<String> server = new ArrayList<>();

		var mixinFolder = Paths.get(
			project.getProjectDir() + "/src/main/java/" + mixinPackage.replaceAll("\\.", "/")
		);
		try (var mixinPaths = Files.list(mixinFolder)) {
			mixinPaths.filter(Files::isRegularFile).forEach(path -> {
				var mixinName = path.getFileName().toString().replace(".java", "");
				switch (extension.getSide().get()) {
					case CLIENT -> client.add(mixinName);
					case DEDICATED_SERVER -> server.add(mixinName);
					case ANY -> mixins.add(mixinName);
				}
			});
		}
		try (var clientMixinPaths = Files.list(mixinFolder.resolve("client"))) {
			clientMixinPaths.filter(Files::isRegularFile).forEach(path ->
				client.add(path.getFileName().toString().replace(".java", "")));
		} catch (Exception e) {
			// ignore
		}
		try (var serverMixinPaths = Files.list(mixinFolder.resolve("server"))) {
			serverMixinPaths.filter(Files::isRegularFile).forEach(path ->
				server.add(path.getFileName().toString().replace(".java", "")));
		} catch (Exception e) {
			// ignore
		}

		Generator.writeFinalResult(
			project,
			project.getName() + ".mixins.json",
			new MixinsJson(mixinPackage, mixins, client, server)
		);
	}
}
