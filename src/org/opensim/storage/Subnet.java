package org.opensim.storage;




import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import org.opensim.Network;
import org.opensim.Port;



//import Network.java class
public class Subnet{
    String name;
    String network_id;
    Hashtable<String,String> addr_range = new Hashtable<String,String> (); 
    public List <Port> ports = new ArrayList<Port>();
    String gateway;
    String mask;
    String curr_addr;
	String subnet_id;
	//constructor
	public Subnet(String Name, String Network_id, String Subnet_id, String start_addr, String last_addr, String Gateway,String Mask, Network net){
		System.out.println("Creating a subnet with subnet_id " + Subnet_id);
		this.name = Name;
		this.network_id = net.network_id;
		this.subnet_id = Subnet_id;
		this.addr_range.put("start_addr", start_addr);
		this.addr_range.put("last_addr", last_addr);
		this.gateway = Gateway;
		this.mask = Mask;
		this.curr_addr = start_addr;
		net.subnet_num = net.subnet_num + 1;
		net.Add_subnet(this);
	
	}
	
	public String Allocate_addr(Port port){
		String[] arr = this.curr_addr.split("\\.",4);
		String[] arr2 = this.addr_range.get("last_addr").split("\\.",4);
		int limit = Integer.parseInt(arr2[arr2.length-1]);
		int addr = Integer.parseInt(arr[arr.length-1]);
		addr++;
		if(addr <= limit){
			arr[arr.length-1] = Integer.toString(addr);
			StringBuilder sb = new StringBuilder();
	        int i;
	        for(i=0;i<(arr.length-1);i++)
	            sb.append(arr[i]+".");
	        sb.append(arr[i]);
	        this.curr_addr = sb.toString();
	        this.ports.add(port);
			return this.curr_addr;
		}
		else{
			System.out.println("subnet is full");
			return "subnet is full";
		}
	}
	
	protected void finalize() throws Throwable
	{
	  //should find the respective network obj here
		//Network net;
	  for(Network net : Network.networks){
		  if(net.network_id == this.network_id){
			  Network.subnets.remove(this);
			  break;
		  }
	  }
	} 
}

