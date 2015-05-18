package utility;
/* This class interprets the "Genotype data" associated with the 
 * variant calls downloads from phase1 of 1000genomes project. It provides
 * sequence and imputation engine allele predictions with quality scores for
 * both. 
 * Generally, only imputed data should be used from this class.
 * This class will only work for genotype fields with the format: GT:DS:GL. 
 * This class DOES accommodate triploid alleles.
 * 
 Example vcf record: note that the format field defines the genotype field(s)
 #CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	HG00096
 22	16050408	rs149201999	T	C	100	PASS	<INFO>	GT:DS:GL	0|0:0.050:-0.03,-1.17,-5.00
 *
 */

import java.lang.Exception;
import java.lang.Character;
import java.lang.String;
import java.math.BigDecimal;
public class AllelicInfo
{
	private static final String ref = "ref";
    private static final String[] alt = {"var1", "var2" , "var3" , "var4" , "var5" , "var6"};    
    private String name; // ie HG00096
    private String genotype; //ie: 0|0:0.050:-0.03,-1.17,-5.00
    private String imputedGenotype;   // The imputed genotype 
    private double imputedScore;      // The genotype score given for the imputed genotype. 
    /**** genotype data ie: 0|0:0.050:-0.03,-1.17,-5.00 ****/
    private String GT;      // The predicted genotype 
    private double DS;      // The genotypic dosage 0 <= DS <= 2
    private double[] GL;    // array of genotype scores; The genotype likelihoods subfield "-0.03,-1.17,-5.00".
    private boolean isValidGenotype; //isValidGenotype marks invalid genotype calls as false
    private boolean isVariant; //isVariant marks a person as true if they have at least one variant allele from the reference.
    /*****************************************************************************/
    /* The single value most likely to exist as an allele of any ploidy is called 
     * the alternateGenotype. The score for aforementioned value is called the 
     * alternate score. Both of these values are set by setAlternateGenotype()
     * 
     */
   private boolean isImputedVariant;     
   /******************************************************************************/
    public AllelicInfo(String name, String genotype)
    {
	this.isValidGenotype = true;
	this.isVariant = true;
	this.genotype = genotype;
	this.name = name;
	try
	    {		
		String[] myArray = genotype.split(":");
		this.GT = myArray[0];		
		setDossage(myArray[1]);
		setGL(myArray[2]);
		setImputedGenotype(GT.toCharArray());
		this.normalizeScores();
	    }
	catch(Exception e)
	    {
		System.err.println("Error caused by: " + name + "\t" + genotype);
		e.printStackTrace();
		System.exit(0);
	    }
    }
    private void setGL(String gl_string)
    {
    	String[] glArray = gl_string.split(",");
    	GL = new double[glArray.length];
    	for(int i=0;i<GL.length;i++)
    		GL[i] = Double.parseDouble(glArray[i]);
    }
    private void normalizeScores()
    // Correct the trucated scores by dividing each score by the 
    // sum of the probability scores.
    {
	double sumScores = 0.0;
	for(int i = 0; i < GL.length; i++)
		sumScores += Math.pow(10.0, GL[i]);
	if(sumScores > 1.7 || sumScores < 0.93)
	    isValidGenotype = false;	   	
	imputedScore =	imputedScore/sumScores;
    }
    /***************************************/
    /****************SETTERS****************/
    /***************************************/


/*******************Imputed Data********************/

    private void setDossage(String i)
    {
	DS = Double.parseDouble(i);
    }
    private void setImputedGenotype(char[] gtArray) throws Exception
    {    	
        // we pick the allele & score by using the function:  
        // F(j/k) = (k*(k+1)*(0.5)) + j; making sure k >= j.
    	int k,j;			
        int first = Character.getNumericValue(gtArray[0]);
	// case 1: the person is a haploid.
        if(gtArray.length == 1)
	    {
		imputedGenotype = hapVal(first);
	    }
	// case 2: the person is diploid.
	else
	    {		
		int second = Character.getNumericValue(gtArray[2]);
		if(second >= first)
		    {
			k = second;
			j = first;
		    }
		else
		    {
			k = first;
			j = second;
		    }
		Double f =  (k*(k+1)*(0.5)) + j;
		BigDecimal F = new BigDecimal(f);		
		imputedScore = GL[F.intValueExact()];
		if (F.intValueExact() == 0)
			isImputedVariant = false;
		else
			isImputedVariant = true;
		imputedGenotype = hapVal(first) + "," + hapVal(second);	
	    }
    }
    private String hapVal(int a)
    // find the allele indicated by the gt subfield
    {
        //System.out.println("The value of the int passed to hapVal is: " + a);
        if(a == 0)
            return ref;
        else
            return alt[a-1];
    }
    
    /***************************************/
    /****************GETTERS****************/
    /***************************************/

    public double getDosage()
    {
	return DS;
    }
    public double[] getScores()
    {
	return GL;
    }
    public String getName()
    {
	return name;
    } 
    public String getGenotype()
    {
	return genotype;
    }
    public boolean isValidGenotype()
    {
	return isValidGenotype;
    }
    public boolean isVariant()
    {
	return isVariant;
    }
    public String getImputedGenotype()
    {
	return imputedGenotype;
    }    
    public double getImputedScore()
    {
	return imputedScore;
    }
    public boolean isImputedVariant()
    {
    	return isImputedVariant;
    }
}