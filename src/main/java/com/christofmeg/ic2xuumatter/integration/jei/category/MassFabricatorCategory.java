package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.christofmeg.ic2xuumatter.utils.Utils;

import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import ic2.core.util.ConfigUtil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
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

public class MassFabricatorCategory implements IRecipeCategory<MassFabricatorCategory.MassFabricatorRecipe> {

    public static String UID = "ic2xuumatter.mass_fabricator";

    IDrawable background;
    IDrawableBuilder smallSlot;
    IDrawableBuilder bigSlot;
    IDrawableBuilder energyEmpty;
    IDrawableAnimated energyFull;

    public static final ResourceLocation commonTexture = new ResourceLocation("ic2", "textures/gui/common.png");

    public MassFabricatorCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(129, 76);
        smallSlot = helper.drawableBuilder(commonTexture, 103, 7, 18, 18);
        bigSlot = helper.drawableBuilder(commonTexture, 99, 35, 26, 26);
        energyEmpty = helper.drawableBuilder(commonTexture, 97, 64, 14, 15);
        energyFull = helper.drawableBuilder(commonTexture, 113, 64, 14, 15).buildAnimated(300, StartDirection.TOP,
                true);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return BlockName.te.getItemStack(TeBlock.mass_fabricator).getDisplayName();
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
    public void setRecipe(IRecipeLayout recipeLayout, MassFabricatorRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, false, 89, 4); // UU-Matter
        guiItemStacks.init(1, true, 89, 43); // Scrap
        guiItemStacks.init(2, false, 58, 43); // batteries
        guiItemStacks.set(2, recipeWrapper.batteries); // batteries
        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        bigSlot.build().draw(minecraft, 85, 0); // UU-Matter
        smallSlot.build().draw(minecraft, 89, 43); // Scrap
        energyEmpty.build().draw(minecraft, 90, 27); // Energy background
        energyFull.draw(minecraft, 90, 27); // Energy overlay
        smallSlot.build().draw(minecraft, 58, 43); // Batteries
    }

    public static final class MassFabricatorRecipe implements IRecipeWrapper {

        public String amplifierValue;
        public ItemStack inputItem;
        public ItemStack outputItem;
        int tierFromConfig = ConfigUtil.getInt(MainConfig.get(), "balance/massFabricatorTier");
        public List<ItemStack> batteries = Utils.getValidBatteryList(false, tierFromConfig);

        public MassFabricatorRecipe(ItemStack itemInput, ItemStack itemOutput, @Nullable String amplifierValue) {
            this.amplifierValue = amplifierValue;
            this.inputItem = itemInput;
            this.outputItem = itemOutput;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> itemInputSlots = new ArrayList<>();
            List<ItemStack> stack = new ArrayList<>();
            stack.add(inputItem);
            itemInputSlots.add(stack);
            itemInputSlots.add(batteries);
            ingredients.setOutput(VanillaTypes.ITEM, outputItem);
            ingredients.setInputLists(VanillaTypes.ITEM, itemInputSlots);
        }

        private String getAmplifier() {
            return amplifierValue;
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            FontRenderer font = minecraft.fontRenderer;
            int tierFromConfig = ConfigUtil.getInt(MainConfig.get(), "balance/massFabricatorTier");
            String tier = I18n.format("ic2.item.tooltip.PowerTier", Integer.toString(tierFromConfig));

            String EU = I18n.format("ic2.generic.text.EU");
            String EUt = I18n.format("ic2.generic.text.EUt");
            int tierEU = (int) Math.pow(4, (tierFromConfig + 1)) * 2;
            font.drawString(tier, 0, 0, 4210752);
            font.drawString(tierEU + " " + EUt, 0, 12, 4210752);

            if (inputItem != null) {
                if (getAmplifier() != null) {
                    String amplifier = Localization.translate("ic2.Matter.gui.info.amplifier");
                    font.drawString(amplifier, 0, 36, 4210752);
                    if (getAmplifier() == "45,000") {
                        font.drawString(I18n.format("+ " + getAmplifier()), 0, 48, 4210752);
                    }
                    if (getAmplifier() == "5,000") {
                        font.drawString(I18n.format("+ " + getAmplifier()), 6, 48, 4210752);
                    }
                    double euRaw = Double.parseDouble("" + Math
                            .round(1000000.0F * ConfigUtil.getFloat(MainConfig.get(), "balance/uuEnergyFactor") / 6));
                    font.drawString(String.format("%,.0f", euRaw) + " " + EU, 0, 70, 4210752);
                } else {

                    double euRaw = Double.parseDouble(""
                            + Math.round(1000000.0F * ConfigUtil.getFloat(MainConfig.get(), "balance/uuEnergyFactor")));
                    font.drawString(String.format("%,.0f", euRaw) + " " + EU, 0, 70, 4210752);
                }

            }
        }
    }

}