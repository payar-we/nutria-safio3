package com.nutria2;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Clic derecho: sonido de creeper (agachado: sonido de muerte de creeper). */
public class CreeperSoundItem extends Item {
	public CreeperSoundItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			level.playSound(null, player.getX(), player.getY(), player.getZ(),
					player.isShiftKeyDown() ? SoundEvents.CREEPER_DEATH : SoundEvents.CREEPER_PRIMED,
					SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		player.getCooldowns().addCooldown(this, 20);
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}
