package info_table_builder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import utility.DetailedVariantRecord;
import utility.MultisampleVariantRecord;
/**
 * This class is used to create the one and only Info Table. The info table
 * largely consists of the data present in the Info field of the VCF file.
 * Other things are calculated like DAF and GERP scores are part of the final 
 * Info Table. 
 * 
 * (See Biotookit_apps/Motif_Evolution/Motif_Diversity/Build_End_Stage_Tables
 * for more information on completing the Info Table)
 * information about SNPs   
 */
public class InfoTableBuilder 
{
	private String[] sampleNames;
	private BufferedReader reader;
	private BufferedWriter writer;
	@SuppressWarnings("unused")
	public static void main(String[]args)
	{
		try
	    {
			String inputFile  = args[0];
			String outputFile = args[1];
			InfoTableBuilder myBuilder = new InfoTableBuilder(inputFile.trim(), outputFile.trim());			
	    }
	catch(Exception e)
	    {
			System.out.println("Error...Please specify the input file and output file in the args[] array. ");
			System.out.println("ie: <input_file> <output_file>");
			e.printStackTrace();
			System.exit(0);
	    }	
	}
	public InfoTableBuilder(String inFile, String outFile)
	{
	    driver(inFile, outFile);
	}
	
	private void driver(String inFile, String outFile)
	{
		String line;
		DetailedVariantRecord myDRecord = null;
		MultisampleVariantRecord MSVrecord = null;
		StatsCalculator myCalculator = null;
		try
		{
		    //reader = new BufferedReader(new FileReader (inFile));
		    //writer = new BufferedWriter(new FileWriter(outFile));

			reader = new BufferedReader(new FileReader (inFile), 67108864);
		    writer = new BufferedWriter(new FileWriter(outFile), 67108864);
		    // spin through the header data.
		    line = reader.readLine();
		    int count = 0;
		    while(line != null)
			{
		    	System.out.println(line);
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
			    myDRecord = new DetailedVariantRecord(line);
			    MSVrecord = new MultisampleVariantRecord(line, sampleNames);
			    myCalculator = new StatsCalculator(MSVrecord, myDRecord);
			    writeRecord(myDRecord, myCalculator);
			    line = reader.readLine();
			    if(count % 32768 == 1)
			    	System.out.println("Records processed: " + count 
			    			+ "\t location: " + MSVrecord.getPos());
			    count ++;
			}
		    reader.close();
		    writer.close();
		}
		catch(Exception e)
		    {
			e.printStackTrace();
			System.exit(0);
		    }
	}
    private void writeRecord(DetailedVariantRecord myRecord, StatsCalculator myCalculator) throws Exception
    {
    /*variant record members*/
	writer.write(myRecord.getChrom() + "`");     //field 1
	writer.write(myRecord.getPos() + "`");	 
	writer.write(myRecord.getId() + "`");		
	writer.write(myRecord.getFilter() + "`");
	writer.write(myRecord.getQual() + "`");
	/*info members*/
	
	writer.write(myRecord.get_LDAF() + "`");   //field 6
	writer.write(myRecord.get_AVGPOST() + "`");  
	writer.write(myRecord.get_RSQ() + "`");      
	writer.write(myRecord.get_ERATE() + "`");    
	writer.write(myRecord.get_THETA() + "`");    
	writer.write(myRecord.get_CIEND() + "`");    
	writer.write(myRecord.get_CIPOS() + "`");    
	writer.write(myRecord.get_END() + "`");      
	writer.write(myRecord.get_HOMLEN() + "`");   
	writer.write(myRecord.get_HOMSEQ() + "`");   
	writer.write(myRecord.get_SVLEN() + "`");    
	writer.write(myRecord.get_SVTYPE() + "`");   
	writer.write(myRecord.get_AC() + "`");	
	writer.write(myRecord.get_AN() + "`");
	writer.write(myRecord.get_AA() + "`");	//field 20
	writer.write(myRecord.get_AF() + "`");	
	writer.write(myRecord.get_AMR_AF() + "`");
	writer.write(myRecord.get_ASN_AF() + "`");   
	writer.write(myRecord.get_AFR_AF() + "`");   
	writer.write(myRecord.get_EUR_AF() + "`");   
	writer.write(myRecord.get_VT() + "`");
	writer.write(myRecord.get_SNPSOURCE() + "`");
	/*calculator members*/	
	writer.write(myCalculator.getDAF() + "`");                 //field 28
	writer.write(myCalculator.getDerivedAlleleCount() + "`");  //field 29
	writer.write(myCalculator.getTotalTokens() + "`");	       //field 30
	writer.write(myCalculator.getInvalidRecordsCount() + "\n");//field 31 
	// "invalid Records" are not really invalid, when the tables are being
	// created, we refer to them as overridden_records.
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