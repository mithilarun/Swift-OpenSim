package org.opensim.storage;

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
	private  static  List<OpenSimFile> filelist=new ArrayList<OpenSimFile>();
	private  static List<OpenSimFile> privatefilelist=new ArrayList<OpenSimFile>();//this goes to private cloud
	private  static List<OpenSimFile> publicfilelist=new ArrayList<OpenSimFile>();//this goes to public cloud
	
	private static List<OpenSimDatacenter> dataCenterlist=new ArrayList<OpenSimDatacenter>();
	

	/**
	
	/*
	 * main to run this example
	 */
	public static void main(String args[])
	{
				
		try
		{
			Log.printLine("Starting StorageExample...");
			//step 1:create dataCenters
			OpenSimDatacenter datacenter0 = new OpenSimDatacenter("privateDataCenter");
			datacenter0.init("privateCtlnode");
			
			
			OpenSimDatacenter datacenter1 = new OpenSimDatacenter("publicDataCenter");
			datacenter1.init("publicCtlnode");
			
			
			
			
			//setting the destControlNode to which query has to be forwarded
			datacenter0.getControlNode().setDestinationControlNode(datacenter1.getControlNode());
			
			//Log.printLine(dataCenterlist);
			
			//Log.printLine(datacenter0);

			dataCenterlist.add(datacenter0);
			//Log.printLine(dataCenterlist);

			
			dataCenterlist.add(datacenter1);
			
	
			//Log.printLine(dataCenterlist);

			createListOfFiles(filelist,30);
			
			//Log.printLine("hey2");
			
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
			
			Log.printLine("Creating a Private file list : " + privatefilelist);
			Log.printLine("Creating a Public file list : " + publicfilelist);
			
			Log.printLine("\n\n**Query on private file list** \n");
			datacenter0.fileOperations(privatefilelist);
			Log.printLine("\n\n**Query on public file list**\n ");
	//		datacenter1.fileOperations(publicfilelist);
		
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
		Log.printLine(no + " files created .");
		//Log.printLine("File List : "+ filelist);
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