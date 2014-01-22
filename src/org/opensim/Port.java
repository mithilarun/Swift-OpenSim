package org.opensim;

import java.util.*;
/* import java.util.concurrent.ConcurrentLinkedQueue; */

import org.cloudbus.cloudsim.Vm;
import org.opensim.Network;
import org.opensim.Subnet;
import org.opensim.VmPacket;

public class Port{
	private String name;
	private String network_id; 
	private int vm_id;
	public String subnet_id;
	public Hashtable<String,String> fixed_ips = new Hashtable<String,String> ();
	private int mac_addr;
	public static int mac = 123;
	/* private ConcurrentLinkedQueue<VmPacket> recieved_msg = new ConcurrentLinkedQueue<VmPacket>(); 
	*/
	//constructor
	public Port(String Name, Vm vm, Subnet sub, Network net){
		System.out.println("Creating a port in network with netword_id " + net.network_id + " and subnet_id " + sub.subnet_id);
		this.name = Name;
		this.network_id = net.network_id;
		this.subnet_id = sub.subnet_id;
		this.fixed_ips.put("ip_addr", sub.Allocate_addr(this));
		this.fixed_ips.put("subnet_id", sub.subnet_id);
		this.mac_addr = this.mac_addr + 1;
		
	
	}

	public Port() {
	}
	
	public void setAddress(String ip){
		this.fixed_ips.put("ip_addr", ip);
	}
	
	
	public String getAddress(){
		return this.fixed_ips.get("ip_addr");
	}
	public int getVmId() {
	return vm_id;
	}

	/**
	* @param deviceId the deviceId to set
	*/
	public void setVmId(int Vm_id) {
	this.vm_id = Vm_id;
	}

	/**
	* @return the macAddress
	*/
	public int getMacAddress() {
	return mac_addr;
	}

	/**
	* @param macAddress the macAddress to set
	*/
	public void setMacAddress(int macAddress) {
		
	this.mac_addr = macAddress;
	}

	/**
	* @return the name
	*/
	public String getName() {
	return name;
	}

	/**
	* @param name the name to set
	*/
	public void setName(String name) {
	this.name = name;
	}

	/**
	* @return the networkId
	*/
	public String getNetworkId() {
	return network_id;
	}

	/**
	* @param networkId the networkId to set
	*/
	public void setNetworkId(String networkId) {
	this.network_id = networkId;
	}
	public void Send_message(String reciever,String data ){
		String senderIp = this.getAddress();
		VmPacket pck = new VmPacket(this.vm_id, senderIp, reciever, data);
		Network.Forward_msg(pck);
	}
	
	public void Recieve_msg(VmPacket pck){
		System.out.println("Recieved message from the vm : "+ pck.getSenderId() +" with ip :"+ pck.getSenderIp() +" with data "+ pck.getData());
	}
	protected void finalize() throws Throwable
	{
	  //should find the respective network obj here
		//Network net;
	  for(Network net : Network.networks){
		  if(net.network_id == this.network_id){
			  for(Subnet sbn : Network.subnets){
				  if(this.subnet_id == sbn.subnet_id){
					  Network.subnets.remove(this);
					  break;
				  }
			  }
			  break;
		  }
	  }
	} 
}