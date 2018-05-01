package org.algorithms.char3;

public abstract class ST<Key extends Comparable<Key>, Value> {
	/**
	 * 将键值对存入表中
	 * @param key
	 * @param val
	 */
	abstract void put(Key key,Value val);
	
	/**
	 * 获取键key对应的值
	 * @param key
	 * @return
	 */
	abstract Value get(Key key);
	
	/**
	 * 从表中删除键key
	 * @param key
	 */
	abstract void delete(Key key);
	/**
	 * 键key是否存在
	 * @param key
	 * @return
	 */
	abstract boolean contains(Key key);
	
	/**
	 * 表是否为空
	 * @return
	 */
	abstract boolean isEmpty();
	/**
	 * 表中键值对数量
	 * @return
	 */
	abstract int size();
	/**
	 * 最小的键
	 * @return
	 */
	abstract Key min();
	/**
	 * 最大的键
	 * @return
	 */
	abstract Key max();
	/**
	 * 小于等于Key的最大键
	 * @param key
	 * @return
	 */
	abstract Key floor(Key key);
	/**
	 * 大于等于key的最小键
	 * @param key
	 * @return
	 */
	abstract Key ceiling(Key key);
	/**
	 * 小于key的键的数量
	 * @param key
	 * @return
	 */
	abstract int rank(Key key);
	/**
	 * 排名为k的键
	 * @param k
	 * @return
	 */
	abstract Key select(int k);
	/**
	 * 删除最小键
	 */
	abstract void deleteMin();
	/**
	 * 删除最大键
	 */
	abstract void deleteMax();
	/**
	 * lo~hi 之间键的数量
	 * @param lo
	 * @param hi
	 * @return
	 */
	abstract int size(Key lo,Key hi);
	/**
	 * lo~hi 之间所有键的，排序
	 * @param lo
	 * @param hi
	 * @return
	 */
	abstract Iterable<Key> keys(Key lo,Key hi);
	/**
	 * 所有键的集合，排序
	 * @return
	 */
	abstract Iterable<Key> keys();
}
