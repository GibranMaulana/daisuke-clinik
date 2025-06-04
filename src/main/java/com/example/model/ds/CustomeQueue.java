package com.example.model.ds;

public class CustomeQueue<T> {
    private CustomeLinkedList<T> list = new CustomeLinkedList<>();

    public void enqueue(T value) {
        list.add(value);
    }

    public T dequeue() {
        if (list.isEmpty()) return null;
        T front = list.get(0);      // you must add a get(int) in CustomeLinkedList
        list.remove(front);
        return front;
    }

    public T peek() {
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    /** Return contents in an array for JSON saving. */
    @SuppressWarnings("unchecked")
    public Object[] toArray() {
        return list.toJsonArray(); // reuse your linked‐list → Object[] converter
    }
}
