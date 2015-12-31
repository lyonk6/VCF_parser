package populationZygosityRetriever;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
/**
 * This class retrieves the pertinent population zygosity data from the
 * 20140126_population_zygosity table.
 * @author kennethlyon
 *
 */

public class PopulationZygosityRetriever
{
	private enum population {ASW, LWK, YRI, CHB, CHS, JPT, CEU, FIN, GBR, TSI, AFR, EUR, ASN};
	private HashMap <String, ArrayList<Double>> PopulationDAFMap;
	private ArrayList<String> locus;
	private Connection connection;
	private Statement  statement;
	private ResultSet  resultSet;

	/**
	 * This constructor does three things. First, it opens a connection to the database
	 * and creates our result set. Second, it calls createPopulationZygosityMap to load
	 * the class member, PopulationZygosityMap, with the result set data. Finally, it
	 * will create a PopulationSpread object that will calculate and write the deltaDAF
	 * for each loci.
	 */
	//TODO, update the constructor documentation
	PopulationZygosityRetriever(String user, String password, String mySQLstatement, String outFile)
	{
		this.initialize();
		try
		{
			String url = "jdbc:mysql://localhost/motif_diversity";
			connection = DriverManager.getConnection
					(url, user.trim() ,password.trim());

			statement = connection.createStatement();
			resultSet = statement.executeQuery(mySQLstatement);
			createPopulationZygosityMap(resultSet);
			new PopulationSetDriver(PopulationDAFMap,locus, outFile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Initialize member fields
	 */
	private void initialize()
	{
		locus = new ArrayList<String>();
		PopulationDAFMap = new HashMap<String, ArrayList <Double>>(20);
		for(population p: population.values())
			PopulationDAFMap.put(p.toString(), new ArrayList<Double>());
	}
	/**
	 * In the new PZR, we need to create an arraylist of population DAFs. This is
	 * done by creating PopualtionDAF objects as we spin though the result set.
	 */
	private void createPopulationZygosityMap(ResultSet resultSet)
	{
		try
		{
			String heterozygous_fraction = null;
			String homozygous_fraction = null;
			PopulationDAF pdaf = null;
			while(resultSet.next())
			{
				locus.add(resultSet.getString("chromosome") + "`" + resultSet.getString("position"));
				// new lines:
				pdaf = null;
				for(population p: population.values())
				{
					homozygous_fraction   = p.toString() + "_homozygous_fraction";
					heterozygous_fraction = p.toString() + "_heterozygous_fraction";
					pdaf = new PopulationDAF(resultSet.getDouble(homozygous_fraction),
							resultSet.getDouble(heterozygous_fraction),
							resultSet.getInt("ancestralAlleleFlag"));
					PopulationDAFMap.get(p.toString()).add(pdaf.getDAF());
				}
			}
			connection.close();
			statement.close();
			resultSet.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	public void debug()
	{
		System.out.println("***************Debug PopulationZygosityRetriever ***************");
		Set<String> keySet = PopulationDAFMap.keySet();
		for(String s: keySet)
		{
			System.out.println(s);
			System.out.println(PopulationDAFMap.get(s).get(0));
		}
		System.out.println("***************Debug PopulationZygosityRetriever ***************");
	}
	public static void main(String[]args)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(args[2].trim()));
			String mySQLstatement = reader.readLine();
			int count = 1;
			while(mySQLstatement != null)
				{
					new PopulationZygosityRetriever(args[0], args[1], mySQLstatement, count + "_chromosome_outfile");
					mySQLstatement = reader.readLine();
					count++;
				}
			reader.close();
		}
		catch(Exception e)
		{
			System.out.println("Please specify the user id, password and a file of SQL queries in the command line");
			e.printStackTrace();
			System.exit(0);
		}
	}
}