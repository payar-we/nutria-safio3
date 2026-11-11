package com.nutria2;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NutriaSnowballItem extends Item {
	private final boolean admin;

	public NutriaSnowballItem(boolean admin, Properties properties) {
		super(properties);
		this.admin = admin;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		// La bola de admin solo la puede lanzar quien tenga permisos de operador
		if (admin && !level.isClientSide) {
			if (!(player instanceof ServerPlayer sp) || !sp.createCommandSourceStack().hasPermission(2)) {
				player.displayClientMessage(Component.translatable("message.nutria2.admin_only"), true);
				return InteractionResultHolder.fail(stack);
			}
		}

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW,
				SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!level.isClientSide) {
			NutriaSnowballEntity ball = new NutriaSnowballEntity(level, player);
			ball.setItem(stack);
			ball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, admin ? 2.5F : 1.5F, 1.0F);
			level.addFreshEntity(ball);
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		if (!admin) {
			stack.consume(1, player);
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}
