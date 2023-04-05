package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.List;

import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IDrawableBuilder;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
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
import net.minecraftforge.fluids.FluidStack;

public class ReplicatorCategory implements IRecipeCategory<ReplicatorCategory.ReplicatorRecipe> {

    public static String UID = "ic2xuumatter.replicator";

    IDrawable background;
    IDrawable energy;
    IDrawableBuilder patternStorage;
    protected IDrawableStatic tankOverlay;

    IDrawableBuilder patternGuiBackground1;
    IDrawableBuilder patternGuiBackground2;
    IDrawableBuilder patternGuiBackground3;
    IDrawableBuilder patternGuiBackground4;
    IDrawableBuilder patternGuiBackground5;
    IDrawableBuilder patternGuiBackground6;

    IDrawableBuilder patternGuiCorner;
    IDrawableBuilder patternGuiBorderFix;
    IDrawableBuilder patternGuiCornerPixel;

    IDrawableBuilder patternGuiRecipeBackground1;
    IDrawableBuilder patternGuiRecipeBackground2;
    IDrawableBuilder patternGuiRecipeBackground3;
    IDrawableBuilder patternGuiRecipeBackground4;
    IDrawableBuilder patternGuiRecipeBackground5;
    IDrawableBuilder patternGuiRecipeBackground6;

    public static final ResourceLocation replicatorTexture = new ResourceLocation("ic2",
            "textures/gui/guireplicator.png");
    public static final ResourceLocation patternStorageTexture = new ResourceLocation("ic2",
            "textures/gui/guipatternstorage.png");
    public static final ResourceLocation jeiGuiBackground = new ResourceLocation("jei",
            "textures/gui/gui_background.png");
    public static final ResourceLocation jeiCatalystTab = new ResourceLocation("jei", "textures/gui/catalyst_tab.png");
    public static final ResourceLocation jeiRecipeBackground = new ResourceLocation("jei",
            "textures/gui/single_recipe_background.png");

    public ReplicatorCategory(IGuiHelper helper) {
        background = helper.createDrawable(replicatorTexture, 7, 7, 162, 93);
        energy = helper.drawableBuilder(replicatorTexture, 176, 0, 14, 15).buildAnimated(300, StartDirection.TOP, true);
        tankOverlay = helper.createDrawable(replicatorTexture, 48 + 64 * 2, 193, 16, 60);
        patternStorage = helper.drawableBuilder(patternStorageTexture, 7, 19, 162, 62);

        patternGuiBackground1 = helper.drawableBuilder(jeiGuiBackground, 0, 28, 60, 36).setTextureSize(64, 64);
        patternGuiBackground2 = helper.drawableBuilder(jeiGuiBackground, 0, 0, 60, 45).setTextureSize(64, 64);

        patternGuiBackground3 = helper.drawableBuilder(jeiGuiBackground, 4, 28, 56, 36).setTextureSize(64, 64);
        patternGuiBackground4 = helper.drawableBuilder(jeiGuiBackground, 4, 0, 56, 45).setTextureSize(64, 64);

        patternGuiBackground5 = helper.drawableBuilder(jeiGuiBackground, 4, 28, 11, 36).setTextureSize(64, 64);
        patternGuiBackground6 = helper.drawableBuilder(jeiGuiBackground, 4, 0, 11, 45).setTextureSize(64, 64);

        patternGuiCorner = helper.drawableBuilder(jeiCatalystTab, 22, 25, 3, 3).setTextureSize(28, 28);
        patternGuiBorderFix = helper.drawableBuilder(jeiCatalystTab, 0, 4, 3, 1).setTextureSize(28, 28);
        patternGuiCornerPixel = helper.drawableBuilder(jeiCatalystTab, 2, 25, 1, 1).setTextureSize(28, 28);

        patternGuiRecipeBackground1 = helper.drawableBuilder(jeiRecipeBackground, 0, 56, 62, 8).setTextureSize(64, 64);
        patternGuiRecipeBackground2 = helper.drawableBuilder(jeiRecipeBackground, 0, 0, 62, 62).setTextureSize(64, 64);

        patternGuiRecipeBackground3 = helper.drawableBuilder(jeiRecipeBackground, 2, 56, 60, 8).setTextureSize(64, 64);
        patternGuiRecipeBackground4 = helper.drawableBuilder(jeiRecipeBackground, 2, 0, 60, 62).setTextureSize(64, 64);

        patternGuiRecipeBackground5 = helper.drawableBuilder(jeiRecipeBackground, 16, 56, 48, 8).setTextureSize(64, 64);
        patternGuiRecipeBackground6 = helper.drawableBuilder(jeiRecipeBackground, 16, 0, 48, 62).setTextureSize(64, 64);

    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return BlockName.te.getItemStack(TeBlock.replicator).getDisplayName();
    }

