package party.lemons.biomemakeover.level.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.util.RandomUtil;

public class LightningSparkParticle extends TextureSheetParticle
{
    private final SpriteSet spriteSet;

    protected LightningSparkParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteSet) {
        super(world, x, y, z, 0.5D - RandomUtil.RANDOM.nextDouble(), velocityY, 0.5D - RandomUtil.RANDOM.nextDouble());

        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);

        this.yd *= 0.20000000298023224D;
        if(xd == 0.0D && zd == 0.0D)
        {
            this.xd *= 0.10000000149011612D;
            this.zd *= 0.10000000149011612D;
        }


        this.quadSize *= 0.75F;
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if(this.age++ >= this.lifetime)
        {
            this.remove();
        }else
        {
            this.setSpriteFromAge(this.spriteSet);
            this.yd += 0.004D;
            this.move(this.xd, this.yd, this.zd);
            if(this.y == this.yo)
            {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }

            this.xd *= 0.9599999785423279D;
            this.yd *= 0.9599999785423279D;
            this.zd *= 0.9599999785423279D;
            if(this.onGround)
            {
                this.xd *= 0.699999988079071D;
                this.zd *= 0.699999988079071D;
            }

        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteProvider;

        public Provider(SpriteSet spriteSet)
        {
            this.spriteProvider = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            LightningSparkParticle soulParticle = new LightningSparkParticle(clientLevel, d, e, f, g, h, i, this.spriteProvider);
            soulParticle.setAlpha(1.0F);
            return soulParticle;
        }
    }
}
