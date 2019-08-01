package com.lothrazar.fragiletorches;
import com.lothrazar.fragiletorches.setup.ClientProxy;
import com.lothrazar.fragiletorches.setup.IProxy;
import com.lothrazar.fragiletorches.setup.ServerProxy;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModFragileTorches.MODID)
public class ModFragileTorches {

  public static final String MODID = "fragiletorches";
  private String certificateFingerprint = "@FINGERPRINT@";
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
  private static final Logger LOGGER = LogManager.getLogger();

  public ModFragileTorches() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(this);
//    ConfigHandler.loadConfig(ConfigHandler.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
  }

  private void setup(final FMLCommonSetupEvent event) {
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    Entity ent = event.getEntity();
    if (ent == null) {
      return;
    }
    World world = ent.world;
    if (ent.world.rand.nextDouble() > 0.01) {
      return;
    }
    if (world.getBlockState(ent.getPosition()).getBlock() == Blocks.TORCH) {
      if (ent instanceof PlayerEntity) {
        //i am a player, i can avoid this
        PlayerEntity p = (PlayerEntity) ent;
        if (p.isSneaking()) {
          return;// ok // torches are safe from breaking as secret edge case for happiness
        }
      }
      //ok break the torch
      this.tryBreak(ent);
    }
  }

  private void tryBreak(Entity living) {
    living.world.destroyBlock(living.getPosition(), true);
  }
}
