package com.nutria2.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.nutria2.client.Nutria2Client;
import com.nutria2.client.NutriaTitleScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientLevel;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	public ClientLevel level;

	/** Cambia el menu principal de vanilla por el de Dedsafio 4. */
	@ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
	private Screen nutria2$replaceTitle(Screen screen) {
		if (Nutria2Client.menuDisabled) return screen;
		if (screen != null && screen.getClass() == TitleScreen.class) {
			return new NutriaTitleScreen();
		}
		if (screen == null && this.level == null) {
			return new NutriaTitleScreen();
		}
		return screen;
	}
}
