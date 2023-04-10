package com.christofmeg.ic2xuumatter.integration.jei.handler;

import javax.annotation.Nonnull;

import com.christofmeg.ic2xuumatter.IC2XUUMatter;
import com.christofmeg.ic2xuumatter.integration.jei.category.ReplicatorCategory;

import ic2.core.block.machine.container.ContainerReplicator;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class TransferHandlerReplicator implements IRecipeTransferHandler<ContainerReplicator> {

    @Override
    public Class<ContainerReplicator> getContainerClass() {
        return ContainerReplicator.class;
    }

    @Nonnull
    public String getRecipeCategoryUid() {
        return ReplicatorCategory.UID;
    }

    @Override
    public IRecipeTransferError transferRecipe(ContainerReplicator container, IRecipeLayout recipeLayout,
            EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        if (doTransfer) {
            for (Slot slot : container.inventorySlots) {
                if (slot.slotNumber > 35) {
                    IC2XUUMatter.log.info(I18n.format(slot.inventory.getName().toString()));
                    IC2XUUMatter.log.info(slot.slotNumber);
                    IC2XUUMatter.log.info("------------------");
                }
            }
        }
        return null;
    }

}