package com.nutria2;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ModItems {
	public static final Item NUTRIA_SNOWBALL = new NutriaSnowballItem(false, new Item.Properties().stacksTo(16));
	public static final Item ADMIN_SNOWBALL = new NutriaSnowballItem(true, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
	public static final Item CREEPER_SOUND = new CreeperSoundItem(new Item.Properties().stacksTo(1));
	public static final Item CREEPER_MORPH = new CreeperMorphItem(new Item.Properties().stacksTo(1));

	public static final CreativeModeTab NUTRIA_TAB = FabricItemGroup.builder()
			.title(Component.translatable("itemGroup.nutria2.nutria"))
			.icon(() -> new ItemStack(NUTRIA_SNOWBALL))
			.displayItems((params, out) -> {
				out.accept(NUTRIA_SNOWBALL);
				out.accept(ADMIN_SNOWBALL);
				out.accept(CREEPER_SOUND);
				out.accept(CREEPER_MORPH);
			})
			.build();

	public static void register() {
		Registry.register(BuiltInRegistries.ITEM, Nutria2.id("nutria_snowball"), NUTRIA_SNOWBALL);
		Registry.register(BuiltInRegistries.ITEM, Nutria2.id("admin_snowball"), ADMIN_SNOWBALL);
		Registry.register(BuiltInRegistries.ITEM, Nutria2.id("creeper_sound"), CREEPER_SOUND);
		Registry.register(BuiltInRegistries.ITEM, Nutria2.id("creeper_morph"), CREEPER_MORPH);
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Nutria2.id("nutria"), NUTRIA_TAB);
	}
}
