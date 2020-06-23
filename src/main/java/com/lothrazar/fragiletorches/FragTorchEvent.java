package com.lothrazar.fragiletorches;

import com.lothrazar.fragiletorches.config.ConfigManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.BlockTags.Wrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FragTorchEvent {

  private static final ResourceLocation TAGRL = new ResourceLocation(ModFragileTorches.TAGID);
  private static final Wrapper TAGSTATE = new BlockTags.Wrapper(TAGRL);

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    Entity ent = event.getEntity();
    if (ent == null) {
      return;
    }
    if (ConfigManager.entityIsGentle(ent.getType())) {
      return;
    }
    if (ent instanceof PlayerEntity) {
      //if config turns off player breaking, stop now
      //i am a player, i can avoid this
      PlayerEntity p = (PlayerEntity) ent;
      if (p.isCrouching()) {
        return;// ok // torches are safe from breaking as secret edge case for happiness
      }
    }
    World world = ent.world;
    if (ent.world.rand.nextDouble() > ConfigManager.DOUBLEVALUE.get()) {
      return;
    }
    BlockPos pos = ent.getPosition();
    BlockState bs = world.getBlockState(pos);
    boolean breakable = bs.isIn(TAGSTATE);
    if (!breakable && ent.getEyeHeight() >= 1) {
      //also check above at eye level
      pos = pos.up();//so go up one 
      bs = world.getBlockState(pos);
      breakable = bs.isIn(TAGSTATE);
    }
    if (breakable) {
      //player? 
      //ok break the torch
      ent.world.destroyBlock(pos, true);
    }
  }
}
