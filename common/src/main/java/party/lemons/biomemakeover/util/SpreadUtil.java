package party.lemons.biomemakeover.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class SpreadUtil {

    public static <R> R sampleCircularOutwardlyFadingSpread(RandomSource random, float radius, VectorCallback2D<R> callback) {
        float theta = random.nextFloat() * Mth.TWO_PI;

        // Outwardly-fading probability.
        // Use `max(rand, rand)` or `sqrt(rand)` instead for a true uniform circular distribution.
        float distance = random.nextFloat() * radius;

        return callback.apply(
                Mth.cos(theta) * distance,
                Mth.sin(theta) * distance
        );
    }

    public static <R> R sampleEllipsoidalOutwardlyFadingSpread(RandomSource random, float radiusXZ, float radiusY, VectorCallback3D<R> callback) {
        float sphereY = random.nextFloat() * 2.0f - 1.0f;
        float sphereTheta = random.nextFloat() * Mth.TWO_PI;
        float sphereXZScale = Mth.sqrt(1.0f - sphereY * sphereY);

        // Outwardly-fading probability.
        // Remove the `Mth.square` for a true uniform spherical distribution.
        float radiusScale = Mth.square(Math.max(Math.max(random.nextFloat(), random.nextFloat()), random.nextFloat()));

        return callback.apply(
                radiusScale * sphereXZScale * radiusXZ * Mth.cos(sphereTheta),
                radiusScale * sphereY * radiusY,
                radiusScale * sphereXZScale * radiusXZ * Mth.sin(sphereTheta)
        );
    }

    public interface VectorCallback2D<R> {
        R apply(float dx, float dz);
    }

    public interface VectorCallback3D<R> {
        R apply(float dx, float dy, float dz);
    }

}
