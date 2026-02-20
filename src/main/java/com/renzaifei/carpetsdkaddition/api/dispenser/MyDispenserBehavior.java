package com.renzaifei.carpetsdkaddition.api.dispenser;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;

public class MyDispenserBehavior implements DispenseItemBehavior {

    private final DispenseItemBehavior oldDispenserBehavior;

    public MyDispenserBehavior(DispenseItemBehavior oldDispenserBehavior) {
        this.oldDispenserBehavior = oldDispenserBehavior;
    }

    @Override
    public ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {
        return oldDispenserBehavior.dispense(blockSource, itemStack);
    }
}
