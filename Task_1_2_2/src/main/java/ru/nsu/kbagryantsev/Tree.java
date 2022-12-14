package ru.nsu.kbagryantsev;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

/**
 * Tree implementation of Collections interface.
 *
 * @param <T> any type
 */
public class Tree<T> implements Collection<T> {
    /**
     * Node's data.
     */
    private T data;
    /**
     * Link to the parent's node.
     */
    private Tree<T> parent;
    /**
     * ArrayList of node's descendants.
     */
    private ArrayList<Tree<T>> children;
    /**
     * Increases if a subtree was modified.
     */
    private int modCount;

    /**
     * Generates an empty tree.
     */
    public Tree() {
        children = new ArrayList<>();
        parent = null;
        modCount = 0;
    }

    /**
     * Adds an element to the current node's children.
     *
     * @param t element whose presence in this collection is to be ensured
     * @return true if succeeded, false if not
     */
    @Override
    public boolean add(final T t) {
        if (data == null) {
            data = t;
        } else {
            Tree<T> child = new Tree<>();
            child.data = t;
            child.parent = this;
            this.children.add(child);
        }
        modCount++;
        return true;
    }

    /**
     * Adds an element to the given node's children. Unluckily not static.
     *
     * @param node node into t value should be added to
     * @param t    element whose presence in this collection is to be ensured
     * @return true if succeeded, false if not
     */
    public Tree<T> add(final @NotNull Tree<T> node, final T t) {
        Tree<T> child = new Tree<>();
        child.data = t;
        child.parent = node;
        node.children.add(child);
        modCount++;
        return child;
    }

    /**
     * Adds all elements from given collection into a tree's node.
     *
     * @param c collection containing elements to be added to this collection
     * @return true if all elements were added
     */
    @Override
    public boolean addAll(final @NotNull Collection<? extends T> c) {
        c.forEach(this::add);
        modCount++;
        return true;
    }

    /**
     * Iterates using bfs algorithm.
     * Suppression for the interface structure corruption.
     * Use BFS() and DFS() instead.
     *
     * @return iterator object
     */
    public Iterator<T> bfs() throws IllegalStateException {
        if (data == null) {
            throw new IllegalStateException();
        }
        return new Bfs();
    }

    /**
     * Clears the tree and makes it garbage collectable.
     */
    @Override
    public void clear() {
        data = null;
        children = new ArrayList<>();
        modCount++;
    }

    /**
     * Tests the presence of a given element in a tree.
     *
     * @param o element whose presence in this collection is to be tested
     * @return true if contained
     */
    @Override
    public boolean contains(final Object o) {
        if (data == null) {
            return false;
        }
        if (children.contains(o) || data == o) {
            return true;
        }

        return children.stream().anyMatch(x -> x.contains(o));
    }

