package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.List;

import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
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

    IDrawable replicator;
    IDrawable energy;
    IDrawableBuilder patternStorage;
    IDrawableStatic tankOverlay;

    IDrawable ingredient;

    IDrawable background;

    IDrawableBuilder patternStorageInfobox;
    IDrawableBuilder patternStorageArrows;
    IDrawableBuilder patternStorageSlot;

    public static final ResourceLocation replicatorTexture = new ResourceLocation("ic2",
            "textures/gui/guireplicator.png");
    public static final ResourceLocation patternStorageTexture = new ResourceLocation("ic2",
            "textures/gui/guipatternstorage.png");
    public static final ResourceLocation jeiGuiBackground = new ResourceLocation("jei",
            "textures/gui/gui_background.png");
    public static final ResourceLocation jeiCatalystTab = new ResourceLocation("jei", "textures/gui/catalyst_tab.png");
    public static final ResourceLocation jeiRecipeBackground = new ResourceLocation("jei",
            "textures/gui/single_recipe_background.png");
    public static final ResourceLocation modGuiArrows = new ResourceLocation("ic2xuumatter", "textures/gui/arrows.png");

    public ReplicatorCategory(IGuiHelper helper) {

        background = helper.createBlankDrawable(162, 160);

        replicator = helper.createDrawable(replicatorTexture, 7, 7, 162, 93);

        ingredient = helper.createDrawableIngredient(new ItemStack(ItemName.crystal_memory.getInstance()));

        energy = helper.drawableBuilder(replicatorTexture, 176, 0, 14, 15).buildAnimated(300, StartDirection.TOP, true);
        tankOverlay = helper.createDrawable(replicatorTexture, 48 + 64 * 2, 193, 16, 60);
        patternStorage = helper.drawableBuilder(patternStorageTexture, 7, 19, 162, 62);

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

        replicator.draw(minecraft, 0, 0);

        energy.draw(minecraft, 126, 76);

        patternStorageInfobox.build().draw(minecraft, 0, 124);
        patternStorageArrows.build().draw(minecraft, 1, 97);
        patternStorageSlot.build().draw(minecraft, 10, 95);
        patternStorageSlot.build().draw(minecraft, 144, 104);

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

            String patternStorage = BlockName.te.getItemStack(TeBlock.pattern_storage).getDisplayName();
            font.drawString(patternStorage, 54, 108, 4210752);

        }
    }

}