    @Override
    public String getModName() {
        return "ic2xuumatter";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    /*
     * TODO Cystal disk ItemSubtypes for every possible Scanner result
     *
     * JEI page Scanner JEI page Pattern Storage JEI page Pattern Storage +
     * Replicator
     */

    @Override
    public void drawExtras(Minecraft minecraft) {
        energy.draw(minecraft, 126, 76);
        /*
         * patternGuiBackground1.build().draw(minecraft, -194, 35);
         * patternGuiBackground2.build().draw(minecraft, -194, -9);
         *
         * patternGuiBackground3.build().draw(minecraft, -138, 35);
         * patternGuiBackground4.build().draw(minecraft, -138, -9);
         *
         * patternGuiBackground3.build().draw(minecraft, -82, 35);
         * patternGuiBackground4.build().draw(minecraft, -82, -9);
         *
         * patternGuiBackground5.build().draw(minecraft, -26, 35);
         * patternGuiBackground6.build().draw(minecraft, -26, -9);
         *
         * patternGuiCorner.build().draw(minecraft, -18, 68);
         * patternGuiBorderFix.build().draw(minecraft, -18, -9);
         * patternGuiCornerPixel.build().draw(minecraft, -16, -7);
         *
         * patternGuiRecipeBackground1.build().draw(minecraft, -189, 58);
         * patternGuiRecipeBackground2.build().draw(minecraft, -189, -4);
         *
         * patternGuiRecipeBackground3.build().draw(minecraft, -127, 58);
         * patternGuiRecipeBackground4.build().draw(minecraft, -127, -4);
         *
         * patternGuiRecipeBackground5.build().draw(minecraft, -67, 58);
         * patternGuiRecipeBackground6.build().draw(minecraft, -67, -4);
         *
         * patternStorage.build().draw(minecraft, -185, 0);
         */
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ReplicatorRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        List<List<FluidStack>> fluidOutput = ingredients.getOutputs(VanillaTypes.FLUID);

        guiFluidStacks.init(0, false, 24, 27, 12, 47, 16000, true, tankOverlay); // uu matter

        guiItemStacks.init(0, false, 0, 64); // empty cell
        guiItemStacks.init(1, true, 0, 19); // filled cell
        guiFluidStacks.set(0, fluidOutput.get(0)); // uu matter

        guiItemStacks.set(ingredients);
    }

    public static final class ReplicatorRecipe implements IRecipeWrapper {

        public ItemStack filledCellInput;
        public ItemStack emptyCellOutput;
        public FluidStack fluidOutput;

        public ReplicatorRecipe(ItemStack filledCellInput, ItemStack emptyCellOutput, FluidStack fluidOutput) {
            this.filledCellInput = filledCellInput;
            this.emptyCellOutput = emptyCellOutput;
            this.fluidOutput = fluidOutput;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(VanillaTypes.ITEM, filledCellInput);
            ingredients.setOutput(VanillaTypes.ITEM, emptyCellOutput);
            ingredients.setOutput(VanillaTypes.FLUID, fluidOutput);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            FontRenderer font = minecraft.fontRenderer;
            String tierHV = I18n.format("translation.ic2xuumatter.tier.4");
            font.drawString(tierHV, 0, 0, 4210752);
        }
    }

}
