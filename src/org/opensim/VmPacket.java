package org.opensim;

public class VmPacket {
	@SuppressWarnings("unused")
	private int senderId;
	@SuppressWarnings("unused")
	private String sender_ip;
	@SuppressWarnings("unused")
	private String reciever_ip;
	@SuppressWarnings("unused")
	private String data;
	public VmPacket(int senderId, String sender_ip, String reciever_ip, String data)
	{
		this.senderId = senderId;
		this.sender_ip = sender_ip;
		this.reciever_ip = reciever_ip;
		this.data = data;
	}
	public int getSenderId(){
		return this.senderId;
	}
	
	public String getSenderIp(){
		return this.sender_ip;
	}
	
	public String getRecieverIp(){
		return this.reciever_ip;
	}
	
	public String getData(){
		return this.data;
	}
}
