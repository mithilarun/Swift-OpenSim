package org.opensim;

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


public class OpensimRunnerSingle extends RunnerAbstract {
	public OpensimRunnerSingle(
			boolean enableOutput,
			boolean outputToFile,
			String inputFolder,
			String outputFolder,
			String workload,
			String vmAllocationPolicy,
			String vmSelectionPolicy,
			String parameter) {
		super(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
	}


	@Override
	protected void init(String inputFolder) {
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
			cloudletList = OpensimHelperSingle.createCloudletListPlanetLab(brokerId, inputFolder);
			hostList = Helper.createHostList(2);
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
			System.exit(0);
		}
	}

}
