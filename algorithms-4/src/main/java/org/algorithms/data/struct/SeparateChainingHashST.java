package org.algorithms.data.struct;

/**
 * hash链表式
 *
 * @param <Key>
 * @param <Value>
 */
public class SeparateChainingHashST<Key, Value> {

    private static final int INITIAL_CAPACITY = 16;

    private static final int shift = Integer.BYTES * 8;

    private int threshold;

    private int capacity;

    private int size = 0;

    Entry[] table;

    public SeparateChainingHashST() {
        this(INITIAL_CAPACITY);
    }

    public SeparateChainingHashST(int capacity) {
        this.capacity = setCapacity(capacity);
        System.out.println(this.capacity);
        setThreshold(this.capacity);
        table = new Entry[this.capacity];
    }

    public void put(Key key, Value val) {
        Entry[] tab = table;
        int len = tab.length;
        int index = hash(key) & (len - 1);
        for (Entry<Key, Value> e = table[index]; e != null; e = e.getNext()) {
            if (key.equals(e.getK())) {
                e.v = val;
                return;
            }
        }
        tab[index] = new Entry(key, val, tab[index]);

        int sz = ++size;
        if (sz >= threshold) {
            rehash();
        }
    }

    public Value delete(Key key) {
        Entry<Key, Value> entry = this.delete0(key);
        if (entry != null) {
            return entry.getV();
        }
        return null;
    }

    private Entry<Key, Value> delete0(Key key) {
        Entry[] tab = table;
        int len = tab.length;
        int index = hash(key) & (len - 1);
        if (table[index] == null) {
            return null;
        }
        Entry<Key, Value> node = null;
        Entry<Key, Value> p = table[index];

        if (key.equals(p.getK())) {
            node = p;
        } else {
            Entry<Key, Value> c = p.getNext();
            do {
                if (key.equals(c.getK())) {
                    node = c;
                    break;
                }
                p = c;
            } while ((c = c.getNext()) != null);
        }

        if (node != null) {
            if (node == p) {
                table[index] = node.getNext();
            } else {
                p.next = node.getNext();
            }
            --size;
        }
        return node;
    }

    private int setCapacity(int capacity) {
        if (capacity <= 0) {
            return 1;
        }
        int initialCapacity = capacity;
        initialCapacity |= initialCapacity >>> 1;
        initialCapacity |= initialCapacity >>> 2;
        initialCapacity |= initialCapacity >>> 4;
        initialCapacity |= initialCapacity >>> 8;
        initialCapacity |= initialCapacity >>> 16;
        initialCapacity++;

        if (initialCapacity < 0) {
            initialCapacity >>>= 1;
        }
        return initialCapacity;

//        int leftZerosNum = Integer.numberOfLeadingZeros(capacity);
//        int rightZerosNum = Integer.numberOfTrailingZeros(capacity);
//        int leftToRightZerosNum = shift - leftZerosNum;
//        int shiftNum = leftToRightZerosNum - rightZerosNum == 1 ? rightZerosNum : leftToRightZerosNum;
//        return 1 << shiftNum;
    }

    private void setThreshold(int len) {
        threshold = len * 2 / 3;
    }

    private int hash(Key key) {
        return key.hashCode() & 0x7fffffff;
    }

    private void rehash() {
//        this.resize();
        this.resize1();
    }

    private void resize() {
        Entry[] oldTab = table;
        int oldLen = oldTab.length;
        int newLen = oldLen * 2;
        Entry[] newTab = new Entry[newLen];
        for (int i = 0; i < oldLen; i++) {
            Entry<Key, Value> e = oldTab[i];
            if (e != null) {
                oldTab[i] = null;
                if (e.next == null) {
                    newTab[indexHash(e, newLen)] = e;
                } else {
                    do {
                        Entry<Key, Value> next = e.getNext();
                        int hashIndex = indexHash(e, newLen);
                        //重新进行调整
                        //类似这个tab[index] = new Entry(key, val, tab[index]);
                        //val e = new Entry(key, val)
                        //e.next = tab[index]
                        //tab[index] = e;
                        e.next = newTab[hashIndex];
                        newTab[hashIndex] = e;
                        e = next;
                    } while (e != null);
                }
            }
        }

        setThreshold(newLen);
        table = newTab;
    }

    /**
     * jdk1.8扩容
     */
    private void resize1() {
        Entry[] oldTab = table;
        int oldLen = oldTab.length;
        int newLen = oldLen * 2;
        Entry[] newTab = new Entry[newLen];
        for (int i = 0; i < oldLen; i++) {
            Entry<Key, Value> e = oldTab[i];
            if (e != null) {
                oldTab[i] = null;
                if (e.next == null) {
                    newTab[indexHash(e, newLen)] = e;
                } else {
                    //链表添加普通方法，头和尾指针
                    Entry<Key, Value> loH = null;
                    Entry<Key, Value> loT = null;
                    Entry<Key, Value> hoH = null;
                    Entry<Key, Value> hoT = null;
                    do {
                        Entry<Key, Value> next = e.getNext();
                        //等于0表示扩容前得元素，不用移动
                        if ((hash(e.getK()) & oldLen) == 0) {
                            if (loH == null) {
                                loH = e;
                            } else {
                                loT.next = e;
                            }
                            loT = e;
                        } else {
                            if (hoH == null) {
                                hoH = e;
                            } else {
                                hoT.next = e;
                            }
                            hoT = e;
                        }
                        e = next;
                    } while (e != null);
                    //消除原来链表next得指向
                    if (loT != null) {
                        loT.next = null;
                        newTab[i] = loH;
                    }

                    if (hoT != null) {
                        hoT.next = null;
                        //扩容也是按照倍数进行扩容
                        newTab[oldLen + i] = hoH;
                    }

                }
            }
        }

        setThreshold(newLen);
        table = newTab;
    }

    private int indexHash(Entry<Key, Value> e, int len) {
        return hash(e.getK()) & (len - 1);
    }


    private static class Entry<Key, Value> {
        Key k;
        Value v;
        Entry<Key, Value> next;

        public Entry(Key key, Value val, Entry entry) {
            this.k = key;
            this.v = val;
            this.next = entry;
        }

        public Key getK() {
            return k;
        }

        public Value getV() {
            return v;
        }

        public Entry<Key, Value> getNext() {
            return next;
        }
    }

    public static void main(String[] args) {
        SeparateChainingHashST st = new SeparateChainingHashST(7);
    }
}
