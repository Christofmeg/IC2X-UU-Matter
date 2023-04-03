package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.List;

import javax.annotation.Nullable;

import com.christofmeg.ic2xuumatter.utils.Utils;

import ic2.core.init.MainConfig;
import ic2.core.ref.BlockName;
import ic2.core.ref.FluidName;
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

public class MassFabricatorSubCategory implements IRecipeCategory<MassFabricatorSubCategory.MatterFabricatorRecipe> {

    public static String UID = "ic2xuumatter.matter_fabricator_sub";

    private final IDrawable icon;
    private final IDrawable background;
    protected IDrawableStatic tankOverlay;

    public static final ResourceLocation matterFabricatorTexture = new ResourceLocation("ic2",
            "textures/gui/guimatter.png");

    public MassFabricatorSubCategory(IGuiHelper helper) {
        background = helper.createDrawable(matterFabricatorTexture, 19, 3, 151, 77);
        tankOverlay = helper.createDrawable(matterFabricatorTexture, 48 + 64 * 2, 193, 16, 60);
        icon = helper.createDrawableIngredient(Utils.getCellFromFluid(FluidName.uu_matter.getName()));
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

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MatterFabricatorRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        List<List<FluidStack>> fluidInput = ingredients.getInputs(FluidStack.class);

        guiItemStacks.init(0, true, 105, 19); // emptyCell
        guiItemStacks.init(1, false, 105, 55); // filled Cell

        guiFluidStacks.init(0, true, 81, 23, 12, 47, 8000, false, tankOverlay); // uu matter

        guiFluidStacks.set(0, fluidInput.get(0));
        guiItemStacks.set(ingredients);

    }

    public static final class MatterFabricatorRecipe implements IRecipeWrapper {

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
            ingredients.setInput(VanillaTypes.FLUID, fluidInput);
            ingredients.setInput(VanillaTypes.ITEM, emptyCellInput);
            ingredients.setOutput(VanillaTypes.ITEM, outputItem);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            FontRenderer font = minecraft.fontRenderer;
            String tier = I18n.format("translation.ic2xuumatter.tier");
            int tierFromConfig = ConfigUtil.getInt(MainConfig.get(), "balance/matterFabricatorTier");
            font.drawString(tier + Integer.toString(tierFromConfig), 0, 0, 4210752);
        }

    }

}
