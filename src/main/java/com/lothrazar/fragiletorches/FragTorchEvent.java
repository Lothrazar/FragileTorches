package com.lothrazar.fragiletorches;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FragTorchEvent {

  private static final ResourceLocation TAGRL = new ResourceLocation(ModFragileTorches.TAGID);
  private static final TagKey<Block> TAGSTATE = BlockTags.create(TAGRL);

  @SubscribeEvent
  public void onEntityUpdate(LivingTickEvent event) {
    Entity ent = event.getEntity();
    if (ent == null || TorchConfigManager.entityIsGentle(ent.getType())) {
      return;
    }
    if (ent instanceof Player p) {
      //i am a player, i can avoid this
      if (p.isCrouching()) {
        return;// ok // torches are safe from breaking as secret edge case for happiness
      }
    }
    Level level = ent.level();
    if (level.random.nextDouble() > TorchConfigManager.DOUBLEVALUE.get()) {
      return;
    }
    BlockPos pos = ent.blockPosition();
    BlockState bs = level.getBlockState(pos);
    boolean breakable = bs.is(TAGSTATE);
    if (!breakable && ent.getEyeHeight() >= 1) {
      //also check above at eye level
      pos = pos.above();//so go up one 
      bs = level.getBlockState(pos);
      breakable = bs.is(TAGSTATE);
    }
    if (breakable) {
      //ok break the torch
      level.destroyBlock(pos, true);
    }
  }
}
