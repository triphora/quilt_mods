package dev.triphora.gradle;

public enum Side {
	ANY("*"),
	CLIENT("client"),
	DEDICATED_SERVER("dedicated_server");

	private final String string;

	Side(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}
}
