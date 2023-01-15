package dev.triphora.clean_logs;

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
