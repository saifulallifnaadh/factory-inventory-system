import java.util.ArrayList;

public class Stack<E> {
    private ArrayList<E> stack;
    
    // create an empty stack
    public Stack() {
        stack = new ArrayList<>();
    }

    // return true if stack has no elements
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // insert item onto the top of the stack
    public E push(E item) {
        stack.add(item);
        return item;
    }

    // remove and return the top item
    public E pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return stack.remove(stack.size() - 1);
    }

    // return the top item without removing it
    public E peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return stack.get(stack.size() - 1);
    }
}