import java.math.BigDecimal;
import java.math.BigInteger;

import utility.AllelicInfo;

/** 
 * 1. read in the population file.
 * 2. create an arraylist for each population
 *  
 */
public class DerivedAlleleFrequencyCalculator 
{	
	// hash table maps associative arrays.
	double[] all_people;
	public DerivedAlleleFrequencyCalculator(String populationFile)
	{
		try
		{
		all_people = new double[1092];
		}
		catch(Exception e)
		{			
			e.printStackTrace();
			System.exit(0);				
		}
	}
	public void updateAllPeople(AllelicInfo[] myArray)
	{
		//update the genotypes of all people.
	}
	static BigDecimal binomialProbability(int N, int K, double P)
	{
	/*  The binomial probability is given by:
	 *  p(x=k)= nchoosek * P^K * (1-P)^(n-k)
	 */				
		BigDecimal p = new BigDecimal(P);
		BigInteger nchoosek = binomial(N,K);
		BigDecimal pcarrotk = p.pow(K);
		// p less than one
		BigDecimal pLessOne = new BigDecimal(1.0).subtract(p);
		// p less than one to the power of k less than n
		BigDecimal pLessOneCarrotklessn = pLessOne.pow(N-K);
		// return nchoosek * pcarrotk * pLessOneCarrotklessn
		return (new BigDecimal(nchoosek)).multiply(pcarrotk).multiply(pLessOneCarrotklessn);		
	}
	// calculate the binomial coefficient
	static BigInteger binomial(final int N, final int K) 
	{
	    BigInteger ret = BigInteger.ONE;
	    for (int k = 0; k < K; k++) {
	        ret = ret.multiply(BigInteger.valueOf(N-k))
	                 .divide(BigInteger.valueOf(k+1));
	    }
	    return ret;
	}
}