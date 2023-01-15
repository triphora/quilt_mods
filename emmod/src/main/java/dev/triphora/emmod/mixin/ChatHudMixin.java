package dev.triphora.emmod.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatHud.class)
abstract class ChatHudMixin {
	@Inject(method = "getChatScale", at = @At("HEAD"), cancellable = true)
	void emmod$changeChatScale(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(1.5D);
	}

	@Inject(method = "getWidth*", at = @At("HEAD"), cancellable = true)
	void emmod$changeWidth(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(480);
	}

	@Inject(method = "getHeight*", at = @At("HEAD"), cancellable = true)
	void emmod$changeHeight(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(240);
	}
}
