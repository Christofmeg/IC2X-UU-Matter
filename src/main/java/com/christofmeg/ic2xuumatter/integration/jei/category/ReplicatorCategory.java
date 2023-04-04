package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.List;

import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
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

public class ReplicatorCategory implements IRecipeCategory<ReplicatorCategory.ReplicatorRecipe> {

    public static String UID = "ic2xuumatter.replicator";

    IDrawable background;
    protected IDrawableStatic tankOverlay;

    public static final ResourceLocation replicatorTexture = new ResourceLocation("ic2",
            "textures/gui/guireplicator.png");

    public ReplicatorCategory(IGuiHelper helper) {
        background = helper.createDrawable(replicatorTexture, 7, 6, 163, 93);
        tankOverlay = helper.createDrawable(replicatorTexture, 31, 34, 16, 60);
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
    public void setRecipe(IRecipeLayout recipeLayout, ReplicatorRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        List<List<FluidStack>> fluidOutput = ingredients.getOutputs(VanillaTypes.FLUID);
        List<List<FluidStack>> fluidInput = ingredients.getInputs(VanillaTypes.FLUID);

        guiFluidStacks.init(0, false, 24, 28, 12, 47, 8000, true, tankOverlay); // uu matter

        guiItemStacks.init(0, true, 0, 65); // filled cell
        guiItemStacks.init(1, false, 0, 20); // empty cell
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
