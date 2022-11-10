package com.emmacypress.gradle;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class QuiltModJson {
	int schema_version;
	QuiltLoader quilt_loader;
	Minecraft minecraft;
	ModMenu modmenu;
	String mixin;

	@AllArgsConstructor
	public static class QuiltLoader {
		String group;
		String id;
		String version;
		Metadata metadata;
		String intermediate_mappings;
		@Nullable Map<String, String> entrypoints;
		List<Dependency> depends;

		@AllArgsConstructor
		static class Metadata {
			String name;
			@Nullable String description;
			Map<String, String> contributors;
			Map<String, String> contact;
			String license;
			String icon;
		}

		@AllArgsConstructor
		public static class Dependency {
			String id;
			String versions;
			@Nullable String reason;
			@Nullable String environment;
			boolean optional;

			public Dependency(String id) {
				this.id = id;
			}

			public Dependency(String id, String versions) {
				this.id = id;
				this.versions = versions;
			}

			public Dependency(String id, String versions, @Nullable String reason) {
				this.id = id;
				this.versions = versions;
				this.reason = reason;
			}
		}
	}

	@AllArgsConstructor
	static class Minecraft {
		String environment;
	}

	@AllArgsConstructor
	static class ModMenu {
		Map<String, String> links;
	}
}
