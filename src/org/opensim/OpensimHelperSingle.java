package org.opensim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelNull
;
import org.opensim.UtilizationModelOpenstack;
import org.cloudbus.cloudsim.examples.power.Constants;

public class OpensimHelperSingle {

	public static List<Cloudlet> createCloudletListPlanetLab(int brokerId, String inputFolderName)
			throws FileNotFoundException {
		List<Cloudlet> list = new ArrayList<Cloudlet>();

		long fileSize = 300;
		long outputSize = 300;
		int vmId = 1;
		int cloudletId = 1;
		int limit = 1;
		UtilizationModel utilizationModelNull = new UtilizationModelNull();

		File inputFolder = new File(inputFolderName);
		File[] files = inputFolder.listFiles();
		//do{
		for (int i = 0; i < files.length; i++) {
			for(int j = 0; j < 2 ; j++){
				for(int k = 0;k < 11;k++){
					Cloudlet cloudlet = null;
				try {
					cloudlet = new Cloudlet(
							cloudletId,
							Constants.CLOUDLET_LENGTH,
							Constants.CLOUDLET_PES,
							fileSize,
							outputSize,
							new UtilizationModelOpenstack(
									files[i].getAbsolutePath(),
									Constants.SCHEDULING_INTERVAL), utilizationModelNull, utilizationModelNull);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
				cloudlet.setUserId(brokerId);
				cloudlet.setVmId(vmId);
				cloudletId++;
				list.add(cloudlet);
				}
			vmId++;			
			}
		}
		//limit++;
		//}while(limit < 4);

		return list;
	}
}
