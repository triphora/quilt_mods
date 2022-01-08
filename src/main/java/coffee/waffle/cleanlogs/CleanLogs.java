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

import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CleanLogs implements PreLaunchEntrypoint {
  private static final String MODID = "clean-logs";
  public static final Logger LOGGER = LogManager.getLogger(MODID);
  public static final Toml CONFIG = getConfig();
  public static final JavaUtilLog4jFilter FILTER = new JavaUtilLog4jFilter();

  @Override
  public void onPreLaunch() {
    boolean shouldPrintOnStart = CONFIG.getBoolean(MODID + ".printOnStart");
    boolean shouldPrintSingleLine = CONFIG.getBoolean(MODID + ".printInitLineOnStart");

    List<String> phraseFilter = CONFIG.getList(MODID + ".phrases");

    if (shouldPrintSingleLine) LOGGER.info("=== Clean Logs will filter from this point on. ===");

    if (shouldPrintOnStart && !phraseFilter.isEmpty()) {
      LOGGER.info("=== Messages containing the following phrases will be filtered out: ===");
      for (String entry : phraseFilter) LOGGER.info(entry);
    }

    List<String> regexFilter = CONFIG.getList(MODID + ".regex");
    if (shouldPrintOnStart && !regexFilter.isEmpty()) {
      LOGGER.info("=== Messages matching the regex patterns will be filtered out: ===");
      for (String entry : regexFilter) LOGGER.info(entry);
    }

    System.setOut(new SystemPrintFilter(System.out));
    java.util.logging.Logger.getLogger("").setFilter(FILTER);
    ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(FILTER);
    ArrayList<LoggerConfig> foundOffshootLog4jLoggers = new ArrayList<>();
    LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
    Map<String, LoggerConfig> map = logContext.getConfiguration().getLoggers();

    for (LoggerConfig logger : map.values()) {
      if (!foundOffshootLog4jLoggers.contains(logger)) {
        logger.addFilter(FILTER);
        foundOffshootLog4jLoggers.add(logger);
      }
    }
  }

  public static boolean shouldFilterMessage(String message) {
    List<String> phraseFilter = CONFIG.getList(MODID + ".phrases");
    Iterator<String> stringIterator = phraseFilter.iterator();
    String phrase;

    List<String> regexFilter = CONFIG.getList(MODID + ".regex");
    Iterator<String> regexIterator = regexFilter.iterator();
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

  private static Toml getConfig() {
    File oldConfig = new File(FabricLoader.getInstance().getConfigDir() + "/shutupconsole.toml");
    File config = new File(FabricLoader.getInstance().getConfigDir() + "/clean-logs.toml");
    if (oldConfig.exists()) {
      LOGGER.info("You still have an old Shut Up Console config!");
      LOGGER.info("Please rename it to `clean-logs.toml` and replace `[shutupconsole]` with `[clean-logs]`.");
      LOGGER.info("If you want to utilise the new printOnStart feature, also add the following line to clean-logs.toml under the [clean-logs] heading:");
      LOGGER.info("`printOnStart = false`");
    }

    if (!config.exists()) {
      try {
        Files.copy(Objects.requireNonNull(CleanLogs.class.getResourceAsStream("/assets/clean-logs/config.toml")), config.toPath());
      } catch (IOException e) {
        LOGGER.error("An error occurred when creating a new config", e);
      }
    }
    return new Toml().read(config);
  }
}
