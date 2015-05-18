package zygosity_evaluator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import utility.AllelicInfo;
import utility.DetailedVariantRecord;
import utility.MultisampleVariantRecord;
/**
 * This class is only intended to evaluate autosomal chromsomes. For each
 * populationn it counts the number of Heterozygous people and the number
 * of homozygous people that match the reference for each SNP. It then 
 * reports these numbers as well as their relative frequency 
 * (ie HeterozygousCount/1092). 
 */
public class ZygosityEvaluator extends PopulationDataStructure
{
	MultisampleVariantRecord msvr;
	DetailedVariantRecord dvr;	
    private String[] sampleNames;
	private HashMap <population, Integer> HeterozygousCount;
	private HashMap <population, Integer> HomozygousRefMatchCount;
	private int[] zygosity_of_people;
	BufferedReader reader;
	BufferedWriter writer;
	public ZygosityEvaluator(String chromosomeFile, String populationFile, String outFile)
	{
		super(populationFile);
		zygosity_of_people = new int[1092];		
		HomozygousRefMatchCount = new HashMap <population, Integer>(19);
		HeterozygousCount = new HashMap <population, Integer>(19);		
		initializeHashMaps();
		driver(chromosomeFile, outFile);
		
	}
	private void initializeHashMaps()
	{
		for(population p: population.values())
		{
			HomozygousRefMatchCount.put(p, 0);
			HeterozygousCount.put(p, 0);
		}
	}
	/** 
	 * This method simply reads the chromosome file and instantiates a 
	 * MultisampleVariantRecord object for each SNP in it. 
	 */
	private void driver(String chromosomeFile, String outFile)
	{
		try
		{
			reader = new BufferedReader(new FileReader(chromosomeFile), 67108864);
			writer = new BufferedWriter(new FileWriter(outFile), 67108864);
			String line = reader.readLine();
			while(line != null) 
		    {
			if(line.matches("#CHROM.*")) //We found the header line!
			    {				
				setSamples(line);
				break;
			    }
			else
			    line = reader.readLine();		     
		    }
		line = reader.readLine();
		while(line != null)
		    {			
			dvr  = new DetailedVariantRecord(line);
			if(!dvr.getIsIndel())
			    {
					msvr = new MultisampleVariantRecord(line, sampleNames);
					evaluateFractions();
					writeRecord();
					initializeHashMaps();
				}
			line = reader.readLine();
		    }
		writer.close();
		reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	private void writeRecord()
	{
		try
		{
			double temp_heterozygous_fraction = 0.0;
			double temp_homozygous_fraction = 0.0;
			ArrayList<Integer> tempPopulationMap;
			// Note that java guaratees that foreach loops and enum arrays are ordered.
			writer.write(dvr.getChrom() + "`" + dvr.getPos() + "`");			
			for(population p: population.values())
			{
				tempPopulationMap = this.getPopulationMap().get(p);
				temp_heterozygous_fraction = HeterozygousCount.get(p)/((double) tempPopulationMap.size());
				temp_homozygous_fraction = HomozygousRefMatchCount.get(p)/((double) tempPopulationMap.size());
				
				writer.write(HeterozygousCount.get(p)+ "`");
				writer.write(temp_heterozygous_fraction + "`");
				writer.write(HomozygousRefMatchCount.get(p) + "`");
				writer.write(temp_homozygous_fraction + "`");
			}
			writer.write(dvr.get_AncestralAlleleFlag() + "\n");
		}		
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);			
		}
	}
	/**
	 * This method evaluates the zygosities of a MultisampleVariantRecord
	 * object. It calls evaluateZygosity on each AllelicInfo object. Then
	 * it spins through the zygosity_of_people array and populates the 
	 * HashMaps: HomozygousRefMatchCount and HeterozygousCount.
	 */
	private void evaluateFractions()
	{
		AllelicInfo[] myAllelicInfoArray = msvr.getAllelicInfoArray();
		//ArrayList<Integer> tempPopulationMap;
		for(int i=0; i < 1092; i++)
		{
			try
			{
				zygosity_of_people[i] = evaluateZygosity(myAllelicInfoArray[i]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		for(population p: population.values())
		{
			// spin through pds and update HeterozygousCount and 
			// HomozygousRefMatchCount for each population in pds
			for(int j: this.getPopulationMap().get(p))
			{
				switch(zygosity_of_people[j])
				{
				case  0:
					HomozygousRefMatchCount.put(p, HomozygousRefMatchCount.get(p) + 1);					
					break;
				case  1:
					HeterozygousCount.put(p, HeterozygousCount.get(p) + 1);
					break;
				}
			}//*/
		}	
	}
	/********************************************************* 
	 * evaluateZygosity uses an AllelicInfo object and returns 
	 *  0 iff the person is homozygous match of the reference.
	 *  1 iff the person is heterozygous and 
	 * -1 otherwise. 
	 */
	private int evaluateZygosity(AllelicInfo a)
	{
		if(isHomozygousRefMatch(a))
			return  0;
		else if(isHeterozygous(a))
			return  1;
		else 
			return -1;
	}
	private boolean isHomozygousRefMatch(AllelicInfo a)
	{
		if(a.getImputedGenotype().equals("ref,ref"))
			return true;
		else
			return false;
	}
	private boolean isHeterozygous(AllelicInfo a)

	{
		String[] valArray = a.getImputedGenotype().split(",");
		try
		{
			if(valArray.length != 2)
				throw new Exception("Non-diploid allele detected!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		if(valArray[0].equals(valArray[1]))
			return false;
		else
			return true;			
	}
	
    private void setSamples(String line)
    {
	//System.out.println(line);
	String[] myArray = line.split("\t");
	sampleNames = new String[myArray.length - 9];
	for(int i = 9; i < myArray.length; i++)
	    {
			sampleNames[i-9] = myArray[i].trim();
	    }	
	}
}
