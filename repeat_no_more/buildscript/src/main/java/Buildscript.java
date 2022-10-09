import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.FabricProject;
import io.github.coolcrabs.brachyura.fabric.Yarn;
import io.github.coolcrabs.brachyura.maven.MavenId;
import net.fabricmc.mappingio.tree.MappingTree;

@SuppressWarnings("unused")
public class Buildscript extends FabricProject {
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
  public void getModDependencies(ModDependencyCollector d) {
    d.addMaven("https://maven.terraformersmc.com/releases",
            new MavenId("com.terraformersmc:modmenu:3.0.1"),
            ModDependencyFlag.RUNTIME);
    d.addMaven("https://api.modrinth.com/maven",
            new MavenId("maven.modrinth:midnightlib:0.3.1"),
            ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE, ModDependencyFlag.JIJ);

    // This mod doesn't actually use any FAPI modules, but the above dependencies do
    d.addMaven(FabricMaven.URL,
            new MavenId("net.fabricmc.fabric-api:fabric-api-base:0.4.3+d7c144a8bc"),
            ModDependencyFlag.RUNTIME);
    d.addMaven(FabricMaven.URL,
            new MavenId("net.fabricmc.fabric-api:fabric-lifecycle-events-v1:1.4.9+3ac43d95cb"),
            ModDependencyFlag.RUNTIME);
    d.addMaven(FabricMaven.URL,
            new MavenId("net.fabricmc.fabric-api:fabric-renderer-registries-v1:3.2.9+b4f4f6cdf4"),
            ModDependencyFlag.RUNTIME);
    d.addMaven(FabricMaven.URL,
            new MavenId("net.fabricmc.fabric-api:fabric-rendering-v1:1.9.0+c683a655d1"),
            ModDependencyFlag.RUNTIME);
    d.addMaven(FabricMaven.URL,
            new MavenId("net.fabricmc.fabric-api:fabric-resource-loader-v0:0.4.9+65d505fcd1"),
            ModDependencyFlag.RUNTIME);
    d.addMaven(FabricMaven.URL,
            new MavenId("net.fabricmc.fabric-api:fabric-screen-api-v1:1.0.8+d7c144a8f4"),
            ModDependencyFlag.RUNTIME);
  }
}
