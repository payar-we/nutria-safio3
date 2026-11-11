package com.nutria2;

import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CreeperPayload(UUID player, boolean creeper) implements CustomPacketPayload {
	public static final Type<CreeperPayload> TYPE = new Type<>(Nutria2.id("creeper"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CreeperPayload> CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, CreeperPayload::player,
			ByteBufCodecs.BOOL, CreeperPayload::creeper,
			CreeperPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
