package com.renzaifei.carpetsdkaddition.rules;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.api.dispenser.MyFallibleItemDispenserBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class dispenserCollectXp extends MyFallibleItemDispenserBehavior {

    public dispenserCollectXp(DispenseItemBehavior oldDispenserBehavior) {
        super(oldDispenserBehavior);
    }

    public static void init(){
        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE,
                    new dispenserCollectXp(DispenserBlock.DISPENSER_REGISTRY.get(Items.GLASS_BOTTLE)));
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
