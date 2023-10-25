package party.lemons.biomemakeover.level.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleTerrainParticle extends TextureSheetParticle {
    private final BlockPos pos;
    private final float uvU;
    private final float uvV;

    public SimpleTerrainParticle(ClientLevel level, double x, double y, double z, double vX, double vY, double vZ, BlockState state) {
        this(level, x, y, z, vX, vY, vZ, state, BlockPos.containing(x, y, z));
    }

    public SimpleTerrainParticle(ClientLevel level, double x, double y, double z, double vX, double vY, double vZ, BlockState state, BlockPos pos) {
        super(level, x, y, z, vX, vY, vZ);
        this.pos = pos;
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;

        this.gravity = 0.25F;

        //TODO: support forge/taniwha tinted particles

        this.quadSize /= 2.0F;
        this.uvU = this.random.nextFloat() * 3.0F;
        this.uvV = this.random.nextFloat() * 3.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uvU + 1.0F) / 4.0F * 16.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uvU / 4.0F * 16.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.uvV / 4.0F * 16.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.uvV + 1.0F) / 4.0F * 16.0F);
    }

    @Override
    public int getLightColor(float f) {
        int i = super.getLightColor(f);
        return i == 0 && this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : i;
    }



    public static class Provider implements ParticleProvider<BlockParticleOption> {
        public Particle createParticle(BlockParticleOption option, ClientLevel level, double x, double y, double z, double vX, double vY, double vZ) {
            BlockState state = option.getState();
            return !state.isAir() && !state.is(Blocks.MOVING_PISTON)
                    ? new SimpleTerrainParticle(level, x, y, z, vX, vY, vZ, state)
                    : null;
        }
    }
}