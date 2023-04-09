package com.christofmeg.ic2xuumatter.integration.jei;

import java.util.Arrays;
import java.util.Collections;

import javax.annotation.Nonnull;

import com.christofmeg.ic2xuumatter.integration.jei.category.MassFabricatorCategory;
import com.christofmeg.ic2xuumatter.integration.jei.category.ReplicatorCategory;
import com.christofmeg.ic2xuumatter.integration.jei.category.ScannerCategory;
import com.christofmeg.ic2xuumatter.utils.Utils;

import ic2.core.block.machine.gui.GuiMatter;
import ic2.core.block.machine.gui.GuiReplicator;
import ic2.core.item.type.CellType;
import ic2.core.item.type.CraftingItemType;
import ic2.core.ref.BlockName;
import ic2.core.ref.FluidName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import ic2.core.util.StackUtil;
import ic2.core.uu.UuGraph;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
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

        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.scanner), ScannerCategory.UID);

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

        UuGraph.iterator().forEachRemaining(item -> {
            ItemStack stack = item.getKey().copy();
            ItemStack pattern = StackUtil.emptyStack;
            pattern = UuGraph.find(stack);
            if (!StackUtil.isEmpty(pattern)) {
                if (stack != null && stack.getItem() != null) {
                    if (item.getValue() != Double.POSITIVE_INFINITY) {
                        double mbValue = item.getValue() / 100;
                        int mb = (int) Math.round(mbValue);
                        if (mbValue - mb > 0) {
                            mb += 1;
                        }
                        registration.addRecipes(Collections.singletonList(new ReplicatorCategory.ReplicatorRecipe(
                                new FluidStack(FluidName.uu_matter.getInstance(), mb), item.getKey(),
                                Utils.getCrystalMemory(item.getKey()))), ReplicatorCategory.UID);

                        registration
                                .addRecipes(Collections.singletonList(new ScannerCategory.ScannerRecipe(item.getKey(),
                                        Utils.getCrystalMemory(item.getKey()))), ScannerCategory.UID);

                    }
                }
            }
        });

        registration.addRecipeClickArea(GuiMatter.class, 117, 41, 21, 16, MassFabricatorCategory.UID);
        registration.addRecipeClickArea(GuiReplicator.class, 12, 45, 13, 24, ReplicatorCategory.UID);

        /*
         * IRecipeTransferRegistry transferRegistry =
         * registration.getRecipeTransferRegistry(); int recipeSlotStart = 1; int
         * inputsrecipeSlotCount = 1; int inputsinventorySlotStart = 2; int
         * inventorySlotCount = 36;
         * transferRegistry.addRecipeTransferHandler(ContainerScanner.class,
         * ScannerCategory.UID, recipeSlotStart, inputsrecipeSlotCount,
         * inputsinventorySlotStart, inventorySlotCount);
         *
         *
         * int mrecipeSlotStart = 0; int mrecipeSlotCount = 2; int minventorySlotStart =
         * 3; int minventorySlotCount = 39;
         *
         *
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

        registration.addRecipeCategories(new MassFabricatorCategory(helper), new ReplicatorCategory(helper),
                new ScannerCategory(helper));
    }

}