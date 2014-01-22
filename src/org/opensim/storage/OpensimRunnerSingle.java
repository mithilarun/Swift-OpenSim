package org.opensim.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.opensim.Helper;
import org.opensim.RunnerAbstract;
import org.cloudbus.cloudsim.power.PowerVm;
import org.opensim.Constants;


public class OpensimRunnerSingle extends RunnerAbstract2 {
	public OpensimRunnerSingle(
			boolean enableOutput,
			boolean outputToFile,
			String inputFolderRead,
			String inputFolderWrite,
			String inputFolderCpu,
			String outputFolder,
			String workload,
			String vmAllocationPolicy,
			String vmSelectionPolicy,
			String parameter) {
		super(
				enableOutput,
				outputToFile,
				inputFolderRead,
				inputFolderWrite,
				inputFolderCpu,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
	}


	@Override
	protected void init(String inputFolderRead,String inputFolderWrite,String inputFolderCpu) {
		List<Vm> List1 = new ArrayList<Vm>();
		try {
			CloudSim.init(1, Calendar.getInstance(), false);

			broker = Helper.createBroker();
			int brokerId = broker.getId();
			//System.out.print(inputFolder);
			for(int i = 1 ; i < 7 ; i++ ){
			List1.add(new PowerVm(
					i,
					brokerId,
					162,
					1,
					1024,
					Constants.VM_BW,
					Constants.VM_SIZE,
					1,
					"Xen",
					new CloudletSchedulerDynamicWorkload(162, 1),
					Constants.SCHEDULING_INTERVAL));
			}
			vmList = List1;
			cloudletList = OpensimHelperSingle.createCloudletListPlanetLab(brokerId, inputFolderRead,inputFolderWrite,inputFolderCpu);
			hostList = Helper.createHostList(3);
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
			System.exit(0);
		}
	}

	
	
/*	@Override
	protected void greenInit(String inputFolder) {
		List<Vm> List1 = new ArrayList<Vm>();
		try {
			CloudSim.init(1, Calendar.getInstance(), false);

			broker = Helper.createBroker();
			int brokerId = broker.getId();
			System.out.print(inputFolder);
			for(int i = 1 ; i < 11 ; i++ ){
			List1.add(new PowerVm(
					i,
					brokerId,
					162,
					1,
					1024,
					Constants.VM_BW,
					Constants.VM_SIZE,
					1,
					"Xen",
					new CloudletSchedulerDynamicWorkload(162, 1),
					Constants.SCHEDULING_INTERVAL));
			}
			vmList = List1;
			cloudletList = OpensimHelperSingle.greenCreateCloudletListPlanetLab(brokerId, inputFolder);
			hostList = Helper.createHostList(3);
			//Log.printLine("Inside Opensimhelper... greenCreateCloudlet....****");
			Log.printLine("***Inside OpensimRunner... greenInit Method***");
			
			Log.printLine(vmList);
			Log.printLine(cloudletList);

			
			
			//broker.greenAssignCloudetToVm();
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
			System.exit(0);
		}
	}
*/
	
	
	
}
