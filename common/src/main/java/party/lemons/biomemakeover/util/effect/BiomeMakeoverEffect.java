package party.lemons.biomemakeover.util.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.sound.AltarCursingSoundInstance;

import java.util.Random;

public enum BiomeMakeoverEffect
{
    PLAY_CURSE_SOUND(BiomeMakeoverEffect::playCurseSound),
    BLOCK_ENDER_PARTICLES(BiomeMakeoverEffect::createEnderParticles);

    private static void createEnderParticles(Level level, BlockPos pos)
    {
        RandomSource random = RandomUtil.RANDOM;
        for(int i = 0; i < 5; ++i) {
            int xOffset = random.nextInt(2) * 2 - 1;
            int zOffset = random.nextInt(2) * 2 - 1;
            double x = (double)pos.getX() + 0.5D + 0.25D * (double)xOffset;
            double y = (float)pos.getY() + random.nextFloat();
            double z = (double)pos.getZ() + 0.5D + 0.25D * (double)zOffset;
            double vX = random.nextFloat() * (float)xOffset;
            double vY = ((double)random.nextFloat() - 0.5D) * 0.125D;
            double vZ = random.nextFloat() * (float)zOffset;
            level.addParticle(ParticleTypes.PORTAL, x, y, z, vX, vY, vZ);
        }
    }

    private static void playCurseSound(Level world, BlockPos pos)
    {
        BlockEntity be = world.getBlockEntity(pos);
        if(be instanceof AltarBlockEntity altar && world.isClientSide())
            BiomeMakeoverClient.curseSound(altar);

    }

    private final Event event;

    private BiomeMakeoverEffect(Event event)
    {
        this.event = event;
    }

    public void execute(Level world, BlockPos pos)
    {
        event.execute(world, pos);
    }



    public interface Event
    {
        void execute(Level world, BlockPos pos);
    }
}