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

import org.quiltmc.config.api.values.TrackedValue;

import java.util.List;

// Util class for config shenanigans
@SuppressWarnings("unchecked")
final class ConfigHelper {
  static final boolean PRINT_ON_START = val("printOnStart");
  static final boolean PRINT_INIT_LINE = val("printInitLineOnStart");

  private static boolean val(String token) {
    return ((TrackedValue<Boolean>) CleanLogs.CONFIG.getValue(List.of(token))).value();
  }
}
