import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import io.github.coolcrabs.brachyura.fabric.FabricContext.*;
import io.github.coolcrabs.brachyura.fabric.*;
import io.github.coolcrabs.brachyura.quilt.*;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import net.fabricmc.mappingio.tree.MappingTree;
import org.jetbrains.annotations.Nullable;

import static io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyFlag.*;

@SuppressWarnings("unused")
public class Buildscript extends SimpleQuiltProject {
  private static final String MODRINTH_MAVEN = "https://api.modrinth.com/maven";
  private static final String TERRAFORMERS_MAVEN = "https://maven.terraformersmc.com/releases";

  private static final String MC_VERSION = "1.18.2";
  private static final String QSL_VERSION = "1.1.0-beta.12";
  private static final String QFAPI_VERSION = "1.0.0-beta.13+0.51.1";

  @Override
  public VersionMeta createMcVersion() {
    return Minecraft.getVersion(MC_VERSION);
  }

  @Override
  public MappingTree createMappings() {
    return QuiltMappings.ofMaven(QuiltMaven.URL, QuiltMaven.quiltMappings(MC_VERSION + "+build.24")).toIntermediary(this.context.get().intermediary.get());
  }

  @Override
  public FabricLoader getLoader() {
    return new FabricLoader(QuiltMaven.URL, QuiltMaven.loader("0.16.1"));
  }

  @Override
  public @Nullable BrachyuraDecompiler decompiler() {
    return new FernflowerDecompiler(Maven.getMavenJarDep(QuiltMaven.URL, new MavenId("org.quiltmc:quiltflower:1.8.1")));
  }

  @Override
  public int getJavaVersion() {
    return 17;
  }

  @Override
  public void getModDependencies(ModDependencyCollector d) {
    // https://maven.quiltmc.org/repository/release/org/quiltmc/qsl/
    String[] qslModules = new String[] {
            "core:qsl_base",
            "core:lifecycle_events",
            "core:networking",
    };
    String[] runtimeQslModules = new String[] {
            "core:resource_loader",
            "data:registry_entry_attachments",
            "data:tags",
            "gui:screen",
            "gui:tooltip",
    };
    for (String module : qslModules) d.addMaven(QuiltMaven.URL,
            new MavenId(QuiltMaven.GROUP_ID + ".qsl." + module + ':' + QSL_VERSION + '+' + MC_VERSION), RUNTIME, COMPILE);
    for (String module : runtimeQslModules) d.addMaven(QuiltMaven.URL,
            new MavenId(QuiltMaven.GROUP_ID + ".qsl." + module + ':' + QSL_VERSION + '+' + MC_VERSION), RUNTIME);

    // https://maven.fabricmc.net/net/fabricmc/fabric-api/
    String[] qfapiModules = new String[] {
            "api-base",
            "key-binding-api-v1",
            "lifecycle-events-v1",
    };
    String[] runtimeQfapiModules = new String[] {
            "renderer-registries-v1",
            "rendering-v1",
            "rendering-data-attachment-v1",
            "rendering-fluids-v1",
            "resource-loader-v0",
            "screen-api-v1",
    };
    for (String module : qfapiModules) d.addMaven(QuiltMaven.URL,
            new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api:fabric-" + module + ':' + QFAPI_VERSION + '-' + MC_VERSION), RUNTIME, COMPILE);
    for (String module : runtimeQfapiModules) d.addMaven(QuiltMaven.URL,
            new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api:fabric-" + module + ':' + QFAPI_VERSION + '-' + MC_VERSION), RUNTIME);

    jij(d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:midnightlib:0.4.0"), RUNTIME, COMPILE));

    d.addMaven(TERRAFORMERS_MAVEN, new MavenId("com.terraformersmc:modmenu:3.2.1"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:sodium:mc1.18.2-0.4.1"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:lithium:mc1.18.2-0.7.10"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:starlight:1.0.2+1.18.2"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:lazydfu:0.1.2"), RUNTIME);
    d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("org.joml:joml:1.10.2"), RUNTIME);
  }
}
