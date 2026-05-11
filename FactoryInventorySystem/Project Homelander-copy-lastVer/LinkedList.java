/**
Coder: Roslan S, UiTM Pahang, roslancs@uitm.edu.my
Year: 2012
Improved & Fixed Version
*/

public class LinkedList<E> {

    private Node<E> head, current, tail;

    public LinkedList() {
        head = current = tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    // ================= ADD =================

    public void addFirst(E data) {
        Node<E> newNode = new Node<>(data); // Node dipanggil di sini
        newNode.next = head;
        head = newNode;

        if (tail == null) {
            tail = head;
        }
    }

    public void addLast(E data) {
        Node<E> newNode = new Node<>(data);

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    public void add(int index, E data) {
        if (index < 0)
            throw new IndexOutOfBoundsException();

        Node<E> newNode = new Node<>(data);

        if (index == 0) {
            newNode.next = head;
            head = newNode;
            if (tail == null)
                tail = head;
            return;
        }

        Node<E> temp = head;
        int count = 0;

        while (temp != null && count < index - 1) {
            temp = temp.next;
            count++;
        }

        if (temp == null || temp.next == null) {
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
        } else {
            newNode.next = temp.next;
            temp.next = newNode;
        }
    }

    // ================= GET =================

    public E getFirst() {
        if (isEmpty()) return null;
        current = head;
        return current.data;
    }

    public E getLast() {
        if (isEmpty()) return null;
        return tail.data;
    }

    public E getNext() {
        if (current == null || current == tail) return null;
        current = current.next;
        return current.data;
    }

    // ================= REMOVE =================

    public E removeFirst() {
        if (isEmpty()) return null;
        current = head;
        head = head.next;
        if (head == null) tail = null;
        return current.data;
    }

    public E removeLast() {
        if (isEmpty()) return null;
        if (head == tail) {
            current = head;
            head = tail = null;
            return current.data;
        }
        Node<E> temp = head;
        while (temp.next != tail) {
            temp = temp.next;
        }
        Node<E> removed = tail;
        tail = temp;
        tail.next = null;
        return removed.data;
    }

    public E removeAfter(E data) {
        if (isEmpty()) return null;
        Node<E> temp = head;
        while (temp != null && !data.equals(temp.data)) {
            temp = temp.next;
        }
        if (temp == null || temp.next == null) return null;
        Node<E> removed = temp.next;
        temp.next = removed.next;
        if (removed == tail) tail = temp;
        return removed.data;
    }

    // ================= SEARCH & UTILITY =================

    public boolean contains(E data) {
        Node<E> temp = head;
        while (temp != null) {
            if (data.equals(temp.data)) return true;
            temp = temp.next;
        }
        return false;
    }

    public void clear() {
        head = current = tail = null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        if (isEmpty()) {
            result.append("The list is empty]");
            return result.toString();
        }
        Node<E> temp = head;
        while (temp != null) {
            result.append(temp.data);
            temp = temp.next;
            if (temp != null) result.append(", ");
            else result.append("]");
        }
        return result.toString();
    }

    // ================= CLASS NODE (PENYELESAIAN MASALAH) =================
    // Letak class ini di dalam LinkedList supaya tak error
    private static class Node<E> {
        E data;
        Node<E> next;

        public Node(E data) {
            this.data = data;
            this.next = null;
        }
    }
}