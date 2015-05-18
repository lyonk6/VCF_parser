package table_builder;
/**
 * TableBuilder provides the a single constructor that expects a VCF file
 * and an output file name. This program is inteded to be used to create 
 * the input necessary for Steve's part of the project (see:
 * Biotookit_apps/Motif_Evolution/Motif_Diversity/main_pipeline)
 * 
 * NOTE: as of 20140126 non-imputed processing is no longer supported. 
 */
import java.io.*;
import java.lang.Exception;

import utility.AllelicInfo;
import utility.MultisampleVariantRecord;

public class TableBuilder
{
    private String inputFile;    
    private String outputFile;
    private String[] sampleNames;
    private BufferedReader reader;
    private BufferedWriter writer;    
    static final boolean use_imputed = true;
    public static void main(String[]args)
    {
	try
	    {
		String inputFile  = args[0];
		String outputFile = args[1];		
		TableBuilder myBuilder = new TableBuilder(inputFile.trim(), outputFile.trim());
	    }
	catch(Exception e)
	    {
		System.out.println("Error...Please specify the input file as well as the output file in the args[] array. ");
		System.out.println("ie: <input_file> <output_file>");
		e.printStackTrace();
		System.exit(0);
	    }
    }
    public TableBuilder(String inputFile, String outputFile)
    {
	this.inputFile  = inputFile;	
	this.outputFile = outputFile;
	try
	    {		
		reader = new BufferedReader(new FileReader (inputFile), 16777216);
		writer = new BufferedWriter(new FileWriter(outputFile), 16777216);	
		this.makeTable();
		writer.close(); 
	    }
	catch(Exception e)
	    {		
		e.printStackTrace();
		System.exit(0);
	    }
    }
   private void makeTable() throws Exception
   {
	String line = null;
	MultisampleVariantRecord myRecord = null;
	line = reader.readLine();
	//Spin through misc header data. As soon as we find the 
	//header-line, we set the sample names and break. 
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
		myRecord = new MultisampleVariantRecord(line, sampleNames);
		if(!myRecord.getIsIndel())
		    writeRecord(myRecord);			
		line = reader.readLine();			
		//should I call System.gc() here?
	    }	
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
	/*
	System.out.println(line + "\n");
	System.out.println("The first is: " + sampleNames[0]);
	System.out.println("The last is:  " + sampleNames[sampleNames.length - 1]); //*/
    }
    private void writeRecord(MultisampleVariantRecord myRecord) throws Exception
    /* Here we write the desired data to the specifed output file. While looping 
     * through the AllelicInfo array we call updateStatistics().
     */
    {
	AllelicInfo[] genotypeArray = myRecord.getAllelicInfoArray();	
	for(AllelicInfo a: genotypeArray)
	    {	
		writer.write(a.getName()  + "\t");
		writer.write(myRecord.getChrom() + "\t");		
		writer.write(myRecord.getPos() + "\t");			
		writer.write(myRecord.getStringGenotype(use_imputed, a) + "\t");
		writer.write("1.0" + "\t");
		writer.write(myRecord.getRef()  + "\t");
		writer.write(myRecord.getAlt()  + "\t");		
		writer.write(a.getGenotype() + "\n");
	    }
    }
}