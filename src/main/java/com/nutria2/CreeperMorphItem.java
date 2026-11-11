package com.nutria2;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Clic derecho: te vuelves creeper. Otro clic derecho: vuelves a la normalidad. */
public class CreeperMorphItem extends Item {
	public CreeperMorphItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide && player instanceof ServerPlayer sp) {
			boolean now = Nutria2.toggleCreeper(sp);
			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CREEPER_PRIMED,
					SoundSource.PLAYERS, 1.0F, now ? 1.0F : 1.5F);
			sp.displayClientMessage(Component.translatable(now ? "message.nutria2.creeper_on" : "message.nutria2.creeper_off"), true);
		}
		player.getCooldowns().addCooldown(this, 20);
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}
