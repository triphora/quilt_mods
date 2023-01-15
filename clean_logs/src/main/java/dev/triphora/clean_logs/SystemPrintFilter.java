package dev.triphora.clean_logs;

import java.io.PrintStream;

public final class SystemPrintFilter extends PrintStream {
	public SystemPrintFilter(PrintStream stream) {
		super(stream);
	}

	@Override
	public void println(String x) {
		if (!CleanLogs.shouldFilterMessage(x)) super.println(x);
	}

	@Override
	public void print(String s) {
		if (!CleanLogs.shouldFilterMessage(s)) super.print(s);
	}
}
