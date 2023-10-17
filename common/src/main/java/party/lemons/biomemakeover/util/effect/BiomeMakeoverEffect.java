package party.lemons.biomemakeover.util.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.block.SucculentBlock;
import party.lemons.biomemakeover.block.SucculentType;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.util.RandomUtil;

public enum BiomeMakeoverEffect
{
    PLAY_CURSE_SOUND(BiomeMakeoverEffect::playCurseSound),
    BLOCK_ENDER_PARTICLES(BiomeMakeoverEffect::createEnderParticles),
    DESTROY_SUCCULENT(BiomeMakeoverEffect::succulentParticles)

    ;

    private static void succulentParticles(Level level, BlockPos pos, BlockState state)
    {
        if(level.isClientSide() && state.getBlock() instanceof SucculentBlock)
        {
            BiomeMakeoverClient.succulentBreakParticles(level, pos, state);
        }
    }

    private static void createEnderParticles(Level level, BlockPos pos, BlockState state)
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

    private static void playCurseSound(Level world, BlockPos pos, BlockState state)
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

    public void execute(Level world, BlockPos pos, BlockState state)
    {
        event.execute(world, pos, state);
    }



    public interface Event
    {
        void execute(Level world, BlockPos pos, BlockState state);
    }
}