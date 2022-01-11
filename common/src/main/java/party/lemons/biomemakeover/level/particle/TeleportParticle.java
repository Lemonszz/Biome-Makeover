package party.lemons.biomemakeover.level.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class TeleportParticle extends TextureSheetParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;
    protected double angle;
    protected double rotateSpeed;

    protected TeleportParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(clientLevel, d, e, f);
        this.xd = g;
        this.yd = h;
        this.zd = i;
        this.x = d;
        this.y = e;
        this.z = f;
        this.xStart = this.x;
        this.yStart = this.y;
        this.zStart = this.z;
        this.quadSize = 0.1f * (this.random.nextFloat() * 0.2f + 0.5f);
        float j = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = j * 0.9f;
        this.gCol = j * 0.3f;
        this.bCol = j;
        this.lifetime = (int) (Math.random() * 10.0) + 40;
        this.angle = Math.random() * 360D;
        this.rotateSpeed = Math.random() / 2;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float f) {
        float g = ((float) this.age + f) / (float) this.lifetime;
        g = 1.0f - g;
        g *= g;
        g = 1.0f - g;
        return this.quadSize * g;
    }

    @Override
    public int getLightColor(float f) {
        int i = super.getLightColor(f);
        float g = (float) this.age / (float) this.lifetime;
        g *= g;
        g *= g;
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((k += (int) (g * 15.0f * 16.0f)) > 240) {
            k = 240;
        }
        return j | k << 16;
    }

    @Override
    public void tick() {
        float f;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        } else {
            this.angle += rotateSpeed;
            double offset = age / 30F;
            x = (Math.cos(angle) / offset) + xStart;
            z = (Math.sin(angle) / offset) + zStart;
            y = yStart + Math.sin((age + rotateSpeed) / 10F) / 3F;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Provider
            implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            TeleportParticle portalParticle = new TeleportParticle(clientLevel, d, e, f, g, h, i);
            portalParticle.pickSprite(this.sprite);
            return portalParticle;
        }
    }
}