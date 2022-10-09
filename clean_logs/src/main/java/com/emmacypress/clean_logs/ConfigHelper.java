package com.emmacypress.clean_logs;

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
