package com.emmacypress.only_pink_sheeps.mixin;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.random.RandomGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SheepEntity.class)
public class SheepEntityMixin {
	/**
	 * @author Emmaffle
	 * @reason Always make sheep pink!
	 */
	@Overwrite
	public static DyeColor generateDefaultColor(RandomGenerator random) {
		return DyeColor.PINK;
	}
}
