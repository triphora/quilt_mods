import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.FabricProject;
import io.github.coolcrabs.brachyura.fabric.Yarn;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import net.fabricmc.mappingio.tree.MappingTree;
import org.jetbrains.annotations.Nullable;

import static io.github.coolcrabs.brachyura.fabric.FabricProject.ModDependencyFlag.*;

@SuppressWarnings("unused")
public class Buildscript extends FabricProject {
  private static final String QUILT_MAVEN = "https://maven.quiltmc.org/repository/release";
  private static final String MODRINTH_MAVEN = "https://api.modrinth.com/maven";
  private static final String TERRAFORMERS_MAVEN = "https://maven.terraformersmc.com/releases";

  @Override
  public String getMcVersion() {
    return "1.18.2";
  }

  @Override
  public MappingTree createMappings() {
    return Yarn.ofMaven(FabricMaven.URL, FabricMaven.yarn("1.18.2+build.2")).tree;
  }

  @Override
  public FabricLoader getLoader() {
    return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.13.3"));
  }

  @Override
  public @Nullable BrachyuraDecompiler decompiler() {
    return new FernflowerDecompiler(Maven.getMavenJarDep(QUILT_MAVEN, new MavenId("org.quiltmc:quiltflower:1.7.0")));
  }

  @Override
  public void getModDependencies(ModDependencyCollector d) {
    // https://maven.fabricmc.net/net/fabricmc/fabric-api/
    String[][] fapiModules = new String[][] {
            {"api-base", "0.4.3+d7c144a8d2"},
            {"key-binding-api-v1", "1.0.11+54e5b2ecd2"},
            {"lifecycle-events-v1", "2.0.1+25407454d2"},
    };
    String[][] runtimeFapiModules = new String[][] {
            {"rendering-data-attachment-v1", "0.3.6+d7c144a8d2"},
            {"rendering-fluids-v1", "2.0.1+54e5b2ecd2"},
            {"resource-loader-v0", "0.4.17+801ec85b60"},
            {"screen-api-v1", "1.0.9+d882b915d2"},
    };
    for (String[] module : fapiModules) d.addMaven(FabricMaven.URL,
            new MavenId(FabricMaven.GROUP_ID + ".fabric-api:fabric-" + module[0] + ':' + module[1]), RUNTIME, COMPILE);
    for (String[] module : runtimeFapiModules) d.addMaven(FabricMaven.URL,
            new MavenId(FabricMaven.GROUP_ID + ".fabric-api:fabric-" + module[0] + ':' + module[1]), RUNTIME);

    d.addMaven(TERRAFORMERS_MAVEN, new MavenId("com.terraformersmc:modmenu:3.0.1"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:sodium:mc1.18.2-0.4.1"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:lithium:mc1.18.2-0.7.9"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:starlight:1.0.2+1.18.2"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:lazydfu:0.1.2"), RUNTIME);
    d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("org.joml:joml:1.10.2"), RUNTIME);
  }
}
