package za.co.sam.rmb.test.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import za.co.sam.rmb.app.Constants;
import za.co.sam.rmb.app.FindDuplicateFiles;

class DuplicateFileTest {

	@Test
	void test() {
		// File system root
		Path root = Paths.get(Constants.TEST_ROOT_DIR + "/duplicates");
		Deque<Path> stack = new ArrayDeque<>();
		stack.push(root);

		FindDuplicateFiles findDuplicateFiles = new FindDuplicateFiles(stack);
		//execute search
		findDuplicateFiles.searchFileSystem();

		Map<String, Set<Path>> duplicateGroups = findDuplicateFiles.getDuplicateGroups();

		//We  know for sure there is duplicate so duplicateGroups must not be null or empty
		assertNotNull(duplicateGroups);
		assertTrue(!duplicateGroups.isEmpty());

		//Compare file contents key groups with its duplicate paths. They must be the same content
		for (Map.Entry<String, Set<Path>> resultGroup : duplicateGroups.entrySet()) {
			String keyContent = resultGroup.getKey();
			compareFileContent(keyContent, resultGroup.getValue());
		}

	}

	private static void compareFileContent(String key, Set<Path> resultGroup) {
		for (Path path : resultGroup) {
			try {
				byte[] encodedFile = Files.readAllBytes(path);
				String fileContents = new String(encodedFile, "UTF-8");
				assertEquals(key, fileContents);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
