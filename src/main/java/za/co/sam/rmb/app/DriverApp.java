package za.co.sam.rmb.app;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DriverApp {

	public static void main(String[] arg) {
		FindDuplicateFiles findDuplicateFiles = new FindDuplicateFiles();
		Deque<Path> stack = new ArrayDeque<>();
		// File system root not as input? is it read from configs?
		//Or get root from system root? e.g.Paths.get("SystemDrive")
		Path root = Paths.get(Constants.TEST_ROOT_DIR);
		// Path root = Paths.get("SystemDrive");

		stack.push(root);
		findDuplicateFiles.setStack(stack);
		File[] directories = new File(root.toString()).listFiles();

		ExecutorService exec = Executors.newFixedThreadPool(directories.length);
		List<Future<?>> futures = new ArrayList<>();
		// create thread based on number of directories
		for (int x = 0; x < directories.length; x++) {
			if (!findDuplicateFiles.getStack().isEmpty()) {
				Future<?> f = exec.submit(new Thread(findDuplicateFiles));
				futures.add(f);
			}
			// new Thread(findDuplicateFiles).start();
		}

		for (Future<?> f : futures) {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		exec.shutdown();
		System.out.println("###### All tasks are completed. Dup Results #########");

		Map<String, Set<Path>> duplicateGroups = findDuplicateFiles.getDuplicateGroups();

		// print out the group of duplicates found in system
		int groupCount = 1;
		for (Map.Entry<String, Set<Path>> resultGroup : duplicateGroups.entrySet()) {
			System.out.println("Group " + groupCount);

			print(resultGroup.getValue());
			groupCount += 1;
		}

	}

	private static void print(Set<Path> resultGroup) {
		for (Path path : resultGroup) {
			System.out.println(path);
		}

	}
}
