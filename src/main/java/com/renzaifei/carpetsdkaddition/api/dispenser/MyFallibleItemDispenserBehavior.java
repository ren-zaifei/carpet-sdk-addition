package com.renzaifei.carpetsdkaddition.api.dispenser;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

public abstract class MyFallibleItemDispenserBehavior extends MyDispenserBehavior{
    private boolean success = false;

    public MyFallibleItemDispenserBehavior(DispenseItemBehavior oldDispenserBehavior) {
        super(oldDispenserBehavior);
    }

    @Override
    public ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {
        setSuccess(false);
        ItemStack itemStack2 = this.dispenseSilently(blockSource,itemStack);
        if (!isSuccess()){
            return super.dispense(blockSource, itemStack);
        }
        this.playSound(blockSource);
        this.spawnParticles(blockSource,blockSource.state().getValue(DispenserBlock.FACING));
        return itemStack2;
    }

    public ItemStack dispenseSilently(BlockSource pointer, ItemStack stack) {
        Direction direction = pointer.state().getValue(DispenserBlock.FACING);
        Position position = DispenserBlock.getDispensePosition(pointer);
        ItemStack itemStack = stack.split(1);
        DefaultDispenseItemBehavior.spawnItem(pointer.level(), itemStack, 6, direction, position);
        return stack;
    }

    protected void playSound(BlockSource pointer) {
        pointer.level().levelEvent(this.isSuccess() ? 1000 : 1001, pointer.pos(), 0);
    }

    protected void spawnParticles(BlockSource pointer, Direction side) {
        pointer.level().levelEvent(2000, pointer.pos(), side.get3DDataValue());
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }
}
