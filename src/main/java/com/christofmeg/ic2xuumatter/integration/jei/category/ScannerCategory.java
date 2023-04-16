package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.ArrayList;
import java.util.List;

import com.christofmeg.ic2xuumatter.utils.Utils;

import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import ic2.core.uu.UuIndex;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
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

public class ScannerCategory implements IRecipeCategory<ScannerCategory.ScannerRecipe> {

    public static String UID = "ic2xuumatter.scanner";

    IDrawable background;
    IDrawable scanner;
    IDrawable energy;
    IDrawable progress;

    public static final ResourceLocation scannerTexture = new ResourceLocation("ic2", "textures/gui/guiscanner.png");
    public static final ResourceLocation commonTexture = new ResourceLocation("ic2", "textures/gui/common.png");

    public ScannerCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(162, 78);
        scanner = helper.createDrawable(scannerTexture, 7, 19, 162, 63);
        energy = helper.drawableBuilder(commonTexture, 113, 64, 14, 15).buildAnimated(300, StartDirection.TOP, true);
        progress = helper.drawableBuilder(scannerTexture, 176, 14, 66, 43).buildAnimated(50, StartDirection.LEFT,
                false);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return BlockName.te.getItemStack(TeBlock.scanner).getDisplayName();
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
        scanner.draw(minecraft, 0, 7);
        energy.draw(minecraft, 2, 12);
        progress.draw(minecraft, 23, 8);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ScannerRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 47, 22); // scannableItem
        guiItemStacks.init(1, false, 144, 52); // crystalMemory output
        guiItemStacks.init(2, false, 0, 30); // batteries
        guiItemStacks.init(3, true, 144, 52); // crystalMemory input

        List<ItemStack> crystalMemoryList = new ArrayList<>();
        crystalMemoryList.add(ItemName.crystal_memory.getItemStack());
        crystalMemoryList.add(recipeWrapper.crystalMemory);

        guiItemStacks.set(0, recipeWrapper.scannableItem); // scannableItem
        guiItemStacks.set(1, recipeWrapper.crystalMemory); // crystalMemory output
        guiItemStacks.set(2, Utils.getValidBatteryList(true, 4)); // batteries
        guiItemStacks.set(3, crystalMemoryList); // crystalMemory input
    }

    public static final class ScannerRecipe implements IRecipeWrapper {
        public ItemStack scannableItem;
        public ItemStack crystalMemory;

        public ScannerRecipe(ItemStack scannableItem, ItemStack crystalMemory) {
            this.scannableItem = scannableItem;
            this.crystalMemory = crystalMemory;
        }

        /*
         * Tells JEI what items can be used in a recipe & item 'U' usage
         */
        @Override
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> itemInputSlots = new ArrayList<>();
            List<ItemStack> stack = new ArrayList<>();
            stack.add(scannableItem);
            itemInputSlots.add(stack);

            List<ItemStack> crystalMemoryList = new ArrayList<>();
            crystalMemoryList.add(ItemName.crystal_memory.getItemStack());
            itemInputSlots.add(crystalMemoryList);

            ingredients.setOutput(VanillaTypes.ITEM, crystalMemory);
            ingredients.setInputLists(VanillaTypes.ITEM, itemInputSlots);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            FontRenderer font = minecraft.fontRenderer;
            String tier = I18n.format("ic2.item.tooltip.PowerTier", 4);
            font.drawString(tier, 0, 0, 4210752);
            String EU = I18n.format("ic2.generic.text.EU");
            font.drawString(Util.toSiString(UuIndex.instance.getInBuckets(scannableItem), 2) + "B", 96, 13, 16777215);
            font.drawString(Util.toSiString((UuIndex.instance.get(scannableItem) / 10) * 512, 3) + " " + EU, 96, 24,
                    16777215);
            font.drawString("844,800 " + EU, 0, 72, 4210752);
        }
    }

}
