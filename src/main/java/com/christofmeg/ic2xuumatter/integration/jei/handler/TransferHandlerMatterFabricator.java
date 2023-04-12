package com.christofmeg.ic2xuumatter.integration.jei.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.christofmeg.ic2xuumatter.integration.jei.category.MatterFabricatorCategory;
import com.google.common.collect.ImmutableSet;

import ic2.core.block.machine.container.ContainerMatter;
import ic2.core.item.type.CraftingItemType;
import ic2.core.ref.ItemName;
import mezz.jei.Internal;
import mezz.jei.JustEnoughItems;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.config.ServerInfo;
import mezz.jei.gui.ingredients.GuiItemStackGroup;
import mezz.jei.network.packets.PacketRecipeTransfer;
import mezz.jei.startup.StackHelper;
import mezz.jei.transfer.BasicRecipeTransferInfo;
import mezz.jei.util.Translator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class TransferHandlerMatterFabricator implements IRecipeTransferHandler<ContainerMatter> {

    private final StackHelper stackHelper;
    private final IRecipeTransferHandlerHelper handlerHelper;
    private IRecipeTransferInfo<ContainerMatter> transferHelper;

    public TransferHandlerMatterFabricator(IRecipeTransferHandlerHelper handlerHelper) {
        this.stackHelper = Internal.getStackHelper();
        this.handlerHelper = handlerHelper;
        this.transferHelper = null;
    }

    @Override
    public Class<ContainerMatter> getContainerClass() {
        return ContainerMatter.class;
    }

    @Override
    public IRecipeTransferError transferRecipe(ContainerMatter container, IRecipeLayout recipeLayout,
            EntityPlayer player, boolean maxTransfer, boolean doTransfer) {

        Map<Integer, ? extends IGuiIngredient<ItemStack>> itemStack = recipeLayout
                .getIngredientsGroup(VanillaTypes.ITEM).getGuiIngredients();

        ItemStack scrap = new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.scrap).getItem(), 34, 23);
        ItemStack scrapBox = new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.scrap_box).getItem(), 4, 24);
        ItemStack emptyCell = ItemName.fluid_cell.getItemStack();

        List<ItemStack> input = new ArrayList<>();
        for (Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : itemStack.entrySet()) {
            input.add(entry.getValue().getDisplayedIngredient());
        }
        List<ItemStack> scrapList = new ArrayList<>();
        List<ItemStack> scrapBoxList = new ArrayList<>();
        List<ItemStack> emptyCellList = new ArrayList<>();
        scrapList.add(scrap);
        scrapBoxList.add(scrapBox);
        emptyCellList.add(emptyCell);

        if (input != null) {
            if (!input.isEmpty() && input.get(0).getTranslationKey().toString().contains("scrap")) {
                this.transferHelper = new BasicRecipeTransferInfo<>(ContainerMatter.class, MatterFabricatorCategory.UID,
                        36, 1, 0, 36);
            } else {
                this.transferHelper = new BasicRecipeTransferInfo<>(ContainerMatter.class, MatterFabricatorCategory.UID,
                        38, 1, 0, 36);
            }
        }

        if (!ServerInfo.isJeiOnServer()) {
            String tooltipMessage = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.no.server");
            return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
        }

        if (!transferHelper.canHandle(container)) {
            return handlerHelper.createInternalError();
        }

        Map<Integer, Slot> inventorySlots = new HashMap<>();
        Map<Integer, Slot> craftingSlots = new HashMap<>();
        for (Slot slot : transferHelper.getInventorySlots(container)) {
            inventorySlots.put(slot.slotNumber, slot);
        }
        for (Slot slot : transferHelper.getRecipeSlots(container)) {
            craftingSlots.put(slot.slotNumber, slot);
        }

        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        int inputCount = 0;
        {
            // indexes that do not fit into the player crafting grid
            Set<Integer> badIndexes = ImmutableSet.of(2, 5, 6, 7, 8);

            int inputIndex = 0;
            for (IGuiIngredient<ItemStack> ingredient : itemStackGroup.getGuiIngredients().values()) {
                if (ingredient.isInput()) {
                    if (!ingredient.getAllIngredients().isEmpty()) {
                        inputCount++;
                        if (badIndexes.contains(inputIndex)) {
                            String tooltipMessage = Translator
                                    .translateToLocal("jei.tooltip.error.recipe.transfer.too.large.player.inventory");
                            return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
                        }
                    }
                    inputIndex++;
                }
            }
        }

        // compact the crafting grid into a 2x2 area
        List<IGuiIngredient<ItemStack>> guiIngredients = new ArrayList<>();
        for (IGuiIngredient<ItemStack> guiIngredient : itemStackGroup.getGuiIngredients().values()) {
            if (guiIngredient.isInput()) {
                guiIngredients.add(guiIngredient);
            }
        }
        IGuiItemStackGroup playerInvItemStackGroup = new GuiItemStackGroup(null, 0);
        int[] playerGridIndexes = { 0, 1, 3, 4 };
        for (int i = 0; i < 4; i++) {
            int index = playerGridIndexes[i];
            if (index < guiIngredients.size()) {
                IGuiIngredient<ItemStack> ingredient = guiIngredients.get(index);
                playerInvItemStackGroup.init(i, true, 0, 0);
                playerInvItemStackGroup.set(i, ingredient.getAllIngredients());
            }
        }

        Map<Integer, ItemStack> availableItemStacks = new HashMap<>();
        int filledCraftSlotCount = 0;
        int emptySlotCount = 0;

        for (Slot slot : craftingSlots.values()) {
            final ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                if (!slot.canTakeStack(player)) {
                    return handlerHelper.createInternalError();
                }
                filledCraftSlotCount++;
                availableItemStacks.put(slot.slotNumber, stack.copy());
            }
        }

        for (Slot slot : inventorySlots.values()) {
            final ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                availableItemStacks.put(slot.slotNumber, stack.copy());
            } else {
                emptySlotCount++;
            }
        }

        // check if we have enough inventory space to shuffle items around to their
        // final locations
        if (filledCraftSlotCount - inputCount > emptySlotCount) {
            String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.inventory.full");
            return handlerHelper.createUserErrorWithTooltip(message);
        }

        StackHelper.MatchingItemsResult matchingItemsResult = stackHelper.getMatchingItems(availableItemStacks,
                playerInvItemStackGroup.getGuiIngredients());

        if (matchingItemsResult.missingItems.size() > 0) {
            String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.missing");
            matchingItemsResult = stackHelper.getMatchingItems(availableItemStacks, itemStackGroup.getGuiIngredients());
            return handlerHelper.createUserErrorForSlots(message, matchingItemsResult.missingItems);
        }

        List<Integer> craftingSlotIndexes = new ArrayList<>(craftingSlots.keySet());
        Collections.sort(craftingSlotIndexes);

        List<Integer> inventorySlotIndexes = new ArrayList<>(inventorySlots.keySet());
        Collections.sort(inventorySlotIndexes);

        // check that the slots exist and can be altered
        for (Map.Entry<Integer, Integer> entry : matchingItemsResult.matchingItems.entrySet()) {
            int craftNumber = entry.getKey();
            int slotNumber = craftingSlotIndexes.get(craftNumber);
            if (slotNumber < 0 || slotNumber >= container.inventorySlots.size()) {
                return handlerHelper.createInternalError();
            }
        }

        if (doTransfer) {
            PacketRecipeTransfer packet = new PacketRecipeTransfer(matchingItemsResult.matchingItems,
                    craftingSlotIndexes, inventorySlotIndexes, maxTransfer, false);

            JustEnoughItems.getProxy().sendPacketToServer(packet);
        }

        return null;
    }

}