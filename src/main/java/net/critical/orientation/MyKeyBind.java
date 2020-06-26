package net.critical.orientation;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

public class MyKeyBind {
    private static String categoryName = "Critical Orientation";
    private static FabricKeyBinding keyBinding = FabricKeyBinding.Builder.create(
            new Identifier("toggle", "orientation"),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_BACKSLASH,
            categoryName
    ).build();;
//    private boolean pressedLastTick = false;

    public MyKeyBind() {

        KeyBindingRegistry.INSTANCE.addCategory(categoryName);
        KeyBindingRegistry.INSTANCE.register(keyBinding);


        ClientTickCallback.EVENT.register(e ->
        {
                if (keyBinding.wasPressed())
                {
//                    for(double x = -600; x < 600; x = x + 10) {
//                        System.out.print(" x : " + x );
//                        System.out.print(" x : " + normalizeHeadYaw(x) );
//                        System.out.print(" rounded : " + roundYaw(normalizeHeadYaw(x)));
//                        System.out.print("\n");
//                    }



                    double yaw = e.player.getHeadYaw() ;

                    test(yaw);

                    yaw = roundYaw(normalizeHeadYaw(yaw));

                    e.player.refreshPositionAndAngles(e.player.getX(), e.player.getY(), e.player.getZ(), (float)yaw, e.player.getPitch(0));
                    }
        });

    }

    public static double normalizeHeadYaw(double yaw) {
        yaw = yaw % 360;
        if (yaw > 180 || yaw < -180) {
            double mod = yaw % 180;
            if (mod > 0) {
                yaw = -180 + mod;
            } else if (mod < 0){
                yaw = 180 + mod;
            }
        }
        return yaw;
    }

    public static double roundYaw(double yaw) {
        if(yaw >=0 && yaw < 22.5) { yaw = 0; }
        if(yaw >=22.5 && yaw < 67.5) { yaw = 45; }
        if(yaw >=67.5 && yaw < 112.5) { yaw = 90; }
        if(yaw >=112.5 && yaw < 157.5) { yaw = 135; }
        if(yaw >=157.5 && yaw <= 180) { yaw = 180; }

        if(yaw <=0 && yaw > -22.5) { yaw = 0; }
        if(yaw <=-22.5 && yaw > -67.5) { yaw = -45; }
        if(yaw <=-67.5 && yaw > -112.5) { yaw = -90; }
        if(yaw <=-112.5 && yaw > -157.5) { yaw = -135; }
        if(yaw <=-157.5 && yaw >= -180) { yaw = 180; }

        return yaw;
    }

    public static void test(double yaw) {
        String s = String.format("Yaw %s adjusts to %s rounds to %s", yaw, normalizeHeadYaw(yaw),
                roundYaw(normalizeHeadYaw(yaw)));
        System.out.println(s);
    }

}
