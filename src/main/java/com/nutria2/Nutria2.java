package com.nutria2;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class Nutria2 implements ModInitializer {
	public static final String MOD_ID = "nutria2";
	private static final Set<UUID> CREEPERS = ConcurrentHashMap.newKeySet();

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModEntities.register();
		ModItems.register();
		PayloadTypeRegistry.playS2C().register(CreeperPayload.TYPE, CreeperPayload.CODEC);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			for (UUID u : CREEPERS) {
				if (ServerPlayNetworking.canSend(handler.getPlayer(), CreeperPayload.TYPE)) {
					ServerPlayNetworking.send(handler.getPlayer(), new CreeperPayload(u, true));
				}
			}
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> setCreeper(handler.getPlayer(), false));
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> setCreeper(newPlayer, false));
	}

	/** Cambia el estado y devuelve true si ahora es creeper. */
	public static boolean toggleCreeper(ServerPlayer p) {
		boolean now = !CREEPERS.contains(p.getUUID());
		setCreeper(p, now);
		return now;
	}

	private static void setCreeper(ServerPlayer p, boolean on) {
		boolean changed = on ? CREEPERS.add(p.getUUID()) : CREEPERS.remove(p.getUUID());
		if (!changed) return;
		MinecraftServer server = p.getServer();
		if (server == null) return;
		for (ServerPlayer other : server.getPlayerList().getPlayers()) {
			if (other != p || on) {
				if (ServerPlayNetworking.canSend(other, CreeperPayload.TYPE)) {
					ServerPlayNetworking.send(other, new CreeperPayload(p.getUUID(), on));
				}
			}
		}
	}
}
