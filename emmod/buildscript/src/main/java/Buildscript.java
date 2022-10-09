import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.Yarn;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.quilt.QuiltMaven;
import io.github.coolcrabs.brachyura.quilt.SimpleQuiltProject;
import net.fabricmc.mappingio.tree.MappingTree;
import org.jetbrains.annotations.Nullable;

import static io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyFlag.RUNTIME;

@SuppressWarnings("unused")
public class Buildscript extends SimpleQuiltProject {
  // https://fabricmc.net/develop
  private static final String MC_VERSION = "1.19";
  private static final byte YARN_VERSION = 4;
  private static final String LOADER_VERSION = "0.17.0";

  @Override
  public VersionMeta createMcVersion() {
    return Minecraft.getVersion(MC_VERSION);
  }

  @Override
  public MappingTree createMappings() {
    return Yarn.ofMaven(FabricMaven.URL, FabricMaven.yarn(MC_VERSION + "+build." + YARN_VERSION)).tree;
  }

  @Override
  public FabricLoader getLoader() {
    return new FabricLoader(QuiltMaven.URL, QuiltMaven.loader(LOADER_VERSION));
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
    d.addMaven("https://api.modrinth.com/maven", new MavenId("maven.modrinth:lithium:mc1.19-0.8.0"), RUNTIME);
    d.addMaven("https://api.modrinth.com/maven", new MavenId("maven.modrinth:starlight:1.1.0+1.19"), RUNTIME);
    d.addMaven("https://api.modrinth.com/maven", new MavenId("maven.modrinth:lazydfu:0.1.3"), RUNTIME);
  }
}
