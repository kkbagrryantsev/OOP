package ru.nsu.kbagryantsev;

import java.util.Collection;

/**
 * Directed weighted graph interface.
 *
 * @param <V> vertex data
 * @param <E> edge data
 */
public interface Graphable<V, E extends Number> {
    /**
     * Adds a vertex by its data.
     *
     * @param data vertex data
     * @return link to an inserted vertex
     */
    Vertex<V> addVertex(V data);

    /**
     * Adds an existent vertex.
     *
     * @param v vertex instance
     * @return graph object for method chaining
     */
    Graphable<V, E> addVertex(Vertex<V> v);

    /**
     * Removes a vertex from the graph.
     * Also removes all adjacent edges of a given vertex.
     *
     * @param v vertex instance
     */
    void cutVertex(Vertex<V> v);

    /**
     * Gets a collection of vertices in a graph.
     *
     * @return collection of vertices
     */
    Collection<Vertex<V>> getVertices();

    /**
     * Adds an edge by its data.
     *
     * @param u initial vertex
     * @param v terminal vertex
     * @param weight weight of an edge
     * @return link to an inserted edge
     */
    Edge<V, E> addEdge(Vertex<V> u, Vertex<V> v, E weight);

    /**
     * Removes an edge by its instance.
     *
     * @param e edge instance
     */
    void cutEdge(Edge<V, E> e);

    /**
     * Removes an edge by its terminations.
     *
     * @param u from-vertex
     * @param v to-vertex
     */
    void cutEdge(Vertex<V> u, Vertex<V> v);
}
