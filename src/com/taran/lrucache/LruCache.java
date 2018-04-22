package com.taran.lrucache;

import java.util.HashMap;

public class LruCache<K, V> {
    private HashMap<K, LinkedListNode<K,V>> map = new HashMap<>();
    LinkedListNode<K, V> head = null;
    LinkedListNode<K, V> tail = null;
    int size;
    int capacity;

    public LruCache(int capacity) {
        this.capacity = capacity;
    }

    public V put(K k, V v) {
        if(map.containsKey(k)) {
            remove(map.get(k));
        } else if(size >= capacity) {
            LinkedListNode<K, V> removed = removeOldest();
            map.remove(removed.key);
        }
        LinkedListNode<K, V> node = new LinkedListNode<>(k, v);
        addToFront(node);
        LinkedListNode<K, V> oldNode = map.put(k, node);
        if(oldNode != null) {
            return oldNode.value;
        } else {
            return null;
        }
    }

    public V get(K k) {
        LinkedListNode<K, V> node = map.get(k);
        if (node != null) {
            node = remove(node);
            addToFront(node);
            return node.value;
        } else {
            return null;
        }
    }


    private void addToFront(LinkedListNode<K,V> node) {
        if(head == null) {
            head = node;
            tail = node;
            size++;
            return;
        }
        node.next = head;
        head.prev = node;
        node.prev = null;
        head = node;
        size++;
    }

    private LinkedListNode<K, V> removeOldest() {
        if (tail == null) return null;
        LinkedListNode<K, V> toRemove = tail;
        if(tail.prev == null) {
            tail = null;
            head = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return toRemove;
    }

    private LinkedListNode<K, V> remove(LinkedListNode<K,V> toRemove) {
        if(head == toRemove) {
            if(head.next == null) {
                tail = null;
                head = null;
            } else {
                head = toRemove.next;
                head.prev = null;
            }
        } else if(tail == toRemove) {
            return removeOldest();
        } else {
            toRemove.prev.next = toRemove.next;
            toRemove.next.prev = toRemove.prev;
        }
        toRemove.next = null;
        toRemove.prev = null;
        size--;
        return toRemove;
    }


    private static class LinkedListNode<K, V> {
        K key;
        V value;
        LinkedListNode<K, V> next;
        LinkedListNode<K, V> prev;

        public LinkedListNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        LruCache<String, String> lru = new LruCache<>(6);
        lru.put("4", "asd");
        lru.put("re", "123");
        lru.put("asf", "est");
        lru.put("4", "345");
        lru.put("a", "jjj");
        lru.put("z", "123");
        lru.put("5rt", "123");

        System.out.println(lru.get("4"));
        lru.put("asf", "123");
        lru.put("adsf", "123");

        System.out.println(lru.get("4"));
        System.out.println(lru.get("re"));


    }
}
