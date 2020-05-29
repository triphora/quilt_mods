package net.critical.orientation;

import net.fabricmc.api.ModInitializer;

public class Orientation implements ModInitializer {

    @Override
    public void onInitialize() {
        /*
          This code runs as soon as Minecraft is in a mod-load-ready state.
          However, some things (like resources) may still be unitialized.
          Proceed with mild caution.
         */
        new MyKeyBind();
        System.out.println("Critical Orientation Mod started.");
    }
}
