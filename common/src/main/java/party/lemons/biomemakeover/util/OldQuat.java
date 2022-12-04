package party.lemons.biomemakeover.util;

import net.minecraft.util.Mth;
import org.joml.Vector3f;

public final class OldQuat {
	public static final OldQuat ONE = new OldQuat(0.0F, 0.0F, 0.0F, 1.0F);
	private float i;
	private float j;
	private float k;
	private float r;

	public OldQuat(float f, float g, float h, float i) {
		this.i = f;
		this.j = g;
		this.k = h;
		this.r = i;
	}

	public OldQuat(Vector3f vector3f, float f, boolean bl) {
		if (bl) {
			f *= (float) (Math.PI / 180.0);
		}

		float g = sin(f / 2.0F);
		this.i = vector3f.x() * g;
		this.j = vector3f.y() * g;
		this.k = vector3f.z() * g;
		this.r = cos(f / 2.0F);
	}

	public OldQuat(float f, float g, float h, boolean bl) {
		if (bl) {
			f *= (float) (Math.PI / 180.0);
			g *= (float) (Math.PI / 180.0);
			h *= (float) (Math.PI / 180.0);
		}

		float i = sin(0.5F * f);
		float j = cos(0.5F * f);
		float k = sin(0.5F * g);
		float l = cos(0.5F * g);
		float m = sin(0.5F * h);
		float n = cos(0.5F * h);
		this.i = i * l * n + j * k * m;
		this.j = j * k * n - i * l * m;
		this.k = i * k * n + j * l * m;
		this.r = j * l * n - i * k * m;
	}

	public OldQuat(OldQuat OldQuat) {
		this.i = OldQuat.i;
		this.j = OldQuat.j;
		this.k = OldQuat.k;
		this.r = OldQuat.r;
	}

	public static OldQuat fromYXZ(float f, float g, float h) {
		OldQuat OldQuat = ONE.copy();
		OldQuat.mul(new OldQuat(0.0F, (float)Math.sin((double)(f / 2.0F)), 0.0F, (float)Math.cos((double)(f / 2.0F))));
		OldQuat.mul(new OldQuat((float)Math.sin((double)(g / 2.0F)), 0.0F, 0.0F, (float)Math.cos((double)(g / 2.0F))));
		OldQuat.mul(new OldQuat(0.0F, 0.0F, (float)Math.sin((double)(h / 2.0F)), (float)Math.cos((double)(h / 2.0F))));
		return OldQuat;
	}

	public static OldQuat fromXYZDegrees(Vector3f vector3f) {
		return fromXYZ((float)Math.toRadians((double)vector3f.x()), (float)Math.toRadians((double)vector3f.y()), (float)Math.toRadians((double)vector3f.z()));
	}

	public static OldQuat fromXYZ(Vector3f vector3f) {
		return fromXYZ(vector3f.x(), vector3f.y(), vector3f.z());
	}

	public static OldQuat fromXYZ(float f, float g, float h) {
		OldQuat OldQuat = ONE.copy();
		OldQuat.mul(new OldQuat((float)Math.sin((double)(f / 2.0F)), 0.0F, 0.0F, (float)Math.cos((double)(f / 2.0F))));
		OldQuat.mul(new OldQuat(0.0F, (float)Math.sin((double)(g / 2.0F)), 0.0F, (float)Math.cos((double)(g / 2.0F))));
		OldQuat.mul(new OldQuat(0.0F, 0.0F, (float)Math.sin((double)(h / 2.0F)), (float)Math.cos((double)(h / 2.0F))));
		return OldQuat;
	}

	public Vector3f toXYZ() {
		float f = this.r() * this.r();
		float g = this.i() * this.i();
		float h = this.j() * this.j();
		float i = this.k() * this.k();
		float j = f + g + h + i;
		float k = 2.0F * this.r() * this.i() - 2.0F * this.j() * this.k();
		float l = (float)Math.asin((double)(k / j));
		return Math.abs(k) > 0.999F * j
				? new Vector3f(2.0F * (float)Math.atan2((double)this.i(), (double)this.r()), l, 0.0F)
				: new Vector3f(
				(float)Math.atan2((double)(2.0F * this.j() * this.k() + 2.0F * this.i() * this.r()), (double)(f - g - h + i)),
				l,
				(float)Math.atan2((double)(2.0F * this.i() * this.j() + 2.0F * this.r() * this.k()), (double)(f + g - h - i))
		);
	}

