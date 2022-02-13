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

import static io.github.coolcrabs.brachyura.fabric.FabricProject.ModDependencyFlag.COMPILE;
import static io.github.coolcrabs.brachyura.fabric.FabricProject.ModDependencyFlag.RUNTIME;

@SuppressWarnings("unused")
public class Buildscript extends FabricProject {
  private static final String MODRINTH_MAVEN = "https://api.modrinth.com/maven";

  @Override
  public String getMcVersion() {
    return "1.18.1";
  }

  @Override
  public MappingTree createMappings() {
    return Yarn.ofMaven(FabricMaven.URL, FabricMaven.yarn("1.18.1+build.22")).tree;
  }

  @Override
  public FabricLoader getLoader() {
    return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.13.2"));
  }

  @Override
  public @Nullable BrachyuraDecompiler decompiler() {
    // Uses QuiltFlower instead of CFR
    return new FernflowerDecompiler(Maven.getMavenJarDep(
            "https://maven.quiltmc.org/repository/release",
            new MavenId("org.quiltmc:quiltflower:1.7.0")));
  }

  @Override
  public int getJavaVersion() {
    return 17;
  }

  @Override
  public void getModDependencies(ModDependencyCollector d) {
    String[][] fapiModules = new String[][]{
            {"api-base", "0.4.2+d7c144a8f4"},
            {"command-api-v1", "1.1.7+d7c144a8f4"},
            {"lifecycle-events-v1", "1.4.13+713c266865"},
            {"key-binding-api-v1", "1.0.10+54e5b2ecf4"}
    };
    String[][] runtimeFapiModules = new String[][] {
            {"rendering-data-attachment-v1", "0.3.5+d7c144a8f4"},
            {"rendering-fluids-v1", "0.1.19+3ac43d9565"},
            {"resource-loader-v0", "0.4.14+713c266865"},
            {"screen-api-v1", "1.0.8+d7c144a8f4"}
    };
    for (String[] module : fapiModules) {
      d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api:fabric-" + module[0] + ':' + module[1]), RUNTIME, COMPILE);
    }
    for (String[] module : runtimeFapiModules) {
      d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api:fabric-" + module[0] + ':' + module[1]), RUNTIME);
    }
    d.addMaven("https://maven.terraformersmc.com/releases", new MavenId("com.terraformersmc:modmenu:3.0.1"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:sodium:mc1.18.1-0.4.0-alpha6"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:lithium:mc1.18.1-0.7.7"), RUNTIME);
    d.addMaven(MODRINTH_MAVEN, new MavenId("maven.modrinth:lazydfu:0.1.2"), RUNTIME);
    d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("org.joml:joml:1.10.2"), RUNTIME);
  }
}
