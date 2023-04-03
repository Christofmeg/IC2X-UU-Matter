package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import ic2.core.util.ConfigUtil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
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
    private final IDrawable tankGlass;
    private final IDrawable uuMatterFluid;

    protected IDrawableStatic tankOverlay;

    public static final ResourceLocation matterFabricatorTexture = new ResourceLocation("ic2",
            "textures/gui/guimatter.png");

    private static final ResourceLocation uuMatterFluidTexture = new ResourceLocation("ic2xuumatter",
            "textures/blocks/fluid/uu_matter_still.png");

    int xPos = 19;
    int yPos = 3;

    public MassFabricatorCategory(IGuiHelper helper) {
        background = helper.createDrawable(matterFabricatorTexture, xPos, yPos, 151, 77);

        tankGlass = helper.createDrawable(matterFabricatorTexture, 179, 60, 10, 37);

        uuMatterFluid = helper.drawableBuilder(uuMatterFluidTexture, 4, 4, 12, 47).buildAnimated(200,
                StartDirection.BOTTOM, false);

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

    @SuppressWarnings("deprecation")
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MatterFabricatorRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        List<List<FluidStack>> outputs = ingredients.getOutputs(FluidStack.class);

        guiItemStacks.init(0, true, 72 - xPos - 1, 40 - yPos - 1); // scrap
        guiFluidStacks.init(0, false, 81, 23, 12, 47, 8000, false, tankOverlay);

        guiFluidStacks.set(0, outputs.get(0));
        guiItemStacks.set(ingredients);

    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {

//        uuMatterFluid.draw(minecraft, 81, 23);
//        tankGlass.draw(minecraft, 84, 28);

    }

    public static final class MatterFabricatorRecipe implements IRecipeWrapper {

        public String energy;
        public ItemStack scrapInput;
        public ItemStack emptyCellInput;
        public ItemStack outputItem;
        public FluidStack fluidOutput;

        public MatterFabricatorRecipe(ItemStack scrapInput, ItemStack emptyCellInput, ItemStack itemOutput,
                @Nullable String energyRequired) {
            this.energy = energyRequired;
            this.scrapInput = scrapInput;
            this.emptyCellInput = emptyCellInput;
            this.outputItem = itemOutput;
        }

        public MatterFabricatorRecipe(ItemStack scrapInput, FluidStack fluidOutput, @Nullable String energyRequired) {
            this.energy = energyRequired;
            this.scrapInput = scrapInput;
            this.fluidOutput = fluidOutput;
        }

        public String getEnergy() {
            return energy;
        }

        public FluidStack getFluid() {
            return fluidOutput;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(VanillaTypes.ITEM, scrapInput);
            ingredients.setOutput(VanillaTypes.FLUID, fluidOutput);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

            FontRenderer font = minecraft.fontRenderer;
            String tier = I18n.format("translation.ic2xuumatter.tier");
            int tierFromConfig = ConfigUtil.getInt(MainConfig.get(), "balance/matterFabricatorTier");
            font.drawString(tier + Integer.toString(tierFromConfig), 0, 0, 4210752);

            if (getEnergy() != null) {
                String amplifier = Localization.translate("ic2.Matter.gui.info.amplifier");
                font.drawString(amplifier, 0, 46, 4210752);
                if (getEnergy() == "45,000") {
                    font.drawString(I18n.format("+ " + getEnergy()), 0, 58, 4210752);
                }
                if (getEnergy() == "5,000") {
                    font.drawString(I18n.format("+ " + getEnergy()), 6, 58, 4210752);
                }
            }
        }

    }

}