	public Vector3f toXYZDegrees() {
		Vector3f vector3f = this.toXYZ();
		return new Vector3f((float)Math.toDegrees((double)vector3f.x()), (float)Math.toDegrees((double)vector3f.y()), (float)Math.toDegrees((double)vector3f.z()));
	}

	public Vector3f toYXZ() {
		float f = this.r() * this.r();
		float g = this.i() * this.i();
		float h = this.j() * this.j();
		float i = this.k() * this.k();
		float j = f + g + h + i;
		float k = 2.0F * this.r() * this.i() - 2.0F * this.j() * this.k();
		float l = (float)Math.asin((double)(k / j));
		return Math.abs(k) > 0.999F * j
				? new Vector3f(l, 2.0F * (float)Math.atan2((double)this.j(), (double)this.r()), 0.0F)
				: new Vector3f(
				l,
				(float)Math.atan2((double)(2.0F * this.i() * this.k() + 2.0F * this.j() * this.r()), (double)(f - g - h + i)),
				(float)Math.atan2((double)(2.0F * this.i() * this.j() + 2.0F * this.r() * this.k()), (double)(f - g + h - i))
		);
	}

	public Vector3f toYXZDegrees() {
		Vector3f vector3f = this.toYXZ();
		return new Vector3f((float)Math.toDegrees((double)vector3f.x()), (float)Math.toDegrees((double)vector3f.y()), (float)Math.toDegrees((double)vector3f.z()));
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object != null && this.getClass() == object.getClass()) {
			OldQuat OldQuat = (OldQuat)object;
			if (Float.compare(OldQuat.i, this.i) != 0) {
				return false;
			} else if (Float.compare(OldQuat.j, this.j) != 0) {
				return false;
			} else if (Float.compare(OldQuat.k, this.k) != 0) {
				return false;
			} else {
				return Float.compare(OldQuat.r, this.r) == 0;
			}
		} else {
			return false;
		}
	}

	public int hashCode() {
		int i = Float.floatToIntBits(this.i);
		i = 31 * i + Float.floatToIntBits(this.j);
		i = 31 * i + Float.floatToIntBits(this.k);
		return 31 * i + Float.floatToIntBits(this.r);
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("OldQuat[").append(this.r()).append(" + ");
		stringBuilder.append(this.i()).append("i + ");
		stringBuilder.append(this.j()).append("j + ");
		stringBuilder.append(this.k()).append("k]");
		return stringBuilder.toString();
	}

	public float i() {
		return this.i;
	}

	public float j() {
		return this.j;
	}

	public float k() {
		return this.k;
	}

	public float r() {
		return this.r;
	}

	public void mul(OldQuat OldQuat) {
		float f = this.i();
		float g = this.j();
		float h = this.k();
		float i = this.r();
		float j = OldQuat.i();
		float k = OldQuat.j();
		float l = OldQuat.k();
		float m = OldQuat.r();
		this.i = i * j + f * m + g * l - h * k;
		this.j = i * k - f * l + g * m + h * j;
		this.k = i * l + f * k - g * j + h * m;
		this.r = i * m - f * j - g * k - h * l;
	}

	public void mul(float f) {
		this.i *= f;
		this.j *= f;
		this.k *= f;
		this.r *= f;
	}

	public void conj() {
		this.i = -this.i;
		this.j = -this.j;
		this.k = -this.k;
	}

	public void set(float f, float g, float h, float i) {
		this.i = f;
		this.j = g;
		this.k = h;
		this.r = i;
	}

	private static float cos(float f) {
		return (float)Math.cos((double)f);
	}

	private static float sin(float f) {
		return (float)Math.sin((double)f);
	}

	public void normalize() {
		float f = this.i() * this.i() + this.j() * this.j() + this.k() * this.k() + this.r() * this.r();
		if (f > 1.0E-6F) {
			float g = Mth.fastInvSqrt(f);
			this.i *= g;
			this.j *= g;
			this.k *= g;
			this.r *= g;
		} else {
			this.i = 0.0F;
			this.j = 0.0F;
			this.k = 0.0F;
			this.r = 0.0F;
		}
	}

	public void slerp(OldQuat OldQuat, float f) {
		throw new UnsupportedOperationException();
	}

	public OldQuat copy() {
		return new OldQuat(this);
	}
}
