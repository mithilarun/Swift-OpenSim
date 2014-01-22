package org.opensim.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;

/**
 * ControlNode is the point of contact between the client and the databases,controls all the databases
 * 
 * can also be used to configure the DatabaseServers
 * @author Toshiba
 *
 */

public class OpensimStorageControlNode {
	
	
	/** the name of the control node */
	private final String CNname;
	
	/** a list storing all the databaseServers controlled by the control node*/
	private List<OpensimStorageServer> databaseList;
	
	/** a list storing the names of all the database. */
	private List<String> dbnameList;
	
	OpensimStorageServer db0;
	
	OpensimStorageControlNode destinationctlnode;
	
	OpensimStorageServer db1;
	
	OpensimStorageCache cache;
	
	public OpensimStorageControlNode(String name) throws ParameterException
	{
		if (name == null || name.length() == 0) {
			throw new ParameterException("OpensimStoragControlNode(): Error - invalid ControlNode name.");
		}
		
		
		this.CNname = name;
		
		Log.printLine("Creating a " +CNname);
		
		//cache access time is taken to be 20ns
		this.cache=new OpensimStorageCache("Cache1", this.CNname, 0.5, 5, 0.000000020);
		Log.printLine("Creating a cache for  " +CNname);
		initControlNode();
	}
	
	
	//  databaseServer description
	
	String dbname="dbserver"; 
	double dbcapacity=10000; 	//capacity in Mb
	double dblatency=.00417;	 //in sec
	int rate=133; 				//max transfer rate in MB/sec
	double seektime=.009; 		//in sec
	
	private void initControlNode() throws ParameterException
	{
		databaseList = new ArrayList<OpensimStorageServer>();
		dbnameList = new ArrayList<String>();
		dbname+=0;
		db0=new OpensimStorageServer(dbname, dbcapacity); 
		db0.setLatency(dblatency);
		db0.setMaxTransferRate(rate);
		db0.setAvgSeekTime(seektime);
		
		Log.printLine("Creating database servers..");
		//Log.printLine("DBList " + databaseList);
		//Log.printLine("data base list " +dbnameList);
		Log.printLine("***"+dbname+"***" + "\nLatency :" + db0.getLatency());
		Log.printLine("Capacity"+ db0.getCapacity());
		Log.printLine("Max transfer rate : " +db0.getMaxTransferRate());
		Log.printLine("Avg seek time: " +db0.getAvgSeekTime());
		

		
		
		
		dbname+=1;
		db1=new OpensimStorageServer(dbname, dbcapacity);
		db1.setLatency(dblatency);
		db1.setMaxTransferRate(rate);
		db1.setAvgSeekTime(seektime);
		
		databaseList.add(db0);
		databaseList.add(db1);
		
		
		
		Log.printLine("***"+dbname+"***" + "\nLatency :" + db1.getLatency());
		Log.printLine("Capacity"+ db1.getCapacity());
		Log.printLine("Max transfer rate : " +db1.getMaxTransferRate());
		Log.printLine("Avg seek time: " +db1.getAvgSeekTime());
		

		Log.printLine("Data base List : "+databaseList);
		
		//can be found using traceroute / ping
		cache.setNetworkLatency(0.1);	 //100ms = 0.1 in sec
		cache.setNetworkBandwidth(0.375); 	//in MBytes/sec
	}
	
	/**
	 * returns the number of databaseServers
	 */
	public int getNumdatabaseServers()
	{
		return databaseList.size();
	}
	
	/**
	 * calls appropriate functions based on client query
	 * @param TypeOfQuery
	 * @param name
	 * @return time taken for the operation 
	 */
	public double ClientQuery(String TypeOfQuery,File file)
	{
		double time=0.0; 
		//if query is to insert file
		if(TypeOfQuery.equals("INSERT"))
		{
			//inserting in round robin fashion so that all database servers are utilized
			time = databaseList.get(0).addFile(file);
			OpensimStorageServer temp=databaseList.remove(0);
			databaseList.add(temp);
			
			Log.printLine("Time taken to insert : " + time);
			//Log.printLine(databaseList); // has only 2, so  keeps toggling
			
		}
		
		//retrieving the file 
		else if(TypeOfQuery.equals("RETRIEVE"))
		{
			//Log.printLine("inside Retrieve");
			//find database containing the file
			OpensimStorageServer db = FindDatabase(file);
			
			if(!db.loaded && db!=null && db.contains(file))
			{
				File retrievedFile=db.OpengetFile(file.getName());
				time=retrievedFile.getTransactionTime();	
			}
		
			
			else if( db.loaded==true)
			{
				Log.printLine("Server under heavy load, query for file"+ file.toString() +"forwarded to other datacenter" );
				time=forwardQuery(destinationctlnode,file);
				
			}
				//why isn't else  working here
			if(time==0.0)
			{
				Log.printLine("File"+ file.toString() +" could not be located for retrieval");
				time=-1;
			}
			Log.printLine("Time taken to retrieve : " + time);

		}
		//if query is to delete the file
		else if(TypeOfQuery.equals("DELETE"))
		{
			
			//Log.printLine("inside delete");
			
			//finding the database containing the file
			OpensimStorageServer db = FindDatabase(file);
			//Log.printLine("Space : "+db.getAvailableSpace());
			//Log.printLine("# of files : "+db.getNumStoredFile());


			
			if( db!=null && db.contains(file))
			{
				time=db.deleteFile(file);
			}
			else
			{
				Log.printLine("File"+ file.toString() +" could not be located for deletion");
				time=-1;
			}
			Log.printLine("Time taken to delete : " + time);

		}
			
		Log.printLine("Available Space : "+db0.getAvailableSpace()+"+"+db1.getAvailableSpace());
		Log.printLine("# of files : "+db0.getNumStoredFile()+"+"+db1.getNumStoredFile());

		
			return time;
	}
	
	
	
	public OpensimStorageServer FindDatabase(File file)
	{
		
		//Log.printLine("Inside FindDatabase");
		
		Iterator<OpensimStorageServer> it=databaseList.iterator();
		OpensimStorageServer tempdb = null;
		boolean found=false;
		//in each database server check if file is contained and return database containing it 
		while(it.hasNext())
		{
			tempdb = it.next();
			found=tempdb.contains(file);
			if (found)
			{
				return tempdb;
			}
		}
		
		Log.printLine(file.toString() + "not contained in any database servers");
		return null;
		
	}
	
	
	//forwarding query to the other dataCenter control node
	
	public double forwardQuery(OpensimStorageControlNode toControlNode,File file)
	{
		double time=0.0;
		Log.printLine("Forwarding query");
		//find database containing the file
		OpensimStorageServer db = toControlNode.FindDatabase(file);
		
		if(db!=null && db.contains(file))
		{
			File retrievedFile=db.OpengetFile(file.getName());
			time=retrievedFile.getTransactionTime();	
		}
		else
		{
			Log.printLine("try");
			time=toControlNode.cache.cacheTransactionime(file);
		}
		return time;
	}
	
	public void setDestinationControlNode(OpensimStorageControlNode destcnode)
	{
		destinationctlnode=destcnode;
	}
	
}