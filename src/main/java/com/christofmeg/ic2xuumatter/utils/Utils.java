package com.christofmeg.ic2xuumatter.utils;

import ic2.core.item.ItemCrystalMemory;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Utils {

    public static ItemStack getCrystalMemory(ItemStack stack) {
        final ItemStack crystalMemory = ItemName.crystal_memory.getItemStack();
        ((ItemCrystalMemory) crystalMemory.getItem()).writecontentsTag(crystalMemory, stack);
        return crystalMemory;
    }

    /**
     * https://github.com/ZelGimi/industrialupgrade/blob/5dbe976428ef0a6c0f7b9eefa9eaea80ea9d4880/src/main/java/com/denfop/utils/ModUtils.java#L65-L73
     */
    public static ItemStack getCellFromFluid(String name) {
        final ItemStack cell = ItemName.fluid_cell.getItemStack().copy();
        final NBTTagCompound nbt = nbt(cell);
        final NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setString("FluidName", name);
        nbt1.setInteger("Amount", 1000);
        nbt.setTag("Fluid", nbt1);
        return cell;
    }

    /**
     * https://github.com/ZelGimi/industrialupgrade/blob/5dbe976428ef0a6c0f7b9eefa9eaea80ea9d4880/src/main/java/com/denfop/utils/ModUtils.java#L386-L396
     */
    public static NBTTagCompound nbt(ItemStack stack) {
        if (stack.isEmpty()) {
            return new NBTTagCompound();
        }
        NBTTagCompound NBTTagCompound = stack.getTagCompound();
        if (NBTTagCompound == null) {
            NBTTagCompound = new NBTTagCompound();
        }
        stack.setTagCompound(NBTTagCompound);
        return NBTTagCompound;
    }

}
