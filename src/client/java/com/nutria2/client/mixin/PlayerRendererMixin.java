package com.nutria2.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nutria2.client.ClientCreepers;
import com.nutria2.client.CreeperRender;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
	@Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At("HEAD"), cancellable = true)
	private void nutria2$creeper(AbstractClientPlayer player, float yaw, float partialTick, PoseStack pose,
			MultiBufferSource buffers, int light, CallbackInfo ci) {
		if (ClientCreepers.is(player.getUUID())) {
			CreeperRender.render(player, partialTick, pose, buffers, light);
			ci.cancel();
		}
	}

	@Inject(method = "renderRightHand", at = @At("HEAD"), cancellable = true)
	private void nutria2$hideRight(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player, CallbackInfo ci) {
		if (ClientCreepers.is(player.getUUID())) ci.cancel();
	}

	@Inject(method = "renderLeftHand", at = @At("HEAD"), cancellable = true)
	private void nutria2$hideLeft(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player, CallbackInfo ci) {
		if (ClientCreepers.is(player.getUUID())) ci.cancel();
	}
}
