package org.machine.learning.algorithm.logistic.regression;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.machine.learning.algorithm.util.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class LrTest {

	public INDArray load(String flieName) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(flieName));
		INDArray y = Nd4j.create(lines.size(), 3);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] line_split = StringUtils.split(line, "\t");
			y.putRow(i, Nd4j.create(createFloat(line_split)));
		}
		return y;
	}

	public float[] createFloat(String[] line_split) {
		float[] fss = Utils.createFloat(line_split, 2);
		float[] fs = new float[3];
		Arrays.fill(fs, 1.0f);
		for (int i = 1; i < 3; i++) {
			fs[i] = fss[i - 1];
		}
		return fs;
	}

	public INDArray load_weight(String fileName) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(fileName));
		String str = new String(bytes);
		String[] line_split = StringUtils.split(str, "\t");
		double[] ds = new double[line_split.length];
		for (int i = 0; i < line_split.length; i++) {
			ds[i] = Double.parseDouble(line_split[i]);
		}

		INDArray array = Nd4j.create(ds);
		return array;
	}

	public static void main(String[] args) throws IOException {

		LrTest lrTest = new LrTest();
		LrTrain lrTrain = new LrTrain();

		URL resource = LrTest.class.getResource("");
		String weight_name = resource.getPath() + "weights.txt";
		INDArray weights = lrTest.load_weight(weight_name);
		resource = LrTest.class.getResource("test_data");
		INDArray y = lrTest.load(resource.getPath());
		INDArray sigmoid = lrTrain.sigmoid(y.mmul(weights.transpose()));
		System.out.println(sigmoid);
		int[] mn = sigmoid.shape();
		int[] h = new int[mn[0]];
		for (int i = 0; i < mn[0]; i++) {
			if (sigmoid.getDouble(i) < 0.5) {
				h[i] = 0;
			} else {
				h[i] = 1;
			}
		}
		System.out.println(Arrays.toString(h));
	}
}
