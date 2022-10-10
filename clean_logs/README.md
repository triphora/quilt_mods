**Clean Logs** will filter out logging based on user-defined options, either by a phrase method or a regex method.

This code is based upon that of [Shut Up Console](https://curseforge.com/projects/396776). While the mod works on 1.17+, I wanted to add a new feature. Also, it had seemed to be abandoned, with the source URL being simply gone. Thus, here's Clean Logs!

The central idea of Clean Logs is to be able to filter things out from your logs based on the config. You can edit the config at `config/clean-logs/config.toml`. Make sure your [TOML syntax is correct](https://www.toml-lint.com/) before saving - if it's not, it might get wiped!

The added feature is the ability to turn off printing the lines in the log to filter out. This was my one complaint with Shut Up Console; it filters out the specified lines, but at the same time, it prints out the line to be filtered out. There's a new config option to disable this printing (it's still enabled by default, though).

Want to add this to your development environment? Check out the [Modrinth Maven](https://docs.modrinth.com/docs/tutorials/maven/).
