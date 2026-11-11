package com.nutria2.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/** Dibuja al jugador como un creeper. */
public class CreeperRender {
	private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper.png");
	private static CreeperModel<Entity> model;

	public static void render(AbstractClientPlayer p, float partialTick, PoseStack pose, MultiBufferSource buffers, int light) {
		if (model == null) {
			model = new CreeperModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.CREEPER));
		}
		pose.pushPose();
		float bodyYaw = Mth.rotLerp(partialTick, p.yBodyRotO, p.yBodyRot);
		float headYaw = Mth.rotLerp(partialTick, p.yHeadRotO, p.yHeadRot);
		float pitch = Mth.lerp(partialTick, p.xRotO, p.getXRot());
		pose.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
		pose.scale(-1.0F, -1.0F, 1.0F);
		pose.translate(0.0F, -1.501F, 0.0F);
		float speed = Math.min(1.0F, p.walkAnimation.speed(partialTick));
		float pos = p.walkAnimation.position(partialTick);
		model.setupAnim(p, pos, speed, p.tickCount + partialTick, headYaw - bodyYaw, pitch);
		VertexConsumer vc = buffers.getBuffer(model.renderType(TEXTURE));
		model.renderToBuffer(pose, vc, light, OverlayTexture.NO_OVERLAY, -1);
		pose.popPose();
	}
}
