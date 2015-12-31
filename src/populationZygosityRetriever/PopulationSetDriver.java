package populationZygosityRetriever;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * This class creates a PopulationSet object for each loci using the 
 * PopulationZygosityMap. 
 * @author kennethlyon
 */
public class PopulationSetDriver 
{
	private enum population {ASW, LWK, YRI, CHB, CHS, JPT, CEU, FIN, GBR, TSI, AFR, EUR, ASN};
	private HashMap <String, ArrayList<Double>> PopulationDAFMap;	
	private ArrayList<String> locus;
	private BufferedWriter writer;
	public PopulationSetDriver(
			HashMap<String, ArrayList<Double>> PopulationZygosityMap,
			ArrayList<String> locus,
			String outFile) 
	{
		//Create shallow coppies of the passed parameters, then create the writer
		try
		{
			writer = new BufferedWriter(new FileWriter(outFile), 33554432);
			this.PopulationDAFMap = PopulationZygosityMap;		
			this.locus = locus;
			drivePopulationSet();
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	private void drivePopulationSet() throws Exception
	{
		PopulationSet PS = null;
		HashMap<String, Double> temp_pop_set = null;
		for(int i=0; i< locus.size(); i++)
		{
			temp_pop_set = new HashMap<String, Double>();
			for(population p: population.values())			
				temp_pop_set.put(p.toString(), PopulationDAFMap.get(p.toString()).get(i));
			
			PS = new PopulationSet(temp_pop_set);
			
			ArrayList<String> DeltaDAFsNames = PS.getDeltaDAFsNames();
			//write the record
			writer.write(locus.get(i));
			for(int j=0; j < DeltaDAFsNames.size(); j++)
			{
				writer.write("`" + PS.getDeltaDAFs().get(DeltaDAFsNames.get(j)));
			}
			writer.write("\n");
		}
		//debug(PS, locus.size() - 1);
	}
	public void debug(PopulationSet PS, int index)
	{
		System.out.println(locus.get(index));
		System.out.println("****************** Debug PopulationSetDriver *******************");		
		for(String s: PS.getDeltaDAFsNames())
			{
				System.out.print(s + "\t");
				System.out.println(PS.getDeltaDAFs().get(s));
			}
		System.out.println("****************** Debug PopulationSetDriver *******************");
	}
}
