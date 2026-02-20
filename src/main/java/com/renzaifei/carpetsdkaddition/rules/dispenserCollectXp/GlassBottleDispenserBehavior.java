package com.renzaifei.carpetsdkaddition.rules.dispenserCollectXp;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.api.dispenser.MyFallibleItemDispenserBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GlassBottleDispenserBehavior extends MyFallibleItemDispenserBehavior {
    private final DefaultDispenseItemBehavior fallbackBehavior = new DefaultDispenseItemBehavior();

    public GlassBottleDispenserBehavior(DispenseItemBehavior oldDispenserBehavior) {
        super(oldDispenserBehavior);
    }

    public static void init(){
        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE,
                    new GlassBottleDispenserBehavior(DispenserBlock.DISPENSER_REGISTRY.get(Items.GLASS_BOTTLE)));
    }

    private ItemStack replaceItem(BlockSource pointer, ItemStack oldItem, ItemStack newItem){
        oldItem.shrink(1);
        if (oldItem.isEmpty()){
            return newItem.copy();
        }

        if (!pointer.blockEntity().insertItem(newItem.copy()).isEmpty()){
            this.fallbackBehavior.dispense(pointer,newItem.copy());
        }

        return oldItem;
    }

    @Override
    public ItemStack dispenseSilently(BlockSource pointer, ItemStack itemStack) {
        if (!CarpetSDKAdditionSettings.dispenserCollectXp){
            return itemStack;
        }
        BlockPos faceBlockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING));

        List<ExperienceOrb> xpEntityList = pointer.level().getEntitiesOfClass(ExperienceOrb.class,new AABB(faceBlockPos), Entity::isAlive);

        int currentXp = 0;

        for (ExperienceOrb xpEntity : xpEntityList){
            for (; xpEntity.count> 0 ;--xpEntity.count) {
                currentXp += xpEntity.getValue();

                if (xpEntity.count == 1){
                    xpEntity.discard();
                }
                if (currentXp >= 8){
                    setSuccess(true);
                    return this.replaceItem(pointer,itemStack,new ItemStack(Items.EXPERIENCE_BOTTLE));
                }
            }
        }
        return itemStack;
    }
}
