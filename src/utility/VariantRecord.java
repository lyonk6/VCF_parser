package utility;
/* This class is used to represent a vcf-file-record as an object. By providing 
 * the constructor a vcf-file-record one can access fields with get methods and
 * expect the data to be valid. 
 *
 * For more detailed analysis of a VariantRecord, use the DetailedVariantRecord
 * class or the MultisampleVariantRecord class. 
 *///
//#CHROM   POS   ID   REF   ALT   QUAL   FILTER   INFO
import java.lang.Exception;
public class VariantRecord
{
    private String chrom;
    private int pos;    // 1-based chromosomal locus identifier. Telomeres are indicated with values of 0 or N+1
    private String id;  // semicolon separated list of identifiers if applicable. IE rs_number
    private String ref; //
    private String alt;
    private double qual;
    private String filter;
    private String info;
    private boolean isIndel;       //true if the variant or the reference is more than a char
    private boolean isVariant;     //true if the record is a variant from the reference    
    // the boolean value isVariant does not get its own setter. It is set by function setAlt().
    VariantRecord(String line)
    {
	this(line.split("\t"));
    }
    VariantRecord(String[] myArray)
    {   
	this.setChrom(myArray[0].trim());
	this.setPos(myArray[1].trim());
	this.setId(myArray[2].trim());
	this.setRef(myArray[3].trim());
	this.setAlt(myArray[4].trim());
	this.setQual(myArray[5].trim());
	this.setFilter(myArray[6].trim());
	this.setInfo(myArray[7].trim());
	this.setIsIndel();
    }    
    public void printRecord(boolean a)
    {
	System.out.print(chrom + "\t");
	System.out.print(pos + "\t");
	System.out.print(id + "\t");
	System.out.print(ref + "\t");
	System.out.print(alt + "\t");
	System.out.print(qual + "\t");
	System.out.print(info + "\n");
    }
    private static boolean isDNA(String dna)
    // make sure that the provided DNA sequence (this method handles indels not 
    // just single variants) is valid and properly formatted.  Valid values are
    // A, G, C, T or N and must be in the format: "A" or "A,G,..".
    {
	boolean isValid = false;
	String[] ploidy = dna.split(",");
	for(int i=0; i< ploidy.length; i++)
	    {
		for(int j=0; j< ploidy[i].length(); j++)
		    {
			char n = ploidy[i].charAt(j);
			if(n == 'A' || n == 'G' || n == 'C' || n == 'T' || n == 'N')
			    {
				isValid = true;
			    }
			else
			    {
				isValid = false;
				break;
			    }	
		    }
	    }
	return isValid;
    }
 /****************************************************************************************************/
    //Setters
 /****************************************************************************************************/
    private void setChrom(String chrom)
    {
	this.chrom = chrom;
    }
    private void setPos(String position)
    {
	try
	    {
		pos = Integer.parseInt(position);
	    }
	catch(Exception e)
	    {
		System.out.println("Error... The position value was not an integer: " + position);
		System.exit(0);
	    }
    }
    private void setId(String id)
    {
	this.id = id;
    }
    private void setRef(String ref)
    {   
	if(isDNA(ref))
	    {
		this.ref = ref;
	    }
	else
	    {
		System.out.println("Error... The provided reference value was invalid: " + ref);
		System.exit(0);
	    }
    }
    private void setAlt(String alt)
    { 
	// it is permissable for the alternate value to be a "." in which
	// case this means that the 'variant' matches the reference. Also
	// we set the isVariant field here.  
	if(isDNA(alt) && !alt.equals(ref))
	    {
		this.alt = alt;
		isVariant = true;
	    }
	else if(alt.equals("."))
	    {
		this.alt = ref;
		isVariant = false;
	    }
	else if(alt.equals("<DEL>"))
	    {   // <DEL> is a valid value that means the variant is a deletion
		// the deletion would be equal to the reference value at this 
		// point
		this.alt = alt;
		isVariant = true;		
	    }
	else
	    {
		System.out.println("Error... The provided variant value was invalid: " + alt);
		System.exit(0);
	    }
    }
    private void setQual(String score)
    {
	try
	    {
		qual = Double.parseDouble(score);
	    }
	catch(Exception e)
	    {
		if(score.equals("."))
		    qual = 0.0;
		else
		    {
			System.out.println("Error... The quality score provided was not a double: " + score
					   + "at position: " + pos);
			e.printStackTrace();
			System.exit(0);
		    }
	    }
    }
    private void setFilter(String filter)
    {
	this.filter = filter;
    }
    private void setInfo(String info)
    {
	this.info = info;
    }
    private void setIsIndel()
    {
	isIndel = false;	
	String[] altArray = alt.split(",");		
	if(ref.length() > 1)
	    isIndel = true;
	for(int i = 0; i < altArray.length; i++)
		if(altArray[i].length() > 1)
		    isIndel = true;
    }    
 /***************************************************************************************************/
    //Getters
 /***************************************************************************************************/
    public String getChrom()
    {
	return chrom;
    }    
    public String getId()
    {
	return id;
    }
    public String getRef()
    {
	return ref;
    }
    public String getAlt()
    {
	return alt;
    }
    public double getQual()
    {
	return qual;
    }
    public String getFilter()
    {
	return filter;
    }
    public String getInfo()
    {
	return info;
    }
    public int getPos()
    {
	return pos;
    }
    public boolean getIsIndel()
    {
	return isIndel;
    }
    public boolean getIsVariant()
    {
	return isVariant;
    }
}
