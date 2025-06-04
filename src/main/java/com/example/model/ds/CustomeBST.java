package com.example.model.ds;

import java.util.function.Consumer;

public class CustomeBST<T extends Comparable<T>> {

  private Node<T> root;  
  private int size;      
  
  private static class Node<T> {
    T data;
    Node<T> left;
    Node<T> right;

    Node(T data) {
      this.data = data;
      this.left = null;
      this.right = null;
    }
  }

  public CustomeBST() {
    root = null;
    size = 0;
  }

  public int size() { 
    return size; 
  }

  public boolean isEmpty() { 
    return size == 0; 
  }

  /*─────────────── INSERT ───────────────*/
  private Node<T> insertRec(Node<T> node, T value) {
    if (node == null) {
      size++;
      return new Node<>(value);
    }

    int cmp = value.compareTo(node.data);
    if (cmp < 0) {
      node.left = insertRec(node.left, value);
    } else if (cmp > 0) {
      node.right = insertRec(node.right, value);
    }
    // if cmp == 0, we treat it as “duplicate” and do nothing
    return node;
  }

  public void insert(T value) {
    root = insertRec(root, value);
  }

  /*─────────────── CONTAINS ───────────────*/
  private boolean containsRec(Node<T> node, T value) {
    if (node == null) {
      return false;
    }
    int cmp = value.compareTo(node.data);
    if (cmp == 0) {
      return true;
    } else if (cmp < 0) {
      return containsRec(node.left, value);
    } else {
      return containsRec(node.right, value);
    }
  }

  public boolean contains(T value) {
    return containsRec(root, value);
  }

  /*─────────────── SEARCH (returns the stored T) ───────────────*/
  /**
   * Returns the actual stored object that .compareTo(key)==0,
   * or null if not found.
   */
  public T search(T key) {
    Node<T> node = searchRec(root, key);
    return (node == null) ? null : node.data;
  }

  private Node<T> searchRec(Node<T> node, T key) {
    if (node == null) {
      return null;
    }
    int cmp = key.compareTo(node.data);
    if (cmp == 0) {
      return node;
    } else if (cmp < 0) {
      return searchRec(node.left, key);
    } else {
      return searchRec(node.right, key);
    }
  }

  /*─────────────── FIND MIN / MAX ───────────────*/
  public T findMin() {
    if (isEmpty()) {
      throw new IllegalStateException("BST is empty.");
    }
    Node<T> current = root;
    while (current.left != null) {
      current = current.left;
    }
    return current.data;
  }

  public T findMax() {
    if (isEmpty()) {
      throw new IllegalStateException("BST is empty.");
    }
    Node<T> current = root;
    while (current.right != null) {
      current = current.right;
    }
    return current.data;
  }

  private T minValue(Node<T> node) {
    Node<T> current = node;
    while (current.left != null) {
      current = current.left;
    }
    return current.data;
  }

  /*─────────────── DELETE ───────────────*/
  private Node<T> deleteRec(Node<T> node, T value) {
    if (node == null) {
      return null; 
    }

    int cmp = value.compareTo(node.data);
    if (cmp < 0) {
      node.left = deleteRec(node.left, value);
    } else if (cmp > 0) {
      node.right = deleteRec(node.right, value);
    } else {
      // Node to delete found
      if (node.left == null && node.right == null) {
        size--;
        return null;
      } else if (node.left == null) {
        size--;
        return node.right;
      } else if (node.right == null) {
        size--;
        return node.left;
      }
      // Two children: replace with in-order successor
      T successorValue = minValue(node.right);
      node.data = successorValue;
      node.right = deleteRec(node.right, successorValue);
      // (No size++ here—size was decremented when we physically removed successor)
    }
    return node;
  }

  public void delete(T value) {
    root = deleteRec(root, value);
  }

  /*─────────────── HEIGHT ───────────────*/
  private int heightRec(Node<T> node) {
    if (node == null) {
      return 0;
    }
    int leftHeight = heightRec(node.left);
    int rightHeight = heightRec(node.right);
    return Math.max(leftHeight, rightHeight) + 1;
  }

  public int height() {
    return heightRec(root);
  }

  /*─────────────── CLEAR ───────────────*/
  public void clear() {
    root = null;
    size = 0;
  }

  /*─────────────── IN-ORDER TRAVERSAL ───────────────*/
  /**
   * Walks the tree in ascending order (left → root → right),
   * calling visitor.accept(node.data) on each node.
   */
  public void inOrderTraversal(Consumer<T> visitor) {
    inOrderRec(root, visitor);
  }

  private void inOrderRec(Node<T> node, Consumer<T> visitor) {
    if (node == null) {
      return;
    }
    inOrderRec(node.left, visitor);
    visitor.accept(node.data);
    inOrderRec(node.right, visitor);
  }

  public CustomeLinkedList<T> inOrderList() {
    CustomeLinkedList<T> result = new CustomeLinkedList<>();
    inOrderCollect(root, result);
    return result;
  }

  private void inOrderCollect(Node<T> node, CustomeLinkedList<T> list) {
    if (node == null) return;
    inOrderCollect(node.left, list);
    list.add(node.data);
    inOrderCollect(node.right, list);
  }
}
