package org.opensim.storage;

import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G5Xeon3075;


public class Constants {

	public final static boolean ENABLE_OUTPUT = true;
	public final static boolean OUTPUT_CSV    = false;

	public final static double SCHEDULING_INTERVAL = 1;
	public final static double SIMULATION_LIMIT = 3293;

	public final static int CLOUDLET_LENGTH	=  48600 * (int) SIMULATION_LIMIT;
	public final static int CLOUDLET_PES	= 1;

	/*
	 * VM instance types:
	 * 	M1.tiny : 0.5EC2 1CPU 512MB
	 * 	M1.SMALL : 1EC2	1CPU 2048MB
	 * 	M1.MEDIUM : 2EC2 2CPU 4096MB
	 * 	M1.LARGE : 2.5EC2 4CPU 8192MB
	 * 	M1.XLARGE : 2.5EC2 8CPU 16384MB
	 *   
	 *
	 */
	public final static int VM_TYPES	= 5;
	public final static int[] VM_MIPS	= { 2500, 2500, 2000, 1000, 250, 500 };
	public final static int[] VM_PES	= { 8, 4, 2, 1, 1, 1 };
	public final static int[] VM_RAM	= { 16384,  8192, 4096, 2048, 1024, 512 };
	public final static int VM_BW		= 100000; // 100 Mbit/s
	public final static int VM_SIZE		= 2500; // 2.5 GB

	/*
	 * Host types:
	 *   Openstack lab (1 x [Xeon e3-1220 3100 MHz, 4 cores], 16GB)
	 *   
	 */
	public final static int HOST_TYPES	 = 6;
	public final static int[] HOST_MIPS	 = { 3100, 3100, 3100, 3100, 3100, 3100 };
	public final static int[] HOST_PES	 = { 8, 8, 8, 8, 8, 8 };
	public final static int[] HOST_RAM	 = { 16384, 16384, 16384, 16384, 16384, 16384 };
	public final static int HOST_BW		 = 1000000; // 1 Gbit/s
	public final static int HOST_STORAGE = 1000000; // 1 GB

	public final static PowerModel[] HOST_POWER = {
		new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
		new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
		new PowerModelSpecPowerHpProLiantMl110G5Xeon3075()
	};

}
