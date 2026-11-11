package com.nutria2.client;

import java.io.InputStream;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.NativeImage;
import com.nutria2.Nutria2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

/** Menu principal estilo Dedsafio 4: video de fondo, logo y botones JUGAR / OPCIONES / SALIR. */
public class NutriaTitleScreen extends Screen {
	private static final int VIDEO_W = 480, VIDEO_H = 270, FRAMES = 240;
	// los 240 cuadros estan agrupados en 15 hojas (s00..s14.png) de 4x4 cuadros, para tener pocos archivos
	private static final int COLS = 4, PER_SHEET = 16;
	private static final int SHEET_W = VIDEO_W * COLS, SHEET_H = VIDEO_H * (PER_SHEET / COLS);
	private static final long FRAME_NANOS = 80_000_000L; // 12.5 fps, igual que el video

	private static final ResourceLocation VIDEO_TEX = Nutria2.id("menu_video");
	private static final ResourceLocation LOGO = Nutria2.id("textures/gui/logo.png");
	private static final ResourceLocation[] BTN = {
			Nutria2.id("textures/gui/jugar.png"),
			Nutria2.id("textures/gui/opciones.png"),
			Nutria2.id("textures/gui/salir.png") };
	// tamano real de cada imagen de boton (ancho, alto)
	private static final int[][] BTN_SIZE = { { 313, 56 }, { 449, 56 }, { 262, 56 } };
	// posicion vertical (fraccion de la pantalla), igual que la captura
	private static final double[] BTN_Y = { 0.519, 0.603, 0.686 };

	private DynamicTexture video;
	private int loadedSheet = -1;
	private int currentFrame = 0;
	private long startNanos;
	private final int[][] rects = new int[3][4]; // x, y, w, h

	public NutriaTitleScreen() {
		super(Component.empty());
	}

	@Override
	protected void init() {
		startNanos = System.nanoTime();
		loadedSheet = -1;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float delta) {
		// sin fondo de vanilla
	}

	private void updateVideo() {
		currentFrame = (int) ((System.nanoTime() - startNanos) / FRAME_NANOS % FRAMES);
		int sheet = currentFrame / PER_SHEET;
		if (sheet == loadedSheet) return;
		loadedSheet = sheet; // aunque falle, no se reintenta en cada cuadro
		try {
			ResourceLocation loc = Nutria2.id(String.format("video/s%02d.png", sheet));
			var res = this.minecraft.getResourceManager().getResource(loc);
			if (res.isEmpty()) return;
			try (InputStream in = res.get().open()) {
				DynamicTexture tex = new DynamicTexture(NativeImage.read(NativeImage.Format.RGBA, in));
				tex.setFilter(true, false);
				// al registrar con el mismo nombre, la hoja anterior se libera sola
				this.minecraft.getTextureManager().register(VIDEO_TEX, tex);
				video = tex;
			}
		} catch (Throwable ignored) {
			// si falla una hoja, se queda la anterior
		}
	}

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
		g.fill(0, 0, width, height, 0xFF0B0714);
		// video de fondo (cubre toda la pantalla)
		updateVideo();
		if (video != null) {
			int idx = currentFrame % PER_SHEET;
			float u = (idx % COLS) * VIDEO_W, v = (idx / COLS) * VIDEO_H;
			double scale = Math.max((double) width / VIDEO_W, (double) height / VIDEO_H);
			int w = (int) Math.ceil(VIDEO_W * scale), h = (int) Math.ceil(VIDEO_H * scale);
			g.blit(VIDEO_TEX, (width - w) / 2, (height - h) / 2, w, h, u, v, VIDEO_W, VIDEO_H, SHEET_W, SHEET_H);
		}
		// logo
		int lw = (int) (width * 0.335), lh = (int) (lw * 360.0 / 680.0);
		g.blit(LOGO, (int) (width * 0.322) - lw / 2, (int) (height * 0.22) - lh / 2, lw, lh, 0, 0, 680, 360, 680, 360);
		// botones
		int bh = Math.max(12, (int) (height * 0.034));
		for (int i = 0; i < 3; i++) {
			int bw = bh * BTN_SIZE[i][0] / BTN_SIZE[i][1];
			int cx = (int) (width * 0.322), cy = (int) (height * BTN_Y[i]);
			boolean hover = mouseX >= cx - bw / 2 - 6 && mouseX <= cx + bw / 2 + 6 && mouseY >= cy - bh / 2 - 6 && mouseY <= cy + bh / 2 + 6;
			int dw = hover ? (int) (bw * 1.15) : bw, dh = hover ? (int) (bh * 1.15) : bh;
			rects[i] = new int[] { cx - bw / 2 - 6, cy - bh / 2 - 6, bw + 12, bh + 12 };
			g.blit(BTN[i], cx - dw / 2, cy - dh / 2, dw, dh, 0, 0, BTN_SIZE[i][0], BTN_SIZE[i][1], BTN_SIZE[i][0], BTN_SIZE[i][1]);
		}
		String copy = "Copyright Mojang AB. ¡No distribuir!";
		g.drawString(this.font, copy, width - this.font.width(copy) - 2, height - 10, 0xA0FFFFFF);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0) {
			for (int i = 0; i < 3; i++) {
				int[] r = rects[i];
				if (r != null && r[2] > 0 && mx >= r[0] && mx <= r[0] + r[2] && my >= r[1] && my <= r[1] + r[3]) {
					this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
					switch (i) {
						case 0 -> this.minecraft.setScreen(new SelectWorldScreen(this));
						case 1 -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
						default -> this.minecraft.stop();
					}
					return true;
				}
			}
		}
		return super.mouseClicked(mx, my, button);
	}

	/** Q + M al mismo tiempo: quita este menu y vuelve el de Minecraft normal. */
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		long win = this.minecraft.getWindow().getWindow();
		boolean q = keyCode == GLFW.GLFW_KEY_Q || InputConstants.isKeyDown(win, GLFW.GLFW_KEY_Q);
		boolean m = keyCode == GLFW.GLFW_KEY_M || InputConstants.isKeyDown(win, GLFW.GLFW_KEY_M);
		if (q && m) {
			Nutria2Client.menuDisabled = true;
			this.minecraft.setScreen(new TitleScreen());
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void removed() {
		// se queda la textura registrada para reutilizarla
	}
}
