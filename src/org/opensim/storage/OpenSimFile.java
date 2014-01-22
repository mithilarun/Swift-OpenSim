package org.opensim.storage;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.ParameterException;

public class OpenSimFile extends File
{
	private boolean secure;
	
	public OpenSimFile(String fileName, int fileSize, boolean secure) throws ParameterException
	{
		super(fileName,fileSize);
		this.secure=secure;
	}
	
	boolean secureType()
	{
		return secure;
	}
	
}