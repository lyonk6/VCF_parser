import java.math.BigDecimal;
import java.math.MathContext;

import utility.AllelicInfo;
import utility.DetailedVariantRecord;
import utility.MultisampleVariantRecord;

/* This class is used to calculate the DAF for a set of allelicInfo objects.
 * It works by using an array of index values that specify which allelicInfo
 * records from the multiSampleVariantRecord class are within a population. 
 *
 * Note that a dot in the aa field indicates that there was no alignment, so 
 * the ancestral alignment is unknown. Also, if the ancestral alignment does
 * not match the reference or the variant, the DAF will be 100%.   
 * 
 */
public class PopulationDaf
{
	private DetailedVariantRecord dvr;
	private MultisampleVariantRecord msvr;
	private int[] population_index;
	private int derivedAlleleCount;
	private double daf;
	private double totalTokens;
	PopulationDaf(DetailedVariantRecord dvr, MultisampleVariantRecord msvr, int[] population_index)
	{
		this.msvr = msvr;		
		this.dvr = dvr;
		this.population_index = population_index;
		this.derivedAlleleCount = 0;
		this.totalTokens = 0;
		calculateDAF();
	}
	private void calculateDAF()
	{		
		for(int a: population_index)
			{
				//we pass the index value and the allelicInfoArray from 
				//msvr to the next method
				updateStatistics(a, msvr.getAllelicInfoArray());				
			}		
	}
	private void updateStatistics(int i, AllelicInfo[] a)
	{
		// Once we get the genotype value (ie var1, var2), we must 
		// then convert it back to the actual genotype. (ie A,G). 
		// Then increment totalTokens and derivedAlleleCount.
		String[] genotypeArray = null;
		genotypeArray = msvr.getStringGenotype(true, a[i]).split(",");
		for(String b: genotypeArray)
		{
			//we pass the parameter true b/c we want the imputed genotype.				
			totalTokens++;				
			if(dvr.get_AA().equals(b))
				derivedAlleleCount++;
		}
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
			double count = (double) derivedAlleleCount;
			double total = (double) totalTokens;
			double DAF = count/total;
			BigDecimal d = new BigDecimal(DAF, new MathContext(3));
			return d.toString();	
		}	
	}
}