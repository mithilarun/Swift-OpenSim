package org.opensim.swift;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.opensim.swift.OpenSimFile;

public class SwiftExample {

	/**This class creates two dataCenters with a network latency between them
	 * Each dataCenter has a control node with 2 database servers
	 * and a cache 
	 * 
	 * @author Mithil Arun
	 *
	 */
	
	private  static  List<OpenSimFile> filelist=new ArrayList<OpenSimFile>();
	private  static List<OpenSimFile> privatefilelist=new ArrayList<OpenSimFile>();//this goes to private cloud
	private  static List<OpenSimFile> publicfilelist=new ArrayList<OpenSimFile>();//this goes to public cloud
	
	private static List<OpenSimDatacenter> dataCenterlist=new ArrayList<OpenSimDatacenter>();
	
	
	public static void main(String[] args) {
		try {
		
		Log.printLine("Starting swift example");
		OpenSimDatacenter datacenter1=new OpenSimDatacenter("privateDataCenter");
		datacenter1.init("privateCtlnode");
		
		OpenSimDatacenter datacenter2=new OpenSimDatacenter("publicDataCenter");
		datacenter2.init("publicCtlnode");
		//setting the destControlNode to which query has to be forwarded
		datacenter1.getControlNode().setDestinationControlNode(datacenter2.getControlNode());

		dataCenterlist.add(datacenter1);
		
		dataCenterlist.add(datacenter2);

		createListOfFiles(filelist,30);
		
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
		datacenter1.fileOperations(privatefilelist);
		Log.printLine("\n\n**Query on public file list**\n ");
	
	}
	catch (Exception e)
	{
		e.printStackTrace();
		Log.printLine("The simulation has been terminated due to an unexpected error");
	}
}

	//30 files ,half are secure half unsecure
	static void createListOfFiles(List<OpenSimFile> filelist ,int no) throws ParameterException {
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
