package party.lemons.biomemakeover.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.phys.AABB;

public final class NBTUtil
{
    public static String BLOCKPOS_TAG = "BMBlockPos";
    public static String BOX_TAG = "BMBox";

    public static void writeBlockPos(BlockPos pos, CompoundTag tag)
    {
        tag.put(BLOCKPOS_TAG, NbtUtils.writeBlockPos(pos));
    }

    public static BlockPos readBlockPos(CompoundTag tag)
    {
        return NbtUtils.readBlockPos(tag.getCompound(BLOCKPOS_TAG));
    }

    public static void writeBox(AABB box, CompoundTag tag)
    {
        CompoundTag boxTag = new CompoundTag();
        boxTag.putDouble("MinX", box.minX);
        boxTag.putDouble("MinY", box.minY);
        boxTag.putDouble("MinZ", box.minZ);
        boxTag.putDouble("MaxX", box.maxX);
        boxTag.putDouble("MaxY", box.maxY);
        boxTag.putDouble("MaxZ", box.maxZ);

        tag.put(BOX_TAG, boxTag);
    }

    public static AABB readBox(CompoundTag tag)
    {
        CompoundTag boxTag = tag.getCompound(BOX_TAG);
        return new AABB(
                boxTag.getDouble("MinX"),
                boxTag.getDouble("MinY"),
                boxTag.getDouble("MinZ"),
                boxTag.getDouble("MaxX"),
                boxTag.getDouble("MaxY"),
                boxTag.getDouble("MaxZ")
        );
    }


    private NBTUtil(){}
}