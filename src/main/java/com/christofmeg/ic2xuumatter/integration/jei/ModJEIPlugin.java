package com.christofmeg.ic2xuumatter.integration.jei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.christofmeg.ic2xuumatter.integration.jei.category.MassFabricatorCategory;
import com.christofmeg.ic2xuumatter.integration.jei.category.ReplicatorCategory;
import com.christofmeg.ic2xuumatter.utils.Utils;

import ic2.core.block.machine.gui.GuiMatter;
import ic2.core.block.machine.gui.GuiReplicator;
import ic2.core.item.type.CellType;
import ic2.core.item.type.CraftingItemType;
import ic2.core.ref.BlockName;
import ic2.core.ref.FluidName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@JEIPlugin
@SideOnly(Side.CLIENT)
public class ModJEIPlugin implements IModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registration) {

        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.matter_generator), MassFabricatorCategory.UID);

        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.replicator), ReplicatorCategory.UID);
        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.pattern_storage), ReplicatorCategory.UID);

        registration.addRecipes(Arrays.asList(
                new MassFabricatorCategory.MatterFabricatorRecipe(null,
                        new FluidStack(FluidName.uu_matter.getInstance(), 1), null),
                new MassFabricatorCategory.MatterFabricatorRecipe(
                        new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.scrap).getItem(), 34, 23),
                        new FluidStack(FluidName.uu_matter.getInstance(), 1), "5,000"),
                new MassFabricatorCategory.MatterFabricatorRecipe(
                        new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.scrap_box).getItem(), 4, 24),
                        new FluidStack(FluidName.uu_matter.getInstance(), 1), "45,000"),
                new MassFabricatorCategory.MatterFabricatorRecipe(
                        new FluidStack(FluidName.uu_matter.getInstance(), 1000), ItemName.fluid_cell.getItemStack(),
                        Utils.getCellFromFluid(FluidName.uu_matter.getName()))),
                MassFabricatorCategory.UID);

        registration.addRecipes(Collections.singletonList(new ReplicatorCategory.ReplicatorRecipe(
                Utils.getCellFromFluid(FluidName.uu_matter.getName()), ItemName.cell.getItemStack(CellType.empty),
                new FluidStack(FluidName.uu_matter.getInstance(), 1000))), ReplicatorCategory.UID);

        registration.addRecipeClickArea(GuiMatter.class, 117, 41, 21, 16, MassFabricatorCategory.UID);
        registration.addRecipeClickArea(GuiReplicator.class, 12, 45, 13, 24, ReplicatorCategory.UID);

//        registration.addAdvancedGuiHandlers(new GlobalReplicatorHandler());

        /*
         * int mrecipeSlotStart = 0; int mrecipeSlotCount = 2; int minventorySlotStart =
         * 3; int minventorySlotCount = 39;
         *
         * // TODO MATTER Fabricator Transfer handler matter
         * registration.getRecipeTransferRegistry().addRecipeTransferHandler(
         * ContainerMatter.class, MassFabricatorCategory.UID, mrecipeSlotStart,
         * mrecipeSlotCount, minventorySlotStart, minventorySlotCount);
         *
         *
         * int recipeSlotStart = 1; int recipeSlotCount = 2; int inventorySlotStart = 4;
         * int inventorySlotCount = 36;
         * registration.getRecipeTransferRegistry().addRecipeTransferHandler(
         * ContainerReplicator.class, ReplicatorCategory.UID, recipeSlotStart,
         * recipeSlotCount, inventorySlotStart, inventorySlotCount);
         */
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new MassFabricatorCategory(helper), new ReplicatorCategory(helper));
    }

    public static class GlobalReplicatorHandler implements IAdvancedGuiHandler<GuiContainer> {

        @Override
        public List<Rectangle> getGuiExtraAreas(GuiContainer guiContainer) {
            int x = guiContainer.getGuiLeft() - 124;
            int y = guiContainer.getGuiTop();
            Minecraft minecraft = guiContainer.mc;
            if (minecraft == null) {
                return Collections.emptyList();
            }
            List<Rectangle> areas = new ArrayList<>();
            areas.add(new Rectangle(x, y, 140, 32));
            return areas;

        }

        @Override
        public Class<GuiContainer> getGuiContainerClass() {
            return GuiContainer.class;
        }
    }

}