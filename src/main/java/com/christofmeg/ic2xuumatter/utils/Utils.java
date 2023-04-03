package com.christofmeg.ic2xuumatter.utils;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import ic2.core.ref.ItemName;
import mezz.jei.api.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class Utils {

    @Nullable
    private static Utils instance;

    public static Utils getDrawables(IGuiHelper guiHelper) {

        if (instance == null) {
            instance = new Utils(guiHelper);
        }
        return instance;
    }

    private Utils(IGuiHelper guiHelper) {

    }

    /**
     * https://github.com/CoFH/CoFHCore-1.12-Legacy/blob/3119c11b853a04a5ff8fa76b97199291f6a40699/src/main/java/cofh/core/util/helpers/RenderHelper.java#L26
     */
    public static final ResourceLocation MC_BLOCK_SHEET = new ResourceLocation("textures/atlas/blocks.png");
    public static final int TANK = 0;

    /**
     * https://github.com/ZelGimi/industrialupgrade/blob/5dbe976428ef0a6c0f7b9eefa9eaea80ea9d4880/src/main/java/com/denfop/utils/ModUtils.java#L65-L73
     */
    public static ItemStack getCellFromFluid(String name) {
        final ItemStack cell = ItemName.fluid_cell.getItemStack().copy();
        final NBTTagCompound nbt = nbt(cell);
        final NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setString("FluidName", name);
        nbt1.setInteger("Amount", 1000);
        nbt.setTag("Fluid", nbt1);
        return cell;
    }

    /**
     * https://github.com/ZelGimi/industrialupgrade/blob/5dbe976428ef0a6c0f7b9eefa9eaea80ea9d4880/src/main/java/com/denfop/utils/ModUtils.java#L386-L396
     */
    public static NBTTagCompound nbt(ItemStack stack) {
        if (stack.isEmpty()) {
            return new NBTTagCompound();
        }
        NBTTagCompound NBTTagCompound = stack.getTagCompound();
        if (NBTTagCompound == null) {
            NBTTagCompound = new NBTTagCompound();
        }
        stack.setTagCompound(NBTTagCompound);
        return NBTTagCompound;
    }

    /**
     * https://github.com/CoFH/ThermalExpansion-1.12-Legacy/blob/92b52710c19f3923f81ece2f580b044d8d8111fc/src/main/java/cofh/thermalexpansion/plugins/jei/JEIPluginTE.java#L141-L155
     */
    public static void drawFluid(int x, int y, FluidStack fluid, int width, int height) {

        if (fluid == null) {
            return;
        }
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft.getMinecraft().renderEngine.bindTexture(MC_BLOCK_SHEET);
        int color = fluid.getFluid().getColor(fluid);
        setGLColorFromInt(color);
        drawTiledTexture(x, y, Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite(fluid.getFluid().getStill(fluid).toString()), width, height);
        GL11.glPopMatrix();
    }

    /**
     * https://github.com/CoFH/ThermalExpansion-1.12-Legacy/blob/92b52710c19f3923f81ece2f580b044d8d8111fc/src/main/java/cofh/thermalexpansion/plugins/jei/JEIPluginTE.java#L157-L173
     */
    public static void drawTiledTexture(int x, int y, TextureAtlasSprite icon, int width, int height) {

        int i;
        int j;

        int drawHeight;
        int drawWidth;

        for (i = 0; i < width; i += 16) {
            for (j = 0; j < height; j += 16) {
                drawWidth = Math.min(width - i, 16);
                drawHeight = Math.min(height - j, 16);
                drawScaledTexturedModelRectFromIcon(x + i, y + j, icon, drawWidth, drawHeight);
            }
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * https://github.com/CoFH/ThermalExpansion-1.12-Legacy/blob/92b52710c19f3923f81ece2f580b044d8d8111fc/src/main/java/cofh/thermalexpansion/plugins/jei/JEIPluginTE.java#L175-L192
     */
    public static void drawScaledTexturedModelRectFromIcon(int x, int y, TextureAtlasSprite icon, int width,
            int height) {

        if (icon == null) {
            return;
        }
        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0).tex(minU, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y + height, 0)
                .tex(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y, 0).tex(minU + (maxU - minU) * width / 16F, minV).endVertex();
        buffer.pos(x, y, 0).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }

    /**
     * https://github.com/CoFH/CoFHCore-1.12-Legacy/blob/3119c11b853a04a5ff8fa76b97199291f6a40699/src/main/java/cofh/core/util/helpers/RenderHelper.java#L55-L61
     */
    public static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        GlStateManager.color(red, green, blue, 1.0F);
    }

}
