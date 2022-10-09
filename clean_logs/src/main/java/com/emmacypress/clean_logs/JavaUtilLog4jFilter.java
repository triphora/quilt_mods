package com.emmacypress.clean_logs;

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
