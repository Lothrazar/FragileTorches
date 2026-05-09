package com.lothrazar.fragiletorches;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.world.entity.LivingEntity;

public class FragTorchEvent {

  private static final ResourceLocation TAGRL = ResourceLocation.parse(ModFragileTorches.TAGID);
  private static final TagKey<Block> TAGSTATE = BlockTags.create(TAGRL);

  // aka LivingTickEvent
  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Post event) {
    Entity ent = event.getEntity();
    if (!(ent instanceof LivingEntity)) {
      return;
    }
    // only players and living creatures can break things, not other entities
    if (ent instanceof Player p) {
      //i am a player, i can avoid this
      if (p.isCrouching()) {
        return;// ok // torches are safe from breaking as secret edge case for happiness
      }
    }
    if (TorchConfigManager.entityIsGentle(ent.getType())) {
      ModFragileTorches.LOGGER.debug(ent.getType() + " is gentle, will not break fragile torches ");
      return;
    }
    Level level = ent.level();
    if (level.random.nextDouble() > TorchConfigManager.DOUBLEVALUE.get()) {
      return;
    }
    BlockPos pos = ent.blockPosition();
    BlockState bs = level.getBlockState(pos);
    boolean breakable = bs.is(TAGSTATE); // the data tag

    if (!breakable && ent.getEyeHeight() >= 1) {
      //also check above at eye level
      pos = pos.above();//so go up one
      bs = level.getBlockState(pos);
      breakable = bs.is(TAGSTATE);
    }
    if (breakable) {
      //ok break the torch
      level.destroyBlock(pos, true);
      ModFragileTorches.LOGGER.debug(ent.getName() + " has broken a fragile torch at "+ pos);
    }
  }
}
