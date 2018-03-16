package org.algorithms.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {

	static Path path = Paths.get("data");

	public static File readFilename(String fileName) {
		Path resolve = path.resolve(fileName);
		File file = new File(resolve.toString());
		if (!file.exists()) {
			throw new NullPointerException("沒有這個文件:" + resolve.toString());
		}

		return file;
	}

	public static Scanner getScanner(String fileName) throws FileNotFoundException {
		File file = readFilename(fileName);
		return new Scanner(new FileInputStream(file), "UTF-8");
	}
	
	public static <R> List<R> readAllStrings(String fileName, Function<String, R> function) {
		File file = readFilename(fileName);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
			return br.lines().map(function).collect(Collectors.toList());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public static int[] readAllInts(String fileName) {
		List<Integer> list = readAllStrings(fileName, x -> Integer.parseInt(x.trim()));
		return list.stream().mapToInt(x -> x).toArray();
	}

}
