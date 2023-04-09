package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.List;

import javax.annotation.Nullable;

import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import ic2.core.util.ConfigUtil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
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

public class MassFabricatorCategory implements IRecipeCategory<MassFabricatorCategory.MatterFabricatorRecipe> {

    public static String UID = "ic2xuumatter.matter_fabricator";

    private final IDrawable background;
    protected IDrawableStatic tankOverlay;

    public static final ResourceLocation matterFabricatorTexture = new ResourceLocation("ic2",
            "textures/gui/guimatter.png");

    public MassFabricatorCategory(IGuiHelper helper) {
        background = helper.createDrawable(matterFabricatorTexture, 19, 3, 151, 77);
        tankOverlay = helper.createDrawable(matterFabricatorTexture, 48 + 64 * 2, 193, 16, 60);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return BlockName.te.getItemStack(TeBlock.matter_generator).getDisplayName();
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
    public void setRecipe(IRecipeLayout recipeLayout, MatterFabricatorRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        List<List<FluidStack>> fluidOutput = ingredients.getOutputs(VanillaTypes.FLUID);
        List<List<FluidStack>> fluidInput = ingredients.getInputs(VanillaTypes.FLUID);

        guiFluidStacks.init(0, false, 81, 23, 12, 47, 8000, true, tankOverlay); // uu matter

        if (recipeWrapper.scrapInput != null) {
            guiItemStacks.init(0, true, 52, 36); // scrap
            guiFluidStacks.set(0, fluidOutput.get(0)); // uu matter
        } else if (recipeWrapper.emptyCellInput == null) {
            guiFluidStacks.set(0, fluidOutput.get(0)); // uu matter
        } else {
            guiFluidStacks.set(0, fluidInput.get(0)); // uu matter
            guiItemStacks.init(0, true, 105, 19); // emptyCell
            guiItemStacks.init(1, false, 105, 55); // filled Cell
        }

        guiItemStacks.set(ingredients);

    }

    public static final class MatterFabricatorRecipe implements IRecipeWrapper {

        public String amplifierValue;
        public ItemStack scrapInput;
        public FluidStack fluidOutput;

        public MatterFabricatorRecipe(@Nullable ItemStack scrapInput, FluidStack fluidOutput,
                @Nullable String amplifierValue) {
            this.amplifierValue = amplifierValue;
            this.scrapInput = scrapInput;
            this.fluidOutput = fluidOutput;
        }

        public FluidStack fluidInput;
        public ItemStack emptyCellInput;
        public ItemStack outputItem;

        public MatterFabricatorRecipe(FluidStack fluidInput, ItemStack emptyCellInput, ItemStack filledCellOutput) {
            this.fluidInput = fluidInput;
            this.emptyCellInput = emptyCellInput;
            this.outputItem = filledCellOutput;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            if (emptyCellInput == null) {
                ingredients.setInput(VanillaTypes.ITEM, scrapInput);
                ingredients.setOutput(VanillaTypes.FLUID, fluidOutput);
            } else {
                ingredients.setInput(VanillaTypes.FLUID, fluidInput);
                ingredients.setInput(VanillaTypes.ITEM, emptyCellInput);
                ingredients.setOutput(VanillaTypes.ITEM, outputItem);
            }
        }

        private String getAmplifier() {
            return amplifierValue;
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            FontRenderer font = minecraft.fontRenderer;
            int tierFromConfig = ConfigUtil.getInt(MainConfig.get(), "balance/matterFabricatorTier");
            String tier = I18n.format("ic2.item.tooltip.PowerTier", Integer.toString(tierFromConfig));

            String EU = I18n.format("ic2.generic.text.EU");
            String EUt = I18n.format("ic2.generic.text.EUt");

            font.drawString(tier, 0, 0, 4210752);
            font.drawString("512 " + EUt, 0, 12, 4210752);

            if (scrapInput != null) {
                if (getAmplifier() != null) {
                    String amplifier = Localization.translate("ic2.Matter.gui.info.amplifier");
                    font.drawString(amplifier, 0, 36, 4210752);
                    if (getAmplifier() == "45,000") {
                        font.drawString(I18n.format("+ " + getAmplifier()), 0, 48, 4210752);
                    }
                    if (getAmplifier() == "5,000") {
                        font.drawString(I18n.format("+ " + getAmplifier()), 6, 48, 4210752);
                    }
                    font.drawString("166,666 " + EU, 0, 70, 4210752);
                }

            } else if (outputItem != null) {
                font.drawString("32 " + EU, 0, 70, 4210752);

            } else if (fluidOutput != null) {
                font.drawString("1,000,000 " + EU, 0, 70, 4210752);
            }

        }

    }

}
