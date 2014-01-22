package org.opensim.examples;

import java.io.IOException;

import org.opensim.OpensimRunnerSingle;

public class try3 {

	public static void main(String[] args) throws IOException {
		boolean enableOutput = true;
		boolean outputToFile = true;
		String inputFolder = "/home/ramesh/cloud_storage/src/org/opensim/examples/new";
		String outputFolder = "/home/ramesh/testing/";
		String workload = "";
		String vmAllocationPolicy = "openstack";
		String vmSelectionPolicy = "";
		String parameter = "";

		new OpensimRunnerSingle(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
	}

}
