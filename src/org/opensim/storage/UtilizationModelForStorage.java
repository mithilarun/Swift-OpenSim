package org.opensim.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.cloudbus.cloudsim.*;

/**
 * The Class UtilizationModelPlanetLab.
 */
public class UtilizationModelForStorage implements UtilizationModel {

	/** The scheduling interval. */
	private double schedulingInterval;

	/** The data (5 min * 288 = 24 hours). */
	private final double[] dataRead = new double[1201];
	private final double[] dataWrite = new double[1201];


	/**
	 * Instantiates a new utilization model PlanetLab.
	 * 
	 * @param inputPath the input path
	 * @throws NumberFormatException the number format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public UtilizationModelForStorage(String inputPathRead ,String inputPathWrite, double schedulingInterval)
			throws NumberFormatException,
			IOException {
		setSchedulingInterval(schedulingInterval);
		BufferedReader inputRead = new BufferedReader(new FileReader(inputPathRead));
		BufferedReader inputWrite = new BufferedReader(new FileReader(inputPathWrite));

		int n = dataRead.length;
		for (int i = 0; i < n - 1; i++) {
			try{
			dataRead[i] = Double.valueOf(inputRead.readLine());
			dataWrite[i] = Double.valueOf(inputWrite.readLine()) ;
			}
			catch(Exception e)
			{ Log.printLine(e);}
		}
		dataRead[n - 1] = dataRead[n - 2];
		dataWrite[n - 1] = dataWrite[n - 2];
//System.out.println("INSIDE UTILSTORAGE");
		inputRead.close();
		inputWrite.close();

	}

	/*
	 * (non-Javadoc)
	 * @see cloudsim.power.UtilizationModel#getUtilization(double)
	 */
	@Override
	public double getUtilization(double time) {
	
		
		//if (time % getSchedulingInterval() == 0) {
			//return data[(int) time / (int) getSchedulingInterval()];
		//}
		//int time1 = (int) Math.floor(time / getSchedulingInterval());
		//int time2 = (int) Math.ceil(time / getSchedulingInterval());
		
		

		double totalRead=0;
		double totalWrite=0;
		
		for(int i=0;i<dataRead.length;i++)
		{
			totalRead=totalRead+dataRead[i];
			totalWrite=totalWrite+dataWrite[i];
		}
		
		
		if(time>1200)
		{
			time=time%1200;
		}
		
		double actualiops=dataRead[(int)time]+dataWrite[(int)time];
		
		
		int time1=(int)Math.floor(time);
		int time2=(int)Math.ceil(time);
		
		double read1 = dataRead[time1];
		double read2 = dataRead[time2];
		double write1 = dataWrite[time1];
		double write2 = dataWrite[time2];
		
		double readDelta = (read2 - read1) / ((time2 - time1) * getSchedulingInterval());
		double writeDelta = (write2 - write1) / ((time2 - time1) * getSchedulingInterval());

		double readps = read1 + readDelta * ((time - time1 )* getSchedulingInterval());
		//readps = readps * 3;
		
		double writeps = write1 + writeDelta * ((time - time1 )* getSchedulingInterval());
		//writeps = writeps * 3;
		
		
		double avgreadps=totalRead/dataRead.length;
		//System.out.println("Time = "+time);
		
		double oneReadTime=0.0;
		double oneWriteTime=0.0;
		if(readps!=0)
		{
			oneReadTime=1/readps;
		}
		if(writeps!=0)
		{
			oneWriteTime=1/writeps;
		}
		
		//double readps=dataRead[(int) time];
		//double writeps=dataWrite[(int) time];
		//double oneReadTime=1/readps;
		//double oneWriteTime=1/writeps;
		double latency = 0.00417;     // 4.17 ms in seconds
		double avgSeekTime = 0.009;
		double oneIOTime=oneReadTime+oneWriteTime+latency+avgSeekTime;
		double iops=1/oneIOTime;
		//System.out.println("IOPS= "+iops+"  ******* TIME= "+time);
		//double iops=readps+writeps+latency+avgseektime;
		double expectediops =iops*3;
		double diff=expectediops- actualiops;
		if(((Math.abs(diff)/actualiops)*100)>30)
		{
			diff=diff/2;
			if(expectediops > actualiops)
			{
				expectediops-=Math.abs(diff);
			}
			else
			{
				expectediops+=Math.abs(diff);
			}
		}
		
		return expectediops/3;
	}

	/**
	 * Sets the scheduling interval.
	 * 
	 * @param schedulingInterval the new scheduling interval
	 */
	public void setSchedulingInterval(double schedulingInterval) {
		this.schedulingInterval = schedulingInterval;
	}

	/**
	 * Gets the scheduling interval.
	 * 
	 * @return the scheduling interval
	 */
	public double getSchedulingInterval() {
		return schedulingInterval;
	}
}