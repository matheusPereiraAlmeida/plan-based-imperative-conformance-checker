package main;

import java.io.File;
import view.Desktop;

public class Main {

	public static void main(String[] args) {

		//Force some files of Fast-downward to be executable

		new File("fast-downward/src/translate/translate.py").setExecutable(true);			
		new File("fast-downward/src/preprocess/preprocess").setExecutable(true);	
		new File("fast-downward/src/search/downward").setExecutable(true);
		new File("fast-downward/src/search/unitcost").setExecutable(true);
		new File("fast-downward/src/search/downward-release").setExecutable(true);

		new File("checkNumberOfTraces").setExecutable(true);		
		new File("run_FD_optimal_blind_Astar").setExecutable(true);
		new File("run_FD_optimal_blind_Astar_all").setExecutable(true);
		new File("run_FD_suboptimal_hmax").setExecutable(true);
		new File("run_FD_suboptimal_hmax_all").setExecutable(true);

		new File("seq-opt-spmas/run_SPMAS").setExecutable(true);
		new File("seq-opt-spmas/run_SPMAS_all").setExecutable(true);
		new File("seq-opt-symba-2/run_SYMBA2").setExecutable(true);
		new File("seq-opt-symba-2/run_SYMBA2_all").setExecutable(true);

		new Desktop();	 
	}
}
