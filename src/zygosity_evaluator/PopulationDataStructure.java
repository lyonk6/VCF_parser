package zygosity_evaluator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/** Population Data Structure
 * This class is a data structure for the populations evaluated in
 * the phase 1 integrated call sets. The purpose of this class is 
 * to evaluate the fraction of the population that is heterozygous.
 * 
 */
public class PopulationDataStructure 
{

	public enum population {AFR,AMR,ASN,ASW,CEU,CHB,CHS,
		CLM,EUR,FIN,GBR,IBS,JPT,LWK,MXL,PUR,TSI,YRI,all};
	BufferedReader reader;
	private HashMap <population, ArrayList<Integer>> PopulationMap;
	
	PopulationDataStructure(String populationFile)
	{		
		//spin through the populations enum and create an
		//array list for each population in the population
		// map
		PopulationMap = new HashMap <population, ArrayList<Integer>>(19);		
		for(population p: population.values())
			PopulationMap.put(p, new ArrayList<Integer>());
		populatePopulationMap(populationFile);
	}
	public HashMap<population, ArrayList<Integer>> getPopulationMap()
	{
		return PopulationMap;
	}
	private void populatePopulationMap(String populationFile)
	{
		String line = "";
		int index = 0;
		try
		{
			reader = new BufferedReader(new FileReader(populationFile.trim()));
			String[] lineArray;
			line=reader.readLine();
			while(line != null)
			{
				if(index >= 1092)
					throw new Exception("The File is too big\n");
				
				lineArray = line.split("`");
				//get the population and add the index to it.				
				PopulationMap.get(population.valueOf(lineArray[2])).add(index);
				//get the super population and add the index to it.
				PopulationMap.get(population.valueOf(lineArray[3])).add(index);
				//the population 'all' gets incremented always.
				PopulationMap.get(population.all).add(index);				
				//rundate pid population superpopulation platform
				//exe: pfmMap.put(populations.valueOf(pmf.getPopulation()), pmf);
				line=reader.readLine();
				index++;
			}
		}
		catch(Exception e)
		{			
			e.printStackTrace();
			System.err.println(line);
			System.err.println("Index: " + index);
			System.exit(0);
		}
	}
	public static void main(String[]args)
	{
		String path = 
		"/Users/kennethlyon/Dropbox/eclipse_workspace/Multisample_Variant_Record";
		String file = "/workspace/zygosity_evaluator/output_2013_05_04_all_snp_people.txt";
		// This method can be used to test the population data structure object.
		PopulationDataStructure pds = new PopulationDataStructure(path + file);
		
		HashMap<population, ArrayList<Integer>> myMap = pds.getPopulationMap();
		String[] populationArray = {"AFR","AMR","ASN","ASW","CEU","CHB","CHS",
			"CLM","EUR","FIN","GBR","IBS","JPT","LWK","MXL","PUR","TSI","YRI","all"};
		
		for(String s: populationArray)
		{
			System.out.println("There exits a(n) " + s + " key: " + myMap.containsKey(population.valueOf(s)));
			System.out.println("The size of the array is: " + myMap.get(population.valueOf(s)).size());			
		}
		System.out.println("The precise values of the IBS population is: ");
		for(int i: myMap.get(population.valueOf("IBS")))
		// should be: 395  396  397  398  399  400  403  404  405  406  407  408  409  410  
		{
			System.out.print(i + "  ");
		}
		System.out.print("\n");
	}
}
