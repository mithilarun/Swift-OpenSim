package org.opensim.examples;

import java.io.IOException;

import org.opensim.storage.OpensimRunnerSingle;

public class try4 {

	public static void main(String[] args) throws IOException {
		boolean enableOutput = true;
		boolean outputToFile = true;
		String inputFolderRead = "E:\\cloud_storage\\src\\org\\opensim\\examples\\reads";
		String inputFolderWrite="E:\\cloud_storage\\src\\org\\opensim\\examples\\writes";
		String inputFolderCpu = "E:\\cloud_storage\\src\\org\\opensim\\examples\\new";
		String outputFolder = "E:\\cloud_storage\\src\\org\\opensim\\examples\\output";
		String workload = "";
		String vmAllocationPolicy = "openstack";
		String vmSelectionPolicy = "";
		String parameter = "";

		new OpensimRunnerSingle(
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

}
