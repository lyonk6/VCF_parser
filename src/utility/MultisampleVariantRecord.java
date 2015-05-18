package utility;

/* MultisampleVariantRecord extends VariantRecord to handle multiple 
 * samples (people). Samples trail after the format field (which defines
 * the sample data) and an array of sample names must be passed to the 
 * MultisampleVariantRecord constructor. 
 * samples (or people) from a single vcf file Variant Record.
 */
// #CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	HG00096
// 22	16050115	.	G	A	100	PASS	.	GT:AP	0|0:0.005,0.000	0|0:0.005,0.290
public class MultisampleVariantRecord extends VariantRecord
{
	static final String expectedFormat = "GT:DS:GL";	
    private AllelicInfo[] AllelicInfoArray; //each sample is handled by an allelic info object.
    
    public MultisampleVariantRecord(String record, String[] sampleNames)
    {
	this(record.split("\t"), sampleNames);
    }
    public MultisampleVariantRecord(String[] record, String[] sampleNames)
    {
	super(record);
	try
	    {
		if(sampleNames.length != record.length - 9)
		    throw new Exception("Error...The number of samples and number of genotypes is inconsistent!");
		AllelicInfoArray = new AllelicInfo[sampleNames.length];
		for(int i = 0; i < sampleNames.length; i++)
		    {
			AllelicInfoArray[i] = new AllelicInfo(sampleNames[i], record[i+9].trim());
		    }
	    }
	catch(Exception e)
	    {
		e.printStackTrace();
		System.exit(0);
	    }
    }
    public AllelicInfo[] getAllelicInfoArray()
    {
	return AllelicInfoArray;
    }
    public String getStringGenotype(boolean b, AllelicInfo a)
    {
    	return abbreviatedGenotypeToActualGenotype(a.getImputedGenotype());
    }
    private String abbreviatedGenotypeToActualGenotype(String g)
    {
    	String[] abbreviated = g.split(",");
    	String[] actual = new String[abbreviated.length];
    	String[] altAlleles = getAlt().split(",");
    	for(int i = 0; i < abbreviated.length; i++)
    	{
    		if(abbreviated[i].equals("ref"))
    			actual[i] = getRef();
    		else if(abbreviated[i].equals("var1"))
    			actual[i] = altAlleles[0];
    		else if(abbreviated[i].equals("var2"))
    			actual[i] = altAlleles[1];
    		else if(abbreviated[i].equals("var3"))
    			actual[i] = altAlleles[2];
    		else if(abbreviated[i].equals("var4"))
    			actual[i] = altAlleles[3];
    		else if(abbreviated[i].equals("var5"))	
    			actual[i] = altAlleles[4];
    		else if(abbreviated[i].equals("var6"))	
    			actual[i] = altAlleles[5];
    		}
    	String temp = actual[0];
    	for(int i = 1; i < actual.length; i++)
    	{
			temp = temp.concat(",");
			temp = temp.concat(actual[i]);
    	}
    	return temp;
    }
    //Test main method
    @SuppressWarnings("unused")
	public static void main(String[]args)
    {
    	String[] names = {"John Doe ","Jane Doe"}; 
    	String record = "22\t" + "16050408\t" +	"rs149201999\t" + "T\t" + "C\t"	+ "100\t" + "PASS\t" + 
	    "<INFO>\t" + "GT:DS:GL\t" + "0|0:0.050:-0.03,-1.17,-5.00\t" + "0|0:0.050:-0.03,-1.17,-5.00";
    	System.out.println(record);
    	MultisampleVariantRecord testRecord;
    	testRecord = new MultisampleVariantRecord(record, names);
    }
}