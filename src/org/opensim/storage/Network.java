package org.opensim.storage;

import java.util.ArrayList;
import java.util.List;

import org.opensim.Subnet;


public class Network{
    String name;
	String network_id;
	int subnet_num = 0;
	static List<Subnet> subnets = new ArrayList<Subnet>();
	public static List<Network> networks = new ArrayList<Network>();
	//constructor
	public Network(String Name, String Network_id){
		System.out.println("Creating a network with netword_id " + Network_id);
		this.name = Name;
		this.network_id = Network_id;
		Network.networks.add(this);
		
	
	}
	
	
	protected void Add_subnet(Subnet sub){
		Network.subnets.add(sub);
	}
	
	protected void Remove_subnet(Subnet sub){
		Network.subnets.remove(sub);
	}
	
	public static void Forward_msg(VmPacket pck){
		String rec_ip = pck.getRecieverIp();
		String[] arr = rec_ip.split("\\.",4);
		int[] arr2 = new int[4];
		for(int i = 0; i < 4; i++){
			arr2[i] = Integer.parseInt(arr[i]);
		}
		//System.out.println(arr2[1]);
		for(Subnet sb : subnets ){
			String start_addr = sb.addr_range.get("start_addr");
			String last_addr = sb.addr_range.get("last_addr");
			String[] start = start_addr.split("\\.",4);
			String[] last = last_addr.split("\\.",4);
			int[] start_arr = new int[4];
			int[] last_arr = new int[4];
			for(int i = 0; i < 4; i++){
				start_arr[i] = Integer.parseInt(start[i]);
			}
			for(int i = 0; i < 4; i++){
				last_arr[i] = Integer.parseInt(last[i]);
			}
			if((arr2[1] == start_arr[1])&&(arr2[2] == start_arr[2])&&(arr2[0] == start_arr[0])){
				if((arr2[3] > start_arr[3])&&(arr2[3] < last_arr[3])){
					for(Port port: sb.ports){
						String ip_addr = port.getAddress();
						if(rec_ip == ip_addr){
							port.Recieve_msg(pck);
							break;
						}
					}
					break;
				}
			else{System.out.println("The destination IP does not belong to a subnet within the network");}
			}
		}
	}
}