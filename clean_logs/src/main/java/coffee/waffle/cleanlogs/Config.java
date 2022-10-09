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

import org.quiltmc.config.api.WrappedConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.values.ValueList;

@SuppressWarnings("unused")
public final class Config extends WrappedConfig {
  @Comment("Print out all phrases and regex to be filtered out on startup")
  public final boolean printOnStart = true;
  @Comment("Print a single line on start saying that Clean Logs will filter from this point forward")
  public final boolean printInitLineOnStart = false;
  @Comment("If a log message has one of these phrases, it will be filtered out from logging")
  public final ValueList<String> phrases = ValueList.create("");
  @Comment("If a log message matches one of these regex patterns, it will be filtered out from logging")
  public final ValueList<String> regex = ValueList.create("");
}
