package org.teacon.xibao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
    modid = "xibao",
    useMetadata = true,
    clientSideOnly = true,
    acceptedMinecraftVersions = "[1.8,1.8.9]",
    acceptableRemoteVersions = "*"
)
public class Xibao {
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new Xibao.XibaoImpl());
    }

    public static final class XibaoImpl {
        private static final ResourceLocation LOCATION = new ResourceLocation("xibao", "textures/xibao.png");
        @SubscribeEvent
        public void on(GuiScreenEvent.InitGuiEvent.Post event) {
            boolean showXibao = !Files.exists(event.gui.mc.mcDataDir.toPath().resolve(".xibao_stop"));
            if (showXibao && event.gui instanceof GuiDisconnected) {
                GuiDisconnected s = (GuiDisconnected) event.gui;
                GuiButton disableXibao = new GuiButton(200, s.width / 2 - 75, s.height - 30, 150, 20, I18n.format("xibao.do_not_show_again")) {
                    @Override
                    public void mouseReleased(int mouseX, int mouseY) {
                        if (this.mousePressed(s.mc, mouseX, mouseY)) {
                            Path gameDir = s.mc.mcDataDir.toPath();
                            try {
                                Files.write(gameDir.resolve(".xibao_stop"), Lists.newArrayList("Remove this file to show Xibao again"), StandardCharsets.UTF_8);
                            } catch (IOException e) {

                            }
                        }
                    }
                };
                event.buttonList.add(disableXibao);
            }
        }

        @SubscribeEvent
        public void on(GuiScreenEvent.BackgroundDrawnEvent event) {
            boolean showXibao = !Files.exists(event.gui.mc.mcDataDir.toPath().resolve(".xibao_stop"));
            if (showXibao && event.gui instanceof GuiDisconnected) {
                GuiDisconnected s = (GuiDisconnected) event.gui;
                s.mc.getTextureManager().bindTexture(LOCATION);
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(0.0D, s.height, 0.0D).tex(0F, 1F).color(255, 255, 255, 255).endVertex();
                worldrenderer.pos(s.width, s.height, 0.0D).tex(1F, 1F).color(255, 255, 255, 255).endVertex();
                worldrenderer.pos(s.width, 0.0D, 0.0D).tex(1F, 0F).color(255, 255, 255, 255).endVertex();
                worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0F, 0F).color(255, 255, 255, 255).endVertex();
                tessellator.draw();
            }
        }
    }
}
