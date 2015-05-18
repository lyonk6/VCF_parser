package utility;

public class DetailedVariantRecord extends VariantRecord
{
	enum type {ldaf, avgpost, rsq, erate, theta, ciend, cipos, 
		end, homlen, homseq, svlen, svtype, ac, an, aa, af, 
		amr_af, asn_af, afr_af, eur_af,  vt, snpsource};

    private Float   LDAF;     // MLE Allele Frequency Accounting for LD
    private Float   AVGPOST;  // Average posterior probability from MaCH/Thunder   
    private Float   RSQ;      // Genotype imputation quality from MaCH/Thunder
    private Float   ERATE;    // Per-marker Mutation rate from MaCH/Thunder
    private Float   THETA;    // Per-marker Transition rate from MaCH/Thunder
    private String  CIEND;    // Confidence interval around END for imprecise variants
    private String  CIPOS;    // Confidence interval around POS for imprecise variants
    private Integer END;      // End position of the variant described in this record  
    private Integer HOMLEN;   // Length of base pair identical micro-homology at event breakpoints
    private String  HOMSEQ;   // Sequence of base pair identical micro-homology at event breakpoints
    private Integer SVLEN;    // Difference in length between REF and ALT alleles
    private String  SVTYPE;   // Type of structural variant
    private Integer AC;	      // Alternate Allele Count
    private Integer AN;	      // Total Allele Count
    private String  AA;	      // Ancestral Allele, ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/pilot_data/technical/reference/ancestral_alignments/README
    private Float   AF;	      // Global Allele Frequency based on AC/AN
    private Float   AMR_AF;   // Allele Frequency for samples from AMR based on AC/AN
    private Float   ASN_AF;   // Allele Frequency for samples from ASN based on AC/AN
    private Float   AFR_AF;   // Allele Frequency for samples from AFR based on AC/AN
    private Float   EUR_AF;   // Allele Frequency for samples from EUR based on AC/AN
    private String  VT;       // indicates what type of variant the line represents
    private String  SNPSOURCE;//indicates if a snp was called when analyzing the low coverage or exome alignment data
    private int     ancestralAlleleFlag;
    public DetailedVariantRecord(String vcf_record)
    {    	
    	super(vcf_record);    
    	try
    	{
    		String[] info_array = this.getInfo().split(";");
    		initialize_values();
    		setValues(info_array);
    		setAncestralAlleleFlag();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		System.exit(0);
    	}
    }
    private void setAncestralAlleleFlag()
    {
    	if(this.AA.equals(this.getRef()))
        	ancestralAlleleFlag =  0;
    	else if(this.AA.equals(this.getAlt()))
    		ancestralAlleleFlag =  1;
    	else
    		ancestralAlleleFlag = -1;
    }
    private void setValues(String[] info_array) throws Exception
    {	// initialize values to null/0 here.
		for(String a : info_array)
		    {
			String[] i = a.split("=");
			type t = type.valueOf(i[0].trim().toLowerCase());
			switch(t)
			    {
			    case ldaf:
				set_LDAF(i[1]);	    
				break;    
			    case avgpost:  	    
				set_AVGPOST(i[1]);  
				break;		    
			    case rsq: 	   	    
				set_RSQ(i[1]);	     
				break;		    
			    case erate:    	    
				set_ERATE(i[1]);    
				break;		    
			    case theta:    	    
				set_THETA(i[1]);     
				break;		    
			    case ciend:    	    
				set_CIEND(i[1]);    	     
				break;		    
			    case cipos:		    
				set_CIPOS(i[1]);    	     
				break;		    
			    case end: 		    
				set_END(i[1]);	    
				break;		    
			    case homlen: 	    
				set_HOMLEN(i[1]);   	     
				break;		    
			    case homseq: 	    
				set_HOMSEQ(i[1]);     
				break;		    
			    case svlen: 	    
				set_SVLEN(i[1]);     
				break;		    
			    case svtype:	    
				set_SVTYPE(i[1]);   	     
				break;		    
			    case ac: 		    
				set_AC(i[1]);	     
				break;		    
			    case an: 		    
				set_AN(i[1]);	     
				break;		    
			    case aa: 		    
				set_AA(i[1]);	    
				break;		    
			    case af: 		    
				set_AF(i[1]);	    
				break;		    
			    case amr_af: 	    
				set_AMR_AF(i[1]);   
				break;		    
			    case asn_af: 	    
				set_ASN_AF(i[1]);   
				break;		    
			    case afr_af: 	    
				set_AFR_AF(i[1]);   
				break;		    
			    case eur_af:	    
				set_EUR_AF(i[1]);   
				break;		    
			    case vt: 		    
				set_VT(i[1]);	    
				break;		    
			    case snpsource:	    
				set_SNPSOURCE(i[1]);
				break;
			    default:
				throw new Exception("Error, unknown info value: " + a);
			    }
		    }
    }
    private void set_LDAF(String ldaf)
    // LDAF,Number=1,Type=Float,Description="MLE Allele Frequency Accounting for LD
    {
	this.LDAF = Float.parseFloat(ldaf);
    }
    private void set_AVGPOST(String avgpost)	       
    // AVGPOST,Number=1,Type=Float,Description="Average posterior probability from MaCH/Thunder
    {
	this.AVGPOST = Float.parseFloat(avgpost);
    }
    private void set_RSQ(String rsq)		       
    // RSQ,Number=1,Type=Float,Description="Genotype imputation quality from MaCH/Thunder
    {
	this.RSQ = Float.parseFloat(rsq);
    }
    private void set_ERATE(String erate)  	       
    // ERATE,Number=1,Type=Float,Description="Per-marker Mutation rate from MaCH/Thunder
    {
	this.ERATE = Float.parseFloat(erate);
    }
    private void set_THETA(String theta)
    // THETA,Number=1,Type=Float,Description="Per-marker Transition rate from MaCH/Thunder
    {
	this.THETA = Float.parseFloat(theta);
    }
    private void set_CIEND(String ciend)
    // CIEND,Number=2,Type=Integer,Description="Confidence interval around END for imprecise variants
    {		                            
    	this.CIEND = ciend;
    }		                                       
    private void set_CIPOS(String cipos)
    // CIPOS,Number=2,Type=Integer,Description="Confidence interval around POS for imprecise variants
    {
	this.CIPOS = cipos;
    }
    private void set_END(String end)
    // END,Number=1,Type=Integer,Description="End position of the variant described in this record
    {
	this.END = Integer.parseInt(end);
    }
    private void set_HOMLEN(String homlen) 	       
    // HOMLEN,Number=.,Type=Integer,Description="Length of base pair identical micro-homology at event breakpoints
    {
    	this.HOMLEN = Integer.parseInt(homlen);
    }
    private void set_HOMSEQ(String homseq)
    // HOMSEQ,Number=.,Type=String,Description="Sequence of base pair identical micro-homology at event breakpoints
    {
    	this.HOMSEQ = homseq;
    }
    private void set_SVLEN(String svlen)
    // SVLEN,Number=1,Type=Integer,Description="Difference in length between REF and ALT alleles    
    {
    		this.SVLEN = Integer.parseInt(svlen);
    }
    private void set_SVTYPE(String svtype)
    // SVTYPE,Number=1,Type=String,Description="Type of structural variant
    {
    	this.SVTYPE = svtype;
    }    
    private void set_AC(String ac)
    // AC,Number=.,Type=Integer,Description="Alternate Allele Count
    {
    	this.AC = Integer.parseInt(ac);
    }
    private void set_AN(String an)
    // AN,Number=1,Type=Integer,Description="Total Allele Count
    {
    	this.AN = Integer.parseInt(an);
    }
    private void set_AA(String aa)
    // AA,Number=1,Type=String,Description="Ancestral Allele, ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/pilot_data/technical/reference/ancestral_alignments/README
    {
    	this.AA = aa;
    }
    private void set_AF(String af)
    // AF,Number=1,Type=Float,Description="Global Allele Frequency based on AC/AN
    {
    	this.AF = Float.parseFloat(af);
    }
    private void set_AMR_AF(String amraf) 	       
    // AMR_AF,Number=1,Type=Float,Description="Allele Frequency for samples from AMR based on AC/AN
    {
    	this.AMR_AF = Float.parseFloat(amraf);
    }
    private void set_ASN_AF(String asnaf)
    // ASN_AF,Number=1,Type=Float,Description="Allele Frequency for samples from ASN based on AC/AN
    {
    	this.ASN_AF = Float.parseFloat(asnaf);
    }
    private void set_AFR_AF(String afraf)
    // AFR_AF,Number=1,Type=Float,Description="Allele Frequency for samples from AFR based on AC/AN
    {
    	this.AFR_AF = Float.parseFloat(afraf);
    }
    private void set_EUR_AF(String euraf)
    // EUR_AF,Number=1,Type=Float,Description="Allele Frequency for samples from EUR based on AC/AN
    {
    	this.EUR_AF = Float.parseFloat(euraf);
    }
    private void set_VT(String vt)
    // VT,Number=1,Type=String,Description="indicates what type of variant the line represents
    {
    	this.VT = vt;
    }
    private void set_SNPSOURCE(String snpsource)
    // SNPSOURCE,Number=.,Type=String,Description="indicates if a snp was called when analysing the low coverage or exome alignment data    
    {
    	this.SNPSOURCE = snpsource;
    }
    // getters
    public Float   get_LDAF(){return LDAF;}   
    public Float   get_AVGPOST(){return AVGPOST;}
    public Float   get_RSQ(){return RSQ;}    
    public Float   get_ERATE(){return ERATE;}  
    public Float   get_THETA() {return THETA;}  
    public String  get_CIEND() {return CIEND;}  
    public String  get_CIPOS() {return CIPOS;}  
    public Integer get_END()   {return END;}    
    public Integer get_HOMLEN(){return HOMLEN;} 
    public String  get_HOMSEQ(){return HOMSEQ;} 
    public Integer get_SVLEN() {return SVLEN;}  
    public String  get_SVTYPE(){return SVTYPE;} 
    public Integer get_AC(){return AC;}	    
    public Integer get_AN(){return AN;}	    
    public String  get_AA(){return AA;}	    
    public Float   get_AF(){return AF;}	    
    public Float   get_AMR_AF(){return AMR_AF;} 
    public Float   get_ASN_AF(){return ASN_AF;} 
    public Float   get_AFR_AF(){return AFR_AF;} 
    public Float   get_EUR_AF(){return EUR_AF;} 
    public String  get_VT(){return  VT;}
    public String  get_SNPSOURCE(){return SNPSOURCE;}
    public int     get_AncestralAlleleFlag(){return ancestralAlleleFlag;}
    private void initialize_values()
    {    
    	set_AA(".");  
    	/*
	String nullCase = "0";
	set_LDAF(nullCase);     //Float   
	set_AVGPOST(nullCase);  //Float
	set_RSQ(nullCase);	//Float   
	set_ERATE(nullCase);    //Float   
	set_THETA(nullCase);    //Float   
	set_CIEND(nullCase);    //Integer 
	set_CIPOS(nullCase);    //Integer 
	set_END(nullCase);	//Integer 
	set_HOMLEN(nullCase);   //Integer 
	set_HOMSEQ(nullCase);   //String  
	set_SVLEN(nullCase);    //Integer 
	set_SVTYPE(nullCase);   //String  
	set_AC(nullCase);	//Integer 
	set_AN(nullCase);	//Integer 
	set_AA(nullCase);	//String  	
	set_AF(nullCase);       //Float   
	set_AMR_AF(nullCase);   //Float   
	set_ASN_AF(nullCase);   //Float   
	set_AFR_AF(nullCase);   //Float   
	set_EUR_AF(nullCase);   //Float   
	set_VT(nullCase);	//String  
	set_SNPSOURCE(nullCase);//String      
    	*/
    }
}