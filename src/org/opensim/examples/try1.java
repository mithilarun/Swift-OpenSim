package org.opensim.examples;

import java.io.IOException;
import org.opensim.OpensimRunner;


public class try1 {

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		boolean enableOutput = true;
		boolean outputToFile = false;
		String inputFolder = "F:\\jnana\\workspace\\cloud\\src\\org\\opensim\\examples\\workload";
		String outputFolder = "output";
		String workload = "";
		String vmAllocationPolicy = "dvfs";
		String vmSelectionPolicy = "";
		String parameter = "";

		new OpensimRunner(
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
