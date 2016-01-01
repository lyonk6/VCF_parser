package populationZygosityRetriever;

import java.util.ArrayList;
import java.util.HashMap;

public class PopulationSet 
{
	/**
	 * EUR = {CEU, FIN, GBR, TSI}
	 * ASN = {CHB, CHS, JPT}
	 * AFR = {ASW, LWK, YRI}
	 */
	private enum population {ASW, LWK, YRI, CHB, CHS, JPT, CEU, FIN, GBR, TSI};	 
	private enum superPopulation {AFR, ASN, EUR};
	private HashMap <String, Double> populationDAFRecord;
	private HashMap <String, Double> DeltaDAFs;
	private ArrayList<String> DeltaDAFsNames;
	/**
	 * 
	 * @param populationSet
	 * @param ancestralAlleleFlags
	 * @param locus
	 * @param writer 
	 */
	public PopulationSet(HashMap <String, Double> populationZygosityRecord)
	{					
		this.populationDAFRecord = populationZygosityRecord;
		
		//DeltaDAFs must hold a complete graph of 10 and a complete graph of 3.	
		DeltaDAFs = new HashMap <String, Double>(45 + 3);
		DeltaDAFsNames = new ArrayList<String>(45 + 3);
		
		recursivePopulationEvaluator(0);
		recursiveSuperPopulationEvaluator(0);
	}
	
	/**
	 * Here we recursively calculate the DeltaDAF of each population pair. It is not
	 * necessary to pass this method a new hashMap, we just pass it the new index. 
	 */
	private void recursivePopulationEvaluator(int i)
	{
		/* Loop through the population enum, then set the 
		 * DeltaDAF for this population pair
		 */
		if(i < 10)
		{	
			Double I = 0.0;
			Double J = 0.0;			
			String IJ = null;
			for(int j=i; j < 10; j++)
			{				
				IJ = population.values()[i].toString() + "-" + population.values()[j].toString();
				I = populationDAFRecord.get(population.values()[i].toString());
				J = populationDAFRecord.get(population.values()[j].toString());
				DeltaDAFs.put(IJ, Math.abs(I - J));
				DeltaDAFsNames.add(IJ);
			}
			recursivePopulationEvaluator(i+1);
		}
	}
	private void recursiveSuperPopulationEvaluator(int i)
	{
		/* Loop through the superPopulation enum, then set the 
		 * DeltaDAF for this superPopulation pair
		 */
		if(i < 3)
		{	
			Double I = 0.0;
			Double J = 0.0;	
			String IJ = null;
			for(int j=i; j < 3; j++)
			{
				IJ = superPopulation.values()[i].toString() + "-" + superPopulation.values()[j].toString();				
				I = populationDAFRecord.get(superPopulation.values()[i].toString());
				J = populationDAFRecord.get(superPopulation.values()[j].toString());
				DeltaDAFs.put(IJ, Math.abs(I - J));
				DeltaDAFsNames.add(IJ);
			}
			recursiveSuperPopulationEvaluator(i+1);
		}		
	}
	public HashMap <String, Double> getDeltaDAFs()
	{
		return DeltaDAFs;
	}
	public ArrayList<String> getDeltaDAFsNames()
	{
		return DeltaDAFsNames;
	}
}