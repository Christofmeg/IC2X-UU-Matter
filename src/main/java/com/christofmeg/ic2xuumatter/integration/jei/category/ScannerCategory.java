package com.christofmeg.ic2xuumatter.integration.jei.category;

import java.util.ArrayList;
import java.util.List;

import com.christofmeg.ic2xuumatter.utils.Utils;

import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
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
        background = helper.createBlankDrawable(162, 70);
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
        guiItemStacks.init(2, true, 0, 30); // batteries
        guiItemStacks.set(ingredients);
    }

    public static final class ScannerRecipe implements IRecipeWrapper {
        public ItemStack scannableItem;
        public ItemStack crystalMemory;
        public List<ItemStack> batteries;

        public ScannerRecipe(ItemStack scannableItem, ItemStack crystalMemory) {
            this.scannableItem = scannableItem;
            this.crystalMemory = crystalMemory;
            this.batteries = Utils.getValidBatteryList(true, 4);
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> itemInputSlots = new ArrayList<>();
            List<ItemStack> stack = new ArrayList<>();
            stack.add(scannableItem);
            itemInputSlots.add(stack);
            itemInputSlots.add(batteries);
            ingredients.setInputLists(VanillaTypes.ITEM, itemInputSlots);
            ingredients.setOutput(VanillaTypes.ITEM, crystalMemory);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            FontRenderer font = minecraft.fontRenderer;
            String tier = I18n.format("ic2.item.tooltip.PowerTier", 4);
            font.drawString(tier, 0, 0, 4210752);
        }
    }

    // TODO Scanner: Recipe Transfer helper
    // TODO Scanner: drawInfo, energy
    // TODO Scanner: GuiClickArea

}
