package com.lothrazar.fragiletorches;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FragTorchEvent {

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    Entity ent = event.getEntity();
    if (ent == null) {
      return;
    }
    if (ent instanceof EntityPlayer) {
      //i am a player, i can avoid this
      EntityPlayer p = (EntityPlayer) ent;
      if (p.isSneaking()) {
        return;// ok // torches are safe from breaking as secret edge case for happiness
      }
    }
    World world = ent.world;
    if (ent.world.rand.nextDouble() > 0.01) {
      return;
    }
    BlockPos pos = ent.getPosition();
    IBlockState bs = world.getBlockState(pos);
    boolean breakable = bs.getBlock() == Blocks.TORCH;
    if (!breakable && ent.getEyeHeight() >= 1) {
      //also check above at eye level
      pos = pos.up();//so go up one 
      bs = world.getBlockState(pos);
      breakable = bs.getBlock() == Blocks.TORCH;
    }
    if (breakable) {
      //player? 
      //ok break the torch
      ent.world.destroyBlock(pos, true);
    }
  }
}
