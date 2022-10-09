package com.emmacypress.repeat_no_more.mixin;

import com.emmacypress.repeat_no_more.RepeatNoMore;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepeaterBlock.class)
public class RepeaterBlockMixin {
	@Shadow
	@Final
	public static IntProperty DELAY;

	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if (state.get(DELAY) == 4 && !player.isSneaking() && RepeatNoMore.Config.enableMod) {
			player.sendMessage(Text.translatable("repeat_no_more.prevented"), true);
			cir.setReturnValue(ActionResult.PASS);
		}
	}
}
