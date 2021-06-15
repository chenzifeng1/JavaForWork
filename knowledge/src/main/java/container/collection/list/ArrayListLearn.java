package container.collection.list;

import java.io.Serializable;
import java.util.*;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-05-06 18:32
 * @Version: 1.0
 **/
public  class ArrayListLearn<E>  extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {

    ArrayList<String> arrayList = new ArrayList<String>();

    private static final long serialVersionUID = 8683452581122892189L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = new Object[0];
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = new Object[0];
    transient Object[] elementData;
    private int size;
    private static final int MAX_ARRAY_SIZE = 2147483639;


    public void trimToSize() {
        ++this.modCount;
        if (this.size < this.elementData.length) {
            this.elementData = this.size == 0 ? EMPTY_ELEMENTDATA : Arrays.copyOf(this.elementData, this.size);
        }

    }

    public void ensureCapacity(int minCapacity) {
        if (minCapacity > this.elementData.length && (this.elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA || minCapacity > 10)) {
            ++this.modCount;
            this.grow(minCapacity);
        }

    }

    private Object[] grow(int minCapacity) {
        return this.elementData = Arrays.copyOf(this.elementData, this.newCapacity(minCapacity));
    }

    private Object[] grow() {
        return this.grow(this.size + 1);
    }

    private int newCapacity(int minCapacity) {
        int oldCapacity = this.elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity <= 0) {
            if (this.elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
                return Math.max(10, minCapacity);
            } else if (minCapacity < 0) {
                throw new OutOfMemoryError();
            } else {
                return minCapacity;
            }
        } else {
            return newCapacity - 2147483639 <= 0 ? newCapacity : hugeCapacity(minCapacity);
        }
    }

    @Override
    public int size() {
        return size;
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        } else {
            return minCapacity > 2147483639 ? 2147483647 : 2147483639;
        }
    }


    @Override
    public E get(int i) {
        return (E) elementData[i];
    }
}
