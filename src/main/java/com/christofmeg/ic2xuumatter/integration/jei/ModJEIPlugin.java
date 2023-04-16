package com.christofmeg.ic2xuumatter.integration.jei;

import java.util.Arrays;
import java.util.Collections;

import javax.annotation.Nonnull;

import com.christofmeg.ic2xuumatter.integration.jei.category.MatterFabricatorCategory;
import com.christofmeg.ic2xuumatter.integration.jei.category.PatternStorageCategory;
import com.christofmeg.ic2xuumatter.integration.jei.category.ReplicatorCategory;
import com.christofmeg.ic2xuumatter.integration.jei.category.ScannerCategory;
import com.christofmeg.ic2xuumatter.integration.jei.handler.TransferHandlerMatterFabricator;
import com.christofmeg.ic2xuumatter.utils.Utils;

import ic2.core.block.machine.container.ContainerPatternStorage;
import ic2.core.block.machine.container.ContainerReplicator;
import ic2.core.block.machine.container.ContainerScanner;
import ic2.core.block.machine.gui.GuiMatter;
import ic2.core.block.machine.gui.GuiPatternStorage;
import ic2.core.block.machine.gui.GuiReplicator;
import ic2.core.block.machine.gui.GuiScanner;
import ic2.core.init.MainConfig;
import ic2.core.item.type.CellType;
import ic2.core.item.type.CraftingItemType;
import ic2.core.ref.BlockName;
import ic2.core.ref.FluidName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import ic2.core.uu.UuGraph;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@JEIPlugin
@SideOnly(Side.CLIENT)
public class ModJEIPlugin implements IModPlugin {

    public static IModRegistry registration;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(ItemName.crystal_memory.getInstance());
    }

    @Override
    public void register(@Nonnull IModRegistry registration) {

        ModJEIPlugin.registration = registration;

        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.matter_generator),
                MatterFabricatorCategory.UID);

        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.replicator), ReplicatorCategory.UID);

        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.pattern_storage), PatternStorageCategory.UID);
        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.pattern_storage), ReplicatorCategory.UID);

        registration.addRecipeCatalyst(BlockName.te.getItemStack(TeBlock.scanner), ScannerCategory.UID);

        int scrapBoxCount = Math.round(4 * ConfigUtil.getFloat(MainConfig.get(), "balance/uuEnergyFactor"));
        int scrapCount = Math.round(34 * ConfigUtil.getFloat(MainConfig.get(), "balance/uuEnergyFactor"));
        if (scrapCount < 1)
            scrapCount = 1;
        if (scrapBoxCount < 1)
            scrapBoxCount = 1;

        registration.addRecipes(
                Arrays.asList(
                        new MatterFabricatorCategory.MatterFabricatorRecipe(null,
                                new FluidStack(FluidName.uu_matter.getInstance(), 1), null),

                        new MatterFabricatorCategory.MatterFabricatorRecipe(
                                new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.scrap).getItem(),
                                        scrapCount, 23),
                                new FluidStack(FluidName.uu_matter.getInstance(), 1), "5,000"),

                        new MatterFabricatorCategory.MatterFabricatorRecipe(
                                new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.scrap_box).getItem(),
                                        scrapBoxCount, 24),
                                new FluidStack(FluidName.uu_matter.getInstance(), 1), "45,000"),

                        new MatterFabricatorCategory.MatterFabricatorRecipe(
                                new FluidStack(FluidName.uu_matter.getInstance(), 1000),
                                ItemName.fluid_cell.getItemStack(),
                                Utils.getCellFromFluid(FluidName.uu_matter.getName()))),

                MatterFabricatorCategory.UID);

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

                        registration.addRecipes(
                                Collections.singletonList(new PatternStorageCategory.PatternStorageRecipe(
                                        Utils.getCrystalMemory(item.getKey()), item.getKey())),
                                PatternStorageCategory.UID);
                    }
                }
            }
        });

        registration.addRecipeClickArea(GuiMatter.class, 117, 41, 21, 16, MatterFabricatorCategory.UID);
        registration.addRecipeClickArea(GuiPatternStorage.class, 47, 19, 103, 24, PatternStorageCategory.UID);
        registration.addRecipeClickArea(GuiReplicator.class, 12, 45, 13, 24, ReplicatorCategory.UID);
        registration.addRecipeClickArea(GuiScanner.class, 26, 21, 22, 41, ScannerCategory.UID);

        IRecipeTransferRegistry recipeTransferRegistry = registration.getRecipeTransferRegistry();
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        recipeTransferRegistry.addRecipeTransferHandler(
                new TransferHandlerMatterFabricator(jeiHelpers.recipeTransferHandlerHelper()),
                MatterFabricatorCategory.UID);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerPatternStorage.class, PatternStorageCategory.UID, 36,
                1, 0, 36);

        recipeTransferRegistry.addRecipeTransferHandler(ContainerReplicator.class, ReplicatorCategory.UID, 38, 2, 0,
                36);

        recipeTransferRegistry.addRecipeTransferHandler(ContainerScanner.class, ScannerCategory.UID, 37, 2, 0, 36);

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new MatterFabricatorCategory(helper), new PatternStorageCategory(helper),
                new ReplicatorCategory(helper), new ScannerCategory(helper));
    }

}