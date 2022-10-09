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

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public final class JavaUtilLog4jFilter extends AbstractFilter implements Filter {
  public boolean isLoggable(@NotNull LogRecord record) {
    return !CleanLogs.shouldFilterMessage(record.getMessage());
  }

  public Result filter(@NotNull LogEvent event) {
    return CleanLogs.shouldFilterMessage("[" + event.getLoggerName() + "]: " + event.getMessage().getFormattedMessage()) ? Result.DENY : Result.NEUTRAL;
  }
}
