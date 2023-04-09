package com.christofmeg.ic2xuumatter.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.core.item.ItemBattery;
import ic2.core.item.ItemBatterySU;
import ic2.core.item.ItemCrystalMemory;
import ic2.core.item.type.DustResourceType;
import ic2.core.ref.ItemName;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Utils {

    public static List<ItemStack> getValidBatteryList(boolean allowRedstoneDust, int machineTier) {
        List<ItemStack> validBatteryList = new ArrayList<>();
        List<Item> list = ImmutableList.copyOf(ForgeRegistries.ITEMS);
        list.stream().filter(item -> (item instanceof ItemBattery || item instanceof ItemBatterySU
                || accepts(item.getDefaultInstance(), allowRedstoneDust, machineTier))).forEach(item -> {
                    validBatteryList.add(item.getDefaultInstance());
                });
        validBatteryList.add(new ItemStack(ItemName.dust.getItemStack(DustResourceType.energium).getItem(), 1, 6));
        return validBatteryList;
    }

    public static boolean accepts(ItemStack stack, boolean allowRedstoneDust, int tier) {
        if ((stack == null) || (stack.getItem() == Items.REDSTONE && !allowRedstoneDust)) {
            return false;
        } else {
            return Info.itemInfo.getEnergyValue(stack) > 0.0D
                    || ElectricItem.manager.discharge(stack, Double.POSITIVE_INFINITY, tier, true, true, true) > 0.0D;
        }
    }

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
