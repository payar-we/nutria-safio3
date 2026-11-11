package com.nutria2.client;

import com.nutria2.CreeperPayload;
import com.nutria2.ModEntities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class Nutria2Client implements ClientModInitializer {
	/** true = el usuario quito el menu con Q + M (dura hasta cerrar el juego). */
	public static boolean menuDisabled = false;

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.SNOWBALL, ThrownItemRenderer::new);
		ClientPlayNetworking.registerGlobalReceiver(CreeperPayload.TYPE,
				(payload, context) -> ClientCreepers.set(payload.player(), payload.creeper()));
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientCreepers.clear());
	}
}
