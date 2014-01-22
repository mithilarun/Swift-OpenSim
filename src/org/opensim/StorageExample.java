package org.opensim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;

/**This class creates two dataCenters with a network latency between them
 * Each dataCenter has a control node with 2 database servers
 * and a cache 
 * 
 * @author Toshiba
 *
 */

public class StorageExample 
{
	/** The fileList. */
	private  static  List<OpenSimFile> filelist;
	private  static List<OpenSimFile> privatefilelist;//this goes to private cloud
	private  static List<OpenSimFile> publicfilelist;//this goes to public cloud
	
	private static List<OpenSimDatacenter> dataCenterlist;
		
	/**
	
	/*
	 * main to run this example
	 */
	public static void main(String args[])
	{
				
		try
		{
			
			//step 1:create dataCenters
			OpenSimDatacenter datacenter0 = new OpenSimDatacenter("privateDataCenter");
			datacenter0.init("privateCtlnode");
			
			
			OpenSimDatacenter datacenter1 = new OpenSimDatacenter("publicDataCenter");
			datacenter1.init("publicCtlnode");
			
			//setting the destControlNode to which query has to be forwarded
			datacenter0.getControlNode().setDestinationControlNode(datacenter1.getControlNode());
			
			dataCenterlist.add(datacenter0);
			
			dataCenterlist.add(datacenter1);
			
	
			
			createListOfFiles(filelist,30);
			
			//depending on whether file needs to be secure or not add it to appropriate list
			
			for(OpenSimFile f:filelist)
			{
				if(f.secureType())
				{
					privatefilelist.add(f);
				}
				else
				{
					publicfilelist.add(f);
				}
			}
			
			datacenter0.fileOperations(privatefilelist);
		//	datacenter1.fileOperations(publicfilelist);
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}
	
	//30 files ,half are secure half unsecure
	static void createListOfFiles(List<OpenSimFile> filelist ,int no) throws ParameterException
	{
		boolean secure=false;
		for(int i=1;i<=no;++i)
		{
			//100MB file size
			OpenSimFile file=new OpenSimFile("file"+ i , 100, secure);
			filelist.add(file);
			secure=!secure;	//toggling between true and false
					
		}
	}
	
}


class OpenSimDatacenter
{
	String dataCenterName;
	OpensimStorageControlNode ctlnode0;
	
	public OpenSimDatacenter(String dname) 
	{
		// TODO Auto-generated constructor stub
		this.dataCenterName=dname;
	
		
	}
	
	//control node for this datacenter
	void init(String controlNodeName) throws ParameterException
	{
		ctlnode0=new OpensimStorageControlNode(controlNodeName);
	}
	
	
	//try the various file operations here like insert ,delete,retrieve here
	
	void fileOperations(List<OpenSimFile>filelist)
	{
		for(int i=0;i<filelist.size();++i)
		{
			ctlnode0.ClientQuery("INSERT",filelist.get(i) );
		}
	}
	
	OpensimStorageControlNode getControlNode()
	{
		return ctlnode0;
	}
	
	
}