package com.nutria2;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
	public static final EntityType<NutriaSnowballEntity> SNOWBALL = EntityType.Builder
			.<NutriaSnowballEntity>of(NutriaSnowballEntity::new, MobCategory.MISC)
			.sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10)
			.build("nutria_snowball");

	public static void register() {
		Registry.register(BuiltInRegistries.ENTITY_TYPE, Nutria2.id("nutria_snowball"), SNOWBALL);
	}
}
