/*package com.tacz.guns.events;




import com.tacz.guns.blocks.WallDoor;
import com.tacz.guns.blocks.abstracts.StructureBlock;
import com.tacz.guns.config.common.WallConfig;
import com.tacz.guns.world.DamageBlockSaveData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.tacz.guns.config.common.WallConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TACZEventHandler {
    private final Class<?> ammoHitBlockEventClass;
    private final Class<?> entityKineticBulletClass;

    private final Method getLevel;
    private final Method getHitResult;
    private final Method getAmmo;
    private final Method getDamage;

    private final int destructionMode = WallConfig.destructionMode;
    private final List<? extends String> whiteList = WallConfig.destructionWhiteList;


    public TACZEventHandler(Class<?> ammoHitBlockEventClass, Class<?> entityKineticBulletClass) throws NoSuchMethodException {
        this.ammoHitBlockEventClass = ammoHitBlockEventClass;
        this.entityKineticBulletClass = entityKineticBulletClass;

        getLevel = ammoHitBlockEventClass.getMethod("getLevel");
        getHitResult = ammoHitBlockEventClass.getMethod("getHitResult");
        getAmmo = ammoHitBlockEventClass.getMethod("getAmmo");
        getDamage = entityKineticBulletClass.getMethod("getDamage", Vec3.class);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event){
        if(!(event.getLevel() instanceof ServerLevel)) return;
        ServerLevel world = (ServerLevel) event.getLevel();
        DamageBlockSaveData damageBlockSaveData = DamageBlockSaveData.get(world);

        damageBlockSaveData.removeBlock(event.getPos());
    }

    @SubscribeEvent
    public void TACZHitBlockEventHandler(Event event) throws InvocationTargetException, IllegalAccessException {
       // if(destructionMode == 0){
         //   return;
       // }
        if(ammoHitBlockEventClass.isInstance(event)) {
            Level level = (Level) getLevel.invoke(event);
            BlockHitResult blockHitResult = (BlockHitResult)getHitResult.invoke(event);
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState blockState = level.getBlockState(pos);

            Object ammo = entityKineticBulletClass.cast(getAmmo.invoke(event));
            float damage = (float)getDamage.invoke(ammo, blockHitResult.getLocation());

            DamageBlockSaveData damageBlockSaveData = DamageBlockSaveData.get(level);

            if(blockState.getBlock() instanceof StructureBlock structureBlock){
                BlockPos masterPos = structureBlock.getMasterPos(level, pos, blockState);
                if (damageBlockSaveData.damageBlock(level, masterPos, (int)damage)<=0){
                    structureBlock.playerWillDestroy(level, masterPos, blockState, null);
                }
                return;
            }

        /*    if (destructionMode == 2) {
                if(whiteList.contains(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).toString())){
                    if (damageBlockSaveData.damageBlock(level, pos, (int)damage)<=0){
                        level.destroyBlock(pos, true);
                    }
                }
            }

            else if(destructionMode == 3){
                if (damageBlockSaveData.damageBlock(level, pos, (int)damage)<=0){
                    level.destroyBlock(pos, true);
                }
            }
      //  }
    }
}*/