    /**
     * Checks whether all elements from c collection are contained in a tree.
     *
     * @param c collection to be checked for containment in this collection
     * @return true if succeeded, false if not
     */
    @Override
    public boolean containsAll(final @NotNull Collection<?> c) {
        if (data == null) {
            return false;
        }
        for (Object element : c) {
            if (!this.contains(element)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Iterates over a tree using depth-first-search.
     *
     * @return iterator over a tree
     */
    public Iterator<T> dfs() throws IllegalStateException {
        if (data == null) {
            throw new IllegalStateException();
        }
        return new Dfs();
    }

    /**
     * Checks whether a tree is empty.
     *
     * @return true if empty
     */
    @Override
    public boolean isEmpty() {
        return data == null;
    }

    /**
     * Iterates via BFS() auxiliary method.
     * Suppression for the interface structure corruption.
     * Use BFS() and DFS() instead.
     *
     * @return iterator object
     */
    @Override
    public Iterator<T> iterator() throws IllegalStateException {
        if (data == null) {
            throw new IllegalStateException();
        }
        return new Bfs();
    }

    /**
     * Removes all nodes equal to given data.
     *
     * @param o element to be removed from this collection, if present
     * @return true if something was removed
     */
    @Override
    public boolean remove(final Object o) {
        if (data == null) {
            return true;
        }
        if (parent == null && data == o) {
            data = null;
            children = new ArrayList<>();
            modCount++;
            return true;
        }
        boolean state;
        state = children.removeIf(node -> node.data == o);
        for (Tree<T> child : children) {
            if (child.remove(o)) {
                state = true;
            }
        }
        modCount++;
        return state;
    }

    /**
     * Removes all elements from the tree that are contained in c.
     *
     * @param c elements to be removed from the tree
     * @return true if succeeded, false if not
     */
    @Override
    public boolean removeAll(final @NotNull Collection<?> c) {
        if (data == null) {
            return true;
        }
        c.forEach(this::remove);
        modCount++;
        return true;
    }

    /**
     * Removes all elements, which are not contained in c.
     *
     * @param c collection containing elements to be retained in this collection
     * @return true if all operations were executed successfully
     */
    @Override
    public boolean retainAll(final @NotNull Collection<?> c) {
        Object[] tree = this.toArray();
        for (Object element : tree) {
            if (!c.contains(element)) {
                this.remove(element);
            }
        }
        modCount++;
        return true;
    }

    /**
     * Evaluates size of a tree recursively via Stream API
     * by calling Tree.size() method for each element in node's children
     * and summing it.
     *
     * @return amount of the elements in a tree
     */
    @Override
    public int size() {
        if (parent == null && data == null) {
            return 0;
        }
        int recursiveSize = children.stream().mapToInt(Tree::size).sum();
        int childrenSize = children.size();
        int size = recursiveSize + childrenSize;
        return parent == null ? size + 1 : size;
    }

    /**
     * Returns array of values in a tree in reverse polish notation.
     *
     * @return array of values
     */
    @Override
    public Object[] toArray() {
        if (data == null) {
            return new Object[]{};
        }
        Iterator<T> iterator = this.iterator();

        Object[] returnArray = new Object[this.size()];

        for (int i = 0; iterator.hasNext(); i++) {
            returnArray[i] = iterator.next();
        }

        return returnArray;
    }

    /**
     * ???.
     *
     * @param a    the array into which the elements of this collection
     *             are to be stored, if it is big enough;
     *             otherwise, a new array of the same
     *             runtime type is allocated for this purpose.
     * @param <T1> ???
     * @return ???
     */
    @Override
    public <T1> T1[] toArray(final T1 @NotNull [] a) {
        throw new UnsupportedOperationException("toArray");
    }

    /**
     * Iterator class implementing breadth-first-search in a tree.
     */
    public class Bfs implements Iterator<T> {
        /**
         * Queue to hold nodes while iterating over a tree.
         */
        private final ArrayDeque<Tree<T>> queue;
        /**
         * Freezes modCount value of a tree
         * at the moment of iterator's creation.
         */
        private final int expectedModCount;

        /**
         * Initialises breadth-first-search with tree's root node.
         */
        public Bfs() {
            queue = new ArrayDeque<>();
            queue.addFirst(Tree.this);
            expectedModCount = Tree.this.modCount;
        }

        /**
         * Checks whether a node has next node or not.
         *
         * @return true if it does
         */
        @Override
        public boolean hasNext() throws ConcurrentModificationException {
            if (expectedModCount != Tree.this.modCount) {
                throw new ConcurrentModificationException();
            }
            return !queue.isEmpty();
        }

        /**
         * Gets the next element in a tree.
         *
         * @return next element
         */
        @Override
        public T next() throws NoSuchElementException {
            if (expectedModCount != Tree.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Tree<T> current = queue.removeFirst();
            queue.addAll(current.children);
            return current.data;
        }
    }

    /**
     * Iterator class implementing depth-first-search in a tree.
     */
    public class Dfs implements Iterator<T> {
        /**
         * Stores information about visited nodes.
         */
        private final ArrayList<Tree<T>> visited;
        /**
         * Nodes awaiting to be visited.
         */
        private final ArrayDeque<Tree<T>> stack;
        /**
         * Freezes modCount value of a tree
         * at the moment of iterator's creation.
         */
        private final int expectedModCount;

        /**
         * Initialises class field and adds a tree root into the stack.
         */
        public Dfs() {
            visited = new ArrayList<>();
            stack = new ArrayDeque<>();
            stack.addFirst(Tree.this);
            expectedModCount = Tree.this.modCount;
        }

        /**
         * Checks whether a node has next node or not.
         *
         * @return true if it does
         */
        @Override
        public boolean hasNext() throws ConcurrentModificationException {
            if (expectedModCount != Tree.this.modCount) {
                throw new ConcurrentModificationException();
            }
            return !stack.isEmpty();
        }

        /**
         * Gets the next element in a tree.
         *
         * @return next element
         */
        @Override
        public T next()
                throws NoSuchElementException, ConcurrentModificationException {
            if (expectedModCount != Tree.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Tree<T> node = stack.removeFirst();
            visited.add(node);
            for (int i = node.children.size() - 1; i >= 0; i--) {
                Tree<T> elem = node.children.get(i);
                if (!visited.contains(elem)) {
                    stack.addFirst(node.children.get(i));
                }
            }
            return node.data;
        }
    }
}
