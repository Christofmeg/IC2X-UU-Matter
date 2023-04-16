package com.christofmeg.ic2xuumatter.integration.jei.category;

import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import ic2.core.uu.UuIndex;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableBuilder;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PatternStorageCategory implements IRecipeCategory<PatternStorageCategory.PatternStorageRecipe> {

    public static String UID = "ic2xuumatter.pattern_storage";

    IDrawable background;
    IDrawableBuilder patternStorage;
    IDrawableBuilder patternStorageInfobox;
    IDrawableBuilder patternStorageArrows;
    IDrawableBuilder patternStorageSlot;

    public static final ResourceLocation patternStorageTexture = new ResourceLocation("ic2",
            "textures/gui/guipatternstorage.png");
    public static final ResourceLocation modGuiArrows = new ResourceLocation("ic2xuumatter", "textures/gui/arrows.png");

    public PatternStorageCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(160, 64);
        patternStorageInfobox = helper.drawableBuilder(patternStorageTexture, 7, 45, 162, 36);
        patternStorageArrows = helper.drawableBuilder(modGuiArrows, 0, 0, 36, 25).setTextureSize(36, 25);
        patternStorageSlot = helper.drawableBuilder(patternStorageTexture, 151, 28, 18, 18);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return BlockName.te.getItemStack(TeBlock.pattern_storage).getDisplayName();
    }

    @Override
    public String getModName() {
        return "ic2xuumatter";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        patternStorageInfobox.build().draw(minecraft, 0, 29);
        patternStorageArrows.build().draw(minecraft, 1, 2);
        patternStorageSlot.build().draw(minecraft, 10, 0);
        patternStorageSlot.build().draw(minecraft, 144, 9);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PatternStorageRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 10, 0); // crystalMemory input
        guiItemStacks.init(1, false, 144, 9); // replicationOutput

        guiItemStacks.set(1, recipeWrapper.replicationOutput); // replicationOutput
        guiItemStacks.set(ingredients);
    }

    public static final class PatternStorageRecipe implements IRecipeWrapper {

        ItemStack crystalMemoryInput;
        ItemStack replicationOutput;

        public PatternStorageRecipe(ItemStack crystalMemoryInput, ItemStack replicationOutput) {
            this.crystalMemoryInput = crystalMemoryInput;
            this.replicationOutput = replicationOutput;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(VanillaTypes.ITEM, crystalMemoryInput);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            FontRenderer font = minecraft.fontRenderer;
            String item = replicationOutput.getDisplayName();
            String EU = I18n.format("ic2.generic.text.EU");
            font.drawString(item, 73, 32, 16777215);

            String name = I18n.format("ic2.generic.text.Name");
            String uuMatter = I18n.format("ic2.generic.text.UUMatte");
            String energy = I18n.format("ic2.generic.text.Energy");

            font.drawString(name, 3, 32, 16777215);
            font.drawString(uuMatter, 3, 43, 16777215);
            font.drawString(energy, 3, 54, 16777215);

            font.drawString(Util.toSiString(UuIndex.instance.getInBuckets(replicationOutput), 2) + "B", 73, 43,
                    16777215);
            font.drawString(Util.toSiString((UuIndex.instance.get(replicationOutput) / 10) * 512, 3) + " " + EU, 73, 54,
                    16777215);
        }
    }

}
