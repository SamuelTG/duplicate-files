package za.co.sam.rmb.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindDuplicateFiles implements Runnable {

	private Map<String, Set<Path>> duplicateGroups = new HashMap<>();

	private Map<String, Path> filesSeenAlready = new HashMap<>();
	private Deque<Path> stack = new ArrayDeque<>();

	public FindDuplicateFiles(Deque<Path> stack) {
		this.stack = stack;
	}

	public FindDuplicateFiles() {
	}

	public void searchFileSystem() {

		while (!stack.isEmpty()) {

			Path currentPath = stack.pop();
			File currentFile = currentPath.toFile();

			// if it's a directory,
			// put the new path in our stack
			if (currentFile.isDirectory()) {
				for (File file : currentFile.listFiles()) {
					stack.push(file.toPath());
				}

				// if it's a file
			} else {

				// get its contents
				String fileContents = null;
				try {
					byte[] encodedFile = Files.readAllBytes(currentPath);
					fileContents = new String(encodedFile, "UTF-8");
				} catch (IOException e) {

					// show error and skip this file
					System.out.println(e);
					continue;
				}

				// if file content is same as what we have in filesSeenAlready then its a
				// duplicate
				if (filesSeenAlready.containsKey(fileContents)) {
					Path oldPath = filesSeenAlready.get(fileContents);

					Set<Path> paths = duplicateGroups.get(fileContents);
					if (paths == null) {
						paths = new HashSet<>();
					}
					paths.add(oldPath);
					paths.add(currentPath);
					duplicateGroups.put(fileContents, paths);
					// current file is the duplicate!

					// replace the old path with new so can add it in the group set
					filesSeenAlready.replace(fileContents, oldPath, currentPath);
					
					
					// if it's a new file, throw it in filesSeenAlready
					// and record its path, so we can compare later to check if its a duplicate
				} else {
					filesSeenAlready.put(fileContents, currentPath);
				}
			}
		}

	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName());
		searchFileSystem();

	}

	public Map<String, Set<Path>> getDuplicateGroups() {
		return duplicateGroups;
	}

	public Map<String, Path> getFilesSeenAlready() {
		return filesSeenAlready;
	}

	public Deque<Path> getStack() {
		return stack;
	}

	public void setStack(Deque<Path> stack) {
		this.stack = stack;
	}

}
