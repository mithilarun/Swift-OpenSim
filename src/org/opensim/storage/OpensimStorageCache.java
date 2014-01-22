package org.opensim.storage;

import java.util.List;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Log;


public class OpensimStorageCache 
{
	/** name of the cache*/
	private final String CacheName;
	
	/**control node the cache is associated with  */
	private String ControlNode;

	/** cache hit rate*/
	private  final double HitRate;
	
	/** cache miss rate */
	private final double MissRate;
	
	/** a list storing the names of all the files on the cache. */
	private List<String> nameList;

	/** a list storing all the files stored on the cache. */
	private List<File> fileList;
	
	/** capacity of the cache: max number of files it can store */
	private int MaxNumFiles;
	
	private double CacheAccessTime;
	
	private double NetworkLatency;
	
	private double NetworkBandwidth;

	public OpensimStorageCache(String Cachename,String ControlNode,double HitRate,int MaxNumFiles,double CacheAccessTime)
	{
		this.CacheName=Cachename;
		this.ControlNode=ControlNode;
		this.HitRate=HitRate;
		this.MissRate=1-HitRate;
		this.MaxNumFiles=MaxNumFiles;
		this.CacheAccessTime=CacheAccessTime;
	
	}
	
	public double cacheTransactionime(File file)
	{
	
		double time=HitRate * CacheAccessTime + MissRate * getAccessTimeForCacheMiss(file);
		
		return time;
	}
	
	public double getAccessTimeForCacheMiss(File file)
	{
		Log.printLine("Lat: "+getNetworkLatency()+"size: "+file.getSize()+"b/w: "+getNetworkBandwidth());
		Double AccessTime=getNetworkLatency() + file.getSize() / getNetworkBandwidth();
		return AccessTime;
	}
	
	
	public void setNetworkLatency(double netLat)
	{
		this.NetworkLatency=netLat;	
	}
	
	public void setNetworkBandwidth(double bandwidth)
	{
		this.NetworkBandwidth=bandwidth;
	}
	
	public double getNetworkLatency()
	{
		return NetworkLatency;
	}
	
	public double getNetworkBandwidth()
	{
		return NetworkBandwidth;
	}

	
	public String getAssociation()
	{
		return ControlNode;
	}
	
	
	
}