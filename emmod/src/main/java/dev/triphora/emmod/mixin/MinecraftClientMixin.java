package dev.triphora.emmod.mixin;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Code from auoeke's noauth, licensed Unlicense
@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {
	@Inject(method = "createUserApiService", at = @At("HEAD"), cancellable = true)
	void emmod$offlineUserInDevEnv(YggdrasilAuthenticationService authService, RunArgs runArgs, CallbackInfoReturnable<UserApiService> cir) {
		if (QuiltLoader.isDevelopmentEnvironment()) cir.setReturnValue(UserApiService.OFFLINE);
	}
}
