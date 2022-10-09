/*
 * This file is part of Clean Logs.
 *
 * Clean Logs is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Clean Logs is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with Clean Logs. If not, see <https://www.gnu.org/licenses/>.
 */

package coffee.waffle.cleanlogs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.config.QuiltConfig;
import org.quiltmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static coffee.waffle.cleanlogs.ConfigHelper.*;

public class CleanLogs implements PreLaunchEntrypoint {
  private static final String MODID = "clean-logs";
  public static final Logger LOGGER = LogManager.getLogger(MODID);
  public static final Config CONFIG = QuiltConfig.create("clean-logs", "config", Config.class);
  public static final JavaUtilLog4jFilter FILTER = new JavaUtilLog4jFilter();

  @Override
  public void onPreLaunch(ModContainer mod) {
    migrate();

    if (PRINT_INIT_LINE) LOGGER.info("=== Clean Logs will filter from this point on. ===");

    if (PRINT_ON_START && !CONFIG.phrases.isEmpty()) {
      LOGGER.info("=== Messages containing the following phrases will be filtered out: ===");
      for (var entry : CONFIG.phrases) LOGGER.info(entry);
    }

    if (PRINT_ON_START && !CONFIG.regex.isEmpty()) {
      LOGGER.info("=== Messages matching the regex patterns will be filtered out: ===");
      for (var entry : CONFIG.regex) LOGGER.info(entry);
    }

    System.setOut(new SystemPrintFilter(System.out));
    java.util.logging.Logger.getLogger("").setFilter(FILTER);
    ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(FILTER);
    var foundOffshootLog4jLoggers = new ArrayList<>();
    var logContext = (LoggerContext) LogManager.getContext(false);
    var map = logContext.getConfiguration().getLoggers();

    for (var logger : map.values()) {
      if (!foundOffshootLog4jLoggers.contains(logger)) {
        logger.addFilter(FILTER);
        foundOffshootLog4jLoggers.add(logger);
      }
    }
  }

  public static boolean shouldFilterMessage(String message) {
    var stringIterator = CONFIG.phrases.iterator();
    String phrase;

    var regexIterator = CONFIG.regex.iterator();
    String regex;

    do {
      if (!stringIterator.hasNext()) {
        do {
          if (!regexIterator.hasNext()) return false;
          regex = regexIterator.next();
        } while (!message.matches(regex));
        return true;
      }
      phrase = stringIterator.next();
    } while (!message.contains(phrase));
    return true;
  }

  private static void migrate() {
    var newConfigLocation = new File(QuiltLoader.getConfigDir() + "/clean-logs/config.toml");

    var shutUpConsoleConfig = new File(QuiltLoader.getConfigDir() + "/shutupconsole.toml");
    if (shutUpConsoleConfig.exists()) {
      LOGGER.warn("You still have an old Shut Up Console config!");
      LOGGER.warn("Migrating it to a Clean Logs config...");
      try {
        var path = Paths.get(shutUpConsoleConfig.getPath());
        var content = Files.readString(path);
        content = content.replaceAll("Shut Up Console", "Clean Logs");
        content = content.replaceAll("\\[shutupconsole]", """
# Print out all phrases and regex to be filtered out on startup
# default: true
printOnStart = true

# Print a single line on start saying that Clean Logs will filter from this point forward
# default: false
printInitLineOnStart = false
                """);
        Files.writeString(path, content);
        //noinspection ResultOfMethodCallIgnored
        path.toFile().renameTo(newConfigLocation);
        LOGGER.info("Successfully migrated Shut Up Console config to Clean Logs config.");
        LOGGER.info("You may need to restart the game for it to take effect.");
      } catch (IOException e) {
        LOGGER.error("Migration of Shut Up Console config to Clean Logs config failed.", e);
      }
    }

    var oldCleanLogsConfig = new File(QuiltLoader.getConfigDir() + "/clean-logs.toml");
    if (oldCleanLogsConfig.exists()) {
      try {
        LOGGER.warn("You still have an old Clean Logs (pre-1.2.0) config!");
        LOGGER.warn("Migrating it to a new Clean Logs config...");
        var path = Paths.get(oldCleanLogsConfig.getPath());
        var content = Files.readString(path);
        content = content.replaceAll("\\[clean-logs]", "");
        Files.writeString(path, content);
        //noinspection ResultOfMethodCallIgnored
        path.toFile().renameTo(newConfigLocation);
        LOGGER.info("Successfully migrated old Clean Logs config to new Clean Logs config.");
        LOGGER.info("You may need to restart the game for it to take effect.");
      } catch (IOException e) {
        LOGGER.error("Migration of old Clean Log config to new Clean Logs config failed.", e);
      }
    }
  }
}
