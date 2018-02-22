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

public class LrTrain {

	public INDArray load(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path));
		INDArray y = Nd4j.create(lines.size(), 3);
		INDArray labelIND = Nd4j.create(lines.size(), 1);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] line_split = StringUtils.split(line, "\t");
			y.putRow(i, Nd4j.create(createFloat(line_split)));
			labelIND.putScalar(i, Integer.parseInt(line_split[2]));
		}
		// INDArray gradAscent = gradAscent(y, labelIND, 0.01f, 1000);
		INDArray gradAscent = stocGradAscent0(y, labelIND, 0.01f);
		return gradAscent;
	}

	public INDArray gradAscent(INDArray y, INDArray classLabels, float alpha, int maxCycles) {
		// 获取矩阵的行列
		int[] mn = y.shape();
		INDArray weights = Nd4j.ones(mn[1], 1);
		for (int i = 0; i <= maxCycles; i++) {
			// z=wx(下标0)+wx(下标1)+wx(下标2)....+wx(下标n)=w(T次方)x
			// z是sigmoid函数的参数
			// 要找到最佳的系数w，用到了梯度上升最优化方法
			// 求得列向量
			INDArray h = sigmoid(y.mmul(weights));
			// 获取误差列向量
			INDArray error = classLabels.sub(h);
			if (i % 100 == 0) {
				double error_rate = error_rate(h, classLabels);
				System.out.println("iter= " + i + ",train error rate= " + error_rate);
			}
			// w=w+a∇f(w)
			// a代表alpha变量
			// w代表
			// 不停的迭代，调整更新回归系数向量
			weights = y.transpose().mul(alpha).mmul(error).add(weights);
		}
		return weights;
	}

	// 这个有问题到时候细看一下
	public INDArray stocGradAscent0(INDArray dataMatrix, INDArray classLabels, float alpha) {
		int[] mn = dataMatrix.shape();
		float[] weights = new float[mn[1]];
		Arrays.fill(weights, 1.0f);
		// 这个是1X3的数据
		INDArray indWeights = Nd4j.create(weights);
		for (int i = 0; i < mn[0]; i++) {
			// 要把indWeights转换成3X1的矩阵才能进行矩阵与矩阵相乘
			// 1X3 3X1
			INDArray mul = dataMatrix.getRow(i).mmul(indWeights.transpose());
			double h = sig(mul.getDouble(0));
			INDArray error = classLabels.getRow(i).sub(h);
			indWeights = dataMatrix.getRow(i).mul(error.mul(alpha)).add(indWeights);
		}
		return indWeights;
	}

	// 估算损失函数
	private double error_rate(INDArray h, INDArray label) {
		int[] mn = h.shape();
		double sum_err = 0.0d;
		for (int i = 0; i < mn[0]; i++) {
			if (h.getDouble(i) > 0 && (1 - h.getDouble(i)) > 0) {
				sum_err -= label.getDouble(i) * Math.log(h.getDouble(i)) + (1 - label.getDouble(i)) * Math.log((1 - h.getDouble(i)));
			} else {
				sum_err -= 0;
			}
		}
		return sum_err / mn[0];
	}

	public INDArray sigmoid(INDArray indArray) {
		int[] mn = indArray.shape();
		INDArray tmp = Nd4j.create(indArray.shape());
		for (int i = 0; i < mn[0]; i++) {
			double sig = sig(indArray.getFloat(i));
			tmp.putScalar(i, sig);
		}
		return tmp;
	}

	/**
	 * sigmoid函数
	 * 
	 * 公式是:1/(1+e^-1)
	 * 
	 * @param x
	 * @return
	 */
	public double sig(double x) {
		return 1.0 / (1 + Math.pow(Math.E, -x));
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

	public double[] createDouble(String[] line_split) {
		double[] dss = Utils.createDouble(line_split, 2);
		double[] ds = new double[3];
		Arrays.fill(ds, 1.0d);
		for (int i = 1; i < 3; i++) {
			ds[i] = dss[i - 1];
		}
		return ds;
	}

	public void wirteFile(String fileName, INDArray array) throws IOException {
		int[] mn = array.shape();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mn[0]; i++) {
			if (sb.length() > 0) {
				sb.append("\t");
			}
			sb.append(array.getDouble(i));
		}
		Files.write(Paths.get(fileName), sb.toString().getBytes());
	}

	public static void main(String[] args) throws IOException {
		LrTrain train = new LrTrain();
		URL resource = LrTrain.class.getResource("testSet.txt");
		System.out.println(resource);
		INDArray load = train.load(resource.getPath());

		// resource = LrTrain.class.getResource("");
		// String weight_name = resource.getPath() + "weights.txt";
		// train.wirteFile(weight_name, load);
		//
	}
}
