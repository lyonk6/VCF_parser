package info_table_builder;
/* This class calculates the Derived allele frequency and reports the 
 * number of records where the genotype score is invalid.
 */
import java.math.BigDecimal;
import java.math.MathContext;

import utility.AllelicInfo;
import utility.DetailedVariantRecord;
import utility.MultisampleVariantRecord;

public class StatsCalculator
{
	private DetailedVariantRecord dvr;
	private MultisampleVariantRecord msvr;
	private int invalidRecords; //the number of people with an invalid genotype score		
	private int totalTokens; // the total number of alleles (2*number of samples for autosomes)
	private int DerivedAlleleCount; // The number of derived alleles used to calculate the DAF	
	public StatsCalculator(MultisampleVariantRecord MRecord, DetailedVariantRecord DRecord)
	{
		this.dvr = DRecord;
		this.msvr = MRecord;
		initialize();
		String[] genotypeArray = null;
		for(AllelicInfo a: msvr.getAllelicInfoArray())
		{
			genotypeArray = msvr.getStringGenotype(true, a).split(",");
			for(String b: genotypeArray)
			{
				//we pass the parameter true b/c we want the imputed genotype.				
				totalTokens++;				
				if((dvr.get_AA().toUpperCase()).equals(b))
					DerivedAlleleCount++;
			}
			// count the number of valid variants and calculate
			// and average quality score.
			if(!a.isValidGenotype())
				invalidRecords++;
		}
	}
	private void initialize()
	{		
		totalTokens = 0;
		invalidRecords = 0;				
		DerivedAlleleCount = 0;		
	}
	public int getInvalidRecordsCount()
	{
		return invalidRecords;
	}
	public int getTotalTokens()
	{
		return totalTokens;
	}
	public int getDerivedAlleleCount()
	{
		return DerivedAlleleCount;
	}
	public String getDAF()
	/*
	 * The derived allele frequency is the ratio of alleles that do not match
	 * the ancestral allele, to those that do match the ancestral allele.
	 * If the AA is unknown, return the String "null".
	 */
	{
		if(dvr.get_AA().equals("."))
		{
			return "null";
		}
		else
		{			
			double count = (double) DerivedAlleleCount;
			double total = (double) totalTokens;
			double DAF = count/total;
			BigDecimal d = new BigDecimal(DAF, new MathContext(3));
			return d.toString();
		}
	}
}