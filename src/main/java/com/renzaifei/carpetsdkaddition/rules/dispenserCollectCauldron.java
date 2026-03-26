package com.renzaifei.carpetsdkaddition.rules;

import com.renzaifei.carpetsdkaddition.CarpetSDKAdditionSettings;
import com.renzaifei.carpetsdkaddition.api.dispenser.MyFallibleItemDispenserBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;


public class dispenserCollectCauldron extends MyFallibleItemDispenserBehavior {
    public dispenserCollectCauldron(DispenseItemBehavior oldDispenserBehavior) {
        super(oldDispenserBehavior);
    }

    private static final HashMap<Block,Item> FluidLists = new HashMap<Block,Item>(Map.of(
            Blocks.WATER_CAULDRON,Items.WATER_BUCKET,
            Blocks.LAVA_CAULDRON,Items.LAVA_BUCKET,
            Blocks.POWDER_SNOW_CAULDRON,Items.POWDER_SNOW_BUCKET
    ));

    public static void init(){
        DispenserBlock.registerBehavior(Items.BUCKET,
                new dispenserCollectCauldron(DispenserBlock.DISPENSER_REGISTRY.get(Items.BUCKET)));
    }

    private static ItemStack getBucket(BlockState faceBlockState,ItemStack stack){
        for (Block block : FluidLists.keySet()){
            if (faceBlockState.is(block)){
                return new ItemStack(FluidLists.get(block));
            }
        }
        return stack;
    }

    @Override
    public ItemStack dispenseSilently(BlockSource pointer, ItemStack stack) {
        if (!CarpetSDKAdditionSettings.dispenserCollectCauldron) return stack;
        ServerLevel serverLevel = pointer.level();
        BlockPos faceBlockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING));
        BlockState faceBlockState = serverLevel.getBlockState(faceBlockPos);
        Block faceBlock = faceBlockState.getBlock();
        if (!faceBlockState.is(BlockTags.CAULDRONS) || faceBlockState.is(Blocks.CAULDRON))return stack;
        if (!((AbstractCauldronBlock)faceBlock).isFull(faceBlockState)) return stack;
        ItemStack resultStack = getBucket(faceBlockState,stack);
        serverLevel.setBlock(faceBlockPos,Blocks.CAULDRON.defaultBlockState(),Block.UPDATE_ALL);
        this.setSuccess(true);
        return this.replaceItem(pointer,stack,resultStack);
    }
}
