package populationZygosityRetriever;
/**
 * This class is a simple object used to calculate the DAF for 
 * an arbitrary population from the population_zygosity_table.
 * 
 * Note that the DAF when the ancetral allele matches the 
 * reference is 1/2 the heterozygous fraction + the homozygous 
 * fraction.
 * @author kennethlyon
 *
 */
public class PopulationDAF 
{
	// 
	private double DAF;
	public PopulationDAF(Double homozygousDAF, Double heterozygousDAF, Integer aaFlag)
	{
		try
		{
		if(aaFlag == 0)
			DAF = homozygousDAF + (heterozygousDAF/2) ;
		else if(aaFlag == 1)
			// I think this is how it works.
			DAF = 1.0 - (homozygousDAF + (heterozygousDAF/2));
		else
			throw new Exception("Illegal ancestral allele flag: " + aaFlag);	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	public double getDAF()
	{
		return DAF;
	}
	public static int compareTo()
	{
		return 0;
	}
}
