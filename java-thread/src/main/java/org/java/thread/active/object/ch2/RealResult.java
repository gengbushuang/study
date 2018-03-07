package org.java.thread.active.object.ch2;
/**
 * 真实结果
 * @author gbs
 *
 * @param <T>
 */
public class RealResult<T> extends Result<T> {

	private final T resultValue;

	public RealResult(T resultValue) {
		this.resultValue = resultValue;
	}

	@Override
	public T getResultValue() {
		return resultValue;
	}

}
