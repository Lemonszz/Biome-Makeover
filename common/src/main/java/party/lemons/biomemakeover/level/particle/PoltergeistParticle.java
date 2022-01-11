package party.lemons.biomemakeover.level.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class PoltergeistParticle extends RisingParticle {

    private final SpriteSet spriteSet;

    protected PoltergeistParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, h, i);

        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);
        scale(1.5F);
    }

    @Override
    public void tick() {
        super.tick();

        if(!removed)
            setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            PoltergeistParticle soulParticle = new PoltergeistParticle(clientLevel, d, e, f, g, h, i, this.spriteProvider);
            soulParticle.setAlpha(1.0F);
            return soulParticle;
        }
    }
}
