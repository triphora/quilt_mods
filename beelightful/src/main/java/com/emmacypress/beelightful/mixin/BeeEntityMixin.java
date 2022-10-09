package com.emmacypress.beelightful.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.UuidUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeeEntity.class)
public class BeeEntityMixin {
	private static final String DESCRIPTOR = "Lnet/minecraft/entity/LivingEntity;addStatusEffect" +
		"(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z";

	@Redirect(method = "tryAttack", at = @At(value = "INVOKE", target = DESCRIPTOR))
	private boolean beelightful$modifyBeeSting(LivingEntity target, StatusEffectInstance effect, Entity source) {
		int difficulty = effect.getDuration() / 20;
		boolean normalMode = difficulty == 10;
		boolean hardMode = difficulty == 18;
		int healthPoints = normalMode ? 2 : hardMode ? 3 : 0;
		var bee = (BeeEntity) (Object) this;

		switch (getBeeStingEffect(target)) {
			case 0, 1 -> {} // No poison! Congratulations.
			case 2 -> { // Shorter than default plus health gets returned (if you haven't yet died)
				effect.upgrade(new StatusEffectInstance(StatusEffects.POISON, difficulty * 10, 0));
				target.addStatusEffect(effect, bee);
				target.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, healthPoints), bee);
			}
			case 3 -> { // Shorter than default
				effect.upgrade(new StatusEffectInstance(StatusEffects.POISON, difficulty * 10, 0));
				target.addStatusEffect(effect, bee);
			}
			case 4, 5 -> target.addStatusEffect(effect, bee); // Same as default
			case 6 -> { // Longer than default
				effect.upgrade(new StatusEffectInstance(StatusEffects.POISON, difficulty * 100, 0));
				target.addStatusEffect(effect, bee);
			}
			case 7 -> { // Longer than default plus double the damage
				effect.upgrade(new StatusEffectInstance(StatusEffects.POISON, difficulty * 200, 0));
				target.addStatusEffect(effect, bee);
				target.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, healthPoints), bee);
			}
			case 8, 9 -> target.damage(DamageSource.sting(bee), Float.MAX_VALUE); // Sorry.
		}
		return false;
	}

	private int getBeeStingEffect(LivingEntity target) {
		int chance = target.getWorld() instanceof ServerWorld serverWorld ? (int) (serverWorld.getSeed() % 10) : 0;
		for (var entry : UuidUtil.toIntArray(target.getUuid())) chance += entry;
		return Math.abs(chance % 10);
	}
}
