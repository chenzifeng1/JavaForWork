package container.collection.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-05-12 19:02
 * @Version: 1.0
 **/
public class LearnHashMap<K,V> {
    private static final long serialVersionUID = 362498820763181265L;
    /**
     * 默认初始化容量
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    /**
     * 最大容量
     */
    static final int MAXIMUM_CAPACITY = 1073741824;
    /**
     * 负载因子
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    /**
     * 转红黑树的链表长度阈值
     */
    static final int TREEIFY_THRESHOLD = 8;
    /**
     * 退化为红黑树阈值
     */
    static final int UNTREEIFY_THRESHOLD = 6;
    /**
     * 转换为红黑树的数组长度阈值，数组超过该阈值就会转为红黑树
     */
    static final int MIN_TREEIFY_CAPACITY = 64;
    transient Node<K, V>[] table;
    transient Set<Map.Entry<K, V>> entrySet;
    transient int size;
    transient int modCount;
    int threshold;
    final float loadFactor;

    public LearnHashMap() {
        this.loadFactor = 0.75F;
    }

    static final int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ h >>> 16;
    }

    HashMap<String,String> hashMap = new HashMap<String, String>();


    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public final K getKey() {
            return this.key;
        }
        @Override
        public final V getValue() {
            return this.value;
        }
        @Override
        public final String toString() {
            return this.key + "=" + this.value;
        }
        @Override
        public final int hashCode() {
            return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
        }

        @Override
        public final V setValue(V newValue) {
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            } else {
                if (o instanceof Map.Entry) {
                    Map.Entry<?, ?> e = (Map.Entry)o;
                    if (Objects.equals(this.key, e.getKey()) && Objects.equals(this.value, e.getValue())) {
                        return true;
                    }
                }

                return false;
            }
        }
    }
}
