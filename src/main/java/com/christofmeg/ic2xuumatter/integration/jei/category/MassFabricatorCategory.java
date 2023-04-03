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

    @SuppressWarnings("deprecation")
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MatterFabricatorRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        List<List<FluidStack>> outputs = ingredients.getOutputs(FluidStack.class);

        guiItemStacks.init(0, true, 52, 36); // scrap
        guiFluidStacks.init(0, false, 81, 23, 12, 47, 8000, false, tankOverlay); // uu matter

        guiFluidStacks.set(0, outputs.get(0));
        guiItemStacks.set(ingredients);

    }

    public static final class MatterFabricatorRecipe implements IRecipeWrapper {

        public String amplifier;
        public ItemStack scrapInput;
        public FluidStack fluidOutput;

        public MatterFabricatorRecipe(ItemStack scrapInput, FluidStack fluidOutput, @Nullable String amplifier) {
            this.amplifier = amplifier;
            this.scrapInput = scrapInput;
            this.fluidOutput = fluidOutput;
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

            if (amplifier != null) {
                String amplifier = Localization.translate("ic2.Matter.gui.info.amplifier");
                font.drawString(amplifier, 0, 46, 4210752);
                if (amplifier == "45,000") {
                    font.drawString(I18n.format("+ " + amplifier), 0, 58, 4210752);
                }
                if (amplifier == "5,000") {
                    font.drawString(I18n.format("+ " + amplifier), 6, 58, 4210752);
                }
            }
        }

    }

}
