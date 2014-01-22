package org.opensim;

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
		
		//cache access time is taken to be 20ns
		this.cache=new OpensimStorageCache("Cache1", this.CNname, 0.5, 5, 0.000000020);
		
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
		
		
		dbname+=1;
		db1=new OpensimStorageServer(dbname, dbcapacity);
		db1.setLatency(dblatency);
		db1.setMaxTransferRate(rate);
		db1.setAvgSeekTime(seektime);
		
		databaseList.add(db0);
		databaseList.add(db1);
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
		}
		
		//retrieving the file 
		else if(TypeOfQuery.equals("RETRIEVE"))
		{
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
			
		}
		//if query is to delete the file
		else if(TypeOfQuery.equals("DELETE"))
		{
			//finding the database containing the file
			OpensimStorageServer db = FindDatabase(file);
			if( db!=null && db.contains(file))
			{
				time=db.deleteFile(file);
			}
			else
			{
				Log.printLine("File"+ file.toString() +" could not be located for deletion");
				time=-1;
			}
		}
			return time;
	}
	
	
	
	public OpensimStorageServer FindDatabase(File file)
	{
		
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
		//find database containing the file
		OpensimStorageServer db = toControlNode.FindDatabase(file);
		
		if(db!=null && db.contains(file))
		{
			File retrievedFile=db.OpengetFile(file.getName());
			time=retrievedFile.getTransactionTime();	
		}
		else
		{
			time=toControlNode.cache.cacheTransactionime(file);
		}
		return time;
	}
	
	public void setDestinationControlNode(OpensimStorageControlNode destcnode)
	{
		destinationctlnode=destcnode;
	}
	
}