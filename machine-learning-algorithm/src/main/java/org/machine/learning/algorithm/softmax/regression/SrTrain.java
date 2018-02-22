package org.machine.learning.algorithm.softmax.regression;

import static org.nd4j.linalg.ops.transforms.Transforms.exp;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.machine.learning.algorithm.util.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class SrTrain {

	public INDArray load(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path));
		INDArray y = Nd4j.create(lines.size(), 3);
		INDArray labelIND = Nd4j.create(lines.size(), 1);
		Set<String> sets = new HashSet<String>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (StringUtils.isBlank(line)) {
				continue;
			}
			String[] line_split = StringUtils.split(line, "\t");
			y.putRow(i, Nd4j.create(createFloat(line_split)));
			sets.add(line_split[2]);
			labelIND.putScalar(i, Integer.parseInt(line_split[2]));
		}

		INDArray array = gradientAscent(y, labelIND, sets.size(), 10000, 0.4);
		System.out.println(array);
		System.out.println(array.sumNumber());
		return array;
	}

	public INDArray gradientAscent(INDArray y, INDArray classLabels, int k, int maxCyclesi, double alpha) {
		// 获取矩阵的行列
		int[] mn = y.shape();
		//生成权重
		INDArray weights = Nd4j.ones(mn[1], k);
		for (int i = 0; i <= maxCyclesi; i++) {
			//矩阵相乘，e指数求解
			INDArray error = exp(y.mmul(weights));
			//求每一行的和，乘以-1
			INDArray rowsum = Nd4j.sum(error, 1).mul(-1);
			//copy4列相同数据
			rowsum = rowsum.repeat(1, k);
			error = error.div(rowsum);
			
			// //
			for (int ix = 0; ix < mn[0]; ix++) {
				int iy = classLabels.getScalar(ix, 0).getInt(0);
				INDArray add = error.getScalar(ix, iy).add(1);
				error.put(ix, iy, add);
			}

			weights = y.transpose().mul(alpha / mn[0]).mmul(error).add(weights);
		}
		return weights;
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

	public static void main(String[] args) throws IOException {
		SrTrain srTrain = new SrTrain();
		URL resource = SrTrain.class.getResource("SoftInput.txt");
		System.out.println(resource);
		INDArray load = srTrain.load(resource.getPath());
	}
}
