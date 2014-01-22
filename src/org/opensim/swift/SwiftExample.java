package org.opensim.swift;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.ParameterException;
import org.opensim.storage.OpenSimFile;

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
		
		OpenSimDatacenter datacenter1=new OpenSimDatacenter("privateDataCenter");
		datacenter1.init("privateCtlnode");
		
		OpenSimDatacenter datacenter2=new OpenSimDatacenter("publicDataCenter");
		datacenter2.init("publicCtlnode");
		
		
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
