package com.example.model.ds;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

public class CustomeLinkedList<T> implements Iterable<T> {
    @JsonIgnore private Node<T> head;
    @JsonIgnore private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public CustomeLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public boolean remove(T data) {
        if (isEmpty()) {
            return false;
        }

        if (head.data.equals(data)) {
            head = head.next;
            size--;
            if (isEmpty()) {
                tail = null;
            }
            return true;
        }

        Node<T> current = head;
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }

        if (current.next != null) {
            if (current.next == tail) {
                tail = current;
            }
            current.next = current.next.next;
            size--;
            return true;
        }

        return false;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public Node<T> getHead() { return head; }

    public boolean contains(T data) {
        for (T item : this) {
            if (item.equals(data)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @JsonValue
    public T[] toJsonArray() {
        int count = 0;
        for (T ignored : this) count++;

        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(
            head != null ? head.data.getClass() : Object.class,
            count
        );

        int idx = 0;
        for (T elem : this) {
            array[idx++] = elem;
        }
        return array;
    }

    // Called when Jackson deserializes JSON array into this list
    @JsonCreator
    public static <T> CustomeLinkedList<T> fromJsonArray(T[] array) {
        CustomeLinkedList<T> list = new CustomeLinkedList<>();
        if (array != null) {
            for (T elem : array) {
                list.add(elem);
            }
        }
        return list;
    }
}
