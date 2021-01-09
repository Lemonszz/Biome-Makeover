package party.lemons.biomemakeover.util;

import com.google.common.collect.Maps;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Grid<V>
{
	private HashMap<GridPosition, V> entries = Maps.newHashMap();
	private int minX, minY, minZ, maxX, maxY, maxZ;

	public Grid()
	{
		minX = 0;
		minY = 0;
		minZ = 0;
		maxX = 0;
		maxY = 0;
		maxZ = 0;
	}

	public void put(int x, int y, int z, V entry)
	{
		GridPosition pos = new GridPosition(x, y, z);
		entries.put(pos, entry);

		if(x < minX)
			minX = x;
		if(y < minY)
			minY = y;
		if(z < minZ)
			minZ = z;
		if(x > maxX)
			maxX = x;
		if(y > maxY)
			maxY = y;
		if(z > maxZ)
			maxZ = z;
	}

	public void put(BlockPos pos, V entry)
	{
		put(pos.getX(), pos.getY(), pos.getZ(), entry);
	}

	public boolean contains(int x, int y, int z)
	{
		return entries.containsKey(new GridPosition(x, y, z));
	}

	public boolean contains(BlockPos pos)
	{
		return contains(pos.getX(), pos.getY(), pos.getZ());
	}

	public V get(int x, int y, int z)
	{
		return entries.get(new GridPosition(x, y, z));
	}

	public int getMinX()
	{
		return minX;
	}

	public int getMinY()
	{
		return minY;
	}

	public int getMinZ()
	{
		return minZ;
	}

	public int getMaxX()
	{
		return maxX;
	}

	public int getMaxY()
	{
		return maxY;
	}

	public int getMaxZ()
	{
		return maxZ;
	}

	public V get(BlockPos pos)
	{
		return get(pos.getX(), pos.getY(), pos.getZ());
	}

	public Collection<V> getEntries()
	{
		return entries.values();
	}

	private class GridPosition
	{
		public final int x,y,z;

		private GridPosition(int x, int y, int z)
		{
			this.x = x; this.y = y; this.z = z;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			GridPosition that = (GridPosition) o;
			return x == that.x && y == that.y && z == that.z;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(x, y, z);
		}
	}
}
