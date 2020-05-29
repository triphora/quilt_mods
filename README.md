# Critical Orientation (Minecraft 1.15.x)

Available to download from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/critical-orientation).

Built using [Fabric Example Mod Template](https://github.com/FabricMC/fabric-example-mod) and made with the [Fabric](https://fabricmc.net) modding toolchain for Minecraft.

A Minecraft Mod that enables the user to perfectly place ice boats, by snapping the player facing direction to their closest cardinal point:

- **HUD Features:**
    - Snap to closest N NW W SW S SE E NE

---

## Installation

- Install [Fabric Loader](https://fabricmc.net/use/) on your Minecraft client
    - Recommended to install with the [MultiMC](https://multimc.org/) Minecraft client, which allows you to install Fabric in one click in the Minecraft instance settings
- Download latest Mod `.jar` from [Github](https://github.com/bshuler/critical-orientation/releases/latest) or from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/critical-orientation)
- Put the downloaded Mod `.jar` in the `.minecraft/mods` folder
    - Or if you're using MultiMC, open the Minecraft instance settings you're using, and look for the option to add a Mod, then select the `.jar` file you downloaded
- Done!

---

## Building from source

- Clone the project with `git clone https://github.com/bshuler/critical-orientation.git`
- Cd into the project's directory `cd critical-orientation`
- Run `./gradlew build` to build the `.jar`
- Built Mod `.jar` files will be located at `build/libs`
    - Example: `build/libs/critical-orientation-1.0.0.jar`
    - This will be the Mod `.jar` file you can put in your `.minecraft/mods` folder

---

## Planned features

- No additional

---

## FAQ

- **Does this Mod work on versions below 1.15?**
    - No, it *might* work on 1.14 with some changes, but not anything below 1.14, since this Mod is made with Fabric, which only supports Minecraft 1.14 and above.
