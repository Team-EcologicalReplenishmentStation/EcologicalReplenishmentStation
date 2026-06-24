package cn.aurorian.ers.particle;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FallingBloodParticle extends TextureSheetParticle {

    FallingBloodParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
        this.quadSize *= 2.4f;
        int $$9 = (int) (32.0 / (Math.random() * 0.8 + 0.2));
        this.lifetime = (int) Math.max((float) $$9 * 0.9F, 1.0F);
        this.roll = (float) Math.random() * 6.2831855F;
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public float getQuadSize(float pScaleFactor) {
        return this.quadSize * Mth.clamp(((float) this.age + pScaleFactor) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.oRoll = this.roll;
            if (this.onGround) {
                this.oRoll = this.roll = 0.0F;
            }

            this.move(this.xd, this.yd, this.zd);
            this.yd -= 0.01000000026077032;
            this.yd = Math.max(this.yd, -0.34000000059604645);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprites) {
            this.sprite = pSprites;
        }

        @Nullable
        public Particle createParticle(
                @NotNull SimpleParticleType pType,
                @NotNull ClientLevel pLevel,
                double pX,
                double pY,
                double pZ,
                double pXSpeed,
                double pYSpeed,
                double pZSpeed) {
            FallingBloodParticle particle = new FallingBloodParticle(pLevel, pX, pY, pZ);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}
