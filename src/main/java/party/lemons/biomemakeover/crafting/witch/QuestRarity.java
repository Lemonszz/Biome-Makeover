package party.lemons.biomemakeover.crafting.witch;

public enum QuestRarity
{
	COMMON(0, new int[]{110, 150, 10, 1}),
	UNCOMMON(8, new int[]{90, 120, 75, 10}),
	RARE(15, new int[]{0, 0, 100, 50}),
	EPIC(30, new int[]{0, 0, 0, 100});

	private final int requiredPoints;
	private final int[] rewardWeights;

	QuestRarity(int requiredPoints, int[] rewardWeights)
	{
		this.requiredPoints = requiredPoints;
		this.rewardWeights  = rewardWeights;
	}

	public static QuestRarity getRarityFromPoints(float points)
	{
		for(int i = values().length - 1; i >= 0; i--)
		{
			if(points >= values()[i].requiredPoints) return values()[i];
		}
		return COMMON;
	}
}