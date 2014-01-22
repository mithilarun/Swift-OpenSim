package org.opensim.swift;

import java.util.List;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.opensim.storage.OpenSimFile;
import org.opensim.storage.OpensimStorageControlNode;

class OpenSimDatacenter
{
	String dataCenterName;
	OpensimStorageControlNode ctlnode0;
	
	public OpenSimDatacenter(String dname) 
	{
		// TODO Auto-generated constructor stub
		this.dataCenterName=dname;
	
		Log.printLine("\nCreating a " + dataCenterName);
	}
	
	//control node for this datacenter
	void init(String controlNodeName) throws ParameterException
	{
		ctlnode0=new OpensimStorageControlNode(controlNodeName);
		//Log.printLine("ControlNode : " +ctlnode0);
	}
	
	
	//try the various file operations here like insert ,delete,retrieve here
	
	void fileOperations(List<OpenSimFile>filelist)
	{
		//Log.printLine("***Query*** "+ filelist);
		Log.printLine(" oldsize "+filelist.size());

		for(int i=0;i<filelist.size();++i)
		{
			Log.printLine("\nInserting file #"+(i+1) );
			ctlnode0.ClientQuery("INSERT",filelist.get(i) );
		}
		for(int i=0;i<filelist.size();++i)
		{
			Log.printLine("\nRetrieving file #"+(i+1) );
			ctlnode0.ClientQuery("RETRIEVE",filelist.get(i) );
			
		}
				
		for(int i=0;i<filelist.size();i=i+3)
		{
			Log.printLine("\nDeleting file #"+(i+1) );
			ctlnode0.ClientQuery("DELETE",filelist.get(i) );
			
		}
		Log.printLine("size "+filelist.size());
		
	}
	
	OpensimStorageControlNode getControlNode()
	{
		return ctlnode0;
	}
	
	
}