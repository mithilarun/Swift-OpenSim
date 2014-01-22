package org.opensim;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.cloudbus.cloudsim.*;

/**
 * The Class UtilizationModelPlanetLab.
 */
public class UtilizationModelOpenstack implements UtilizationModel {

	/** The scheduling interval. */
	private double schedulingInterval;

	//PrintWriter writer = new PrintWriter("aabbcc.txt","w");
	
	
	/** The data (5 min * 288 = 24 hours). */
	private final double[] data = new double[271];

	/**
	 * Instantiates a new utilization model PlanetLab.
	 * 
	 * @param inputPath the input path
	 * @throws NumberFormatException the number format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public UtilizationModelOpenstack(String inputPath, double schedulingInterval)
			throws NumberFormatException,
			IOException {
		//System.out.println("Inside UtilizationModelOpenstack");
		
		setSchedulingInterval(schedulingInterval);
		BufferedReader input = new BufferedReader(new FileReader(inputPath));
		int n = data.length;
		for (int i = 0; i < n - 1; i++) {
			
			data[i] = Double.valueOf(input.readLine());
			//System.out.println("$$$ "+i+": "+data[i]);
		}
		data[n - 1] = data[n - 2];
		//System.out.println("SIZE = "+n);
		input.close();
	}

	/*
	 * (non-Javadoc)
	 * @see cloudsim.power.UtilizationModel#getUtilization(double)
	 */
	@Override
	public double getUtilization(double time) {
		//System.out.println("Inside getUtil------");
		//if(time>310)
		//{
			//System.out.println(data[(int)time]);
		//}
		if (time % getSchedulingInterval() == 0) {
			return data[(int) time / (int) getSchedulingInterval()];
		}
		int time1 = (int) Math.floor(time / getSchedulingInterval());
		int time2 = (int) Math.ceil(time / getSchedulingInterval());
		//System.out.println(time1+ " ^^^ "+time2+" ^^^^^^^^ "+getSchedulingInterval());
 
		double utilization1 = data[time1];
		double utilization2 = data[time2];
		//System.out.println("ZXXXXXXXXXXXXXXXXX  " + utilization1);

		double delta = (utilization2 - utilization1) / ((time2 - time1) * getSchedulingInterval());
		double utilization = utilization1 + delta * (time - time1 * getSchedulingInterval());
		utilization = utilization * 3;
	/*	if(utilization < 100 ){
			return utilization;
		}
		else{
			return 100;
		}
		*/
		
		//System.out.println("Util = "+utilization+ " **** "+"TIME ="+ time); 

		return utilization;

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
	//writer.close();
}
