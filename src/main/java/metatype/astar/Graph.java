package metatype.astar;

/**
 * Defines the minimal subset of a directed, weighted graph needed to perform
 * routing.
 * 
 * @author metatype
 * 
 * @param <V>
 *          the vertex type
 * @param <E>
 *          the edge type
 */
public interface Graph<V, E> {
  /**
   * Provides a standard definition of an heuristic estimate.
   * 
   * @param <V>
   *          the vertex type
   */
  interface Heuristic<V> {
    /**
     * Returns an estimate of the weight from one vertex to another. For this
     * estimate to be useful (admissible) it must not over-estimate the
     * distance.
     * 
     * @param from
     *          the starting vertex
     * @param to
     *          the ending vertex
     * @return the estimate of the weight
     */
    double estimate(V from, V to);
  }

  /**
   * Returns the heuristic function for estimating node distance.
   * 
   * @return the heuristic
   */
  Heuristic<V> getHeuristic();

  /**
   * Returns the outbound edges of a source vertex.
   * 
   * @param source
   *          the vertex
   * @return the ourgoing edges
   */
  Iterable<E> outboundEdges(V source);

  /**
   * Returns the destination endpoint of a directed edge.
   * 
   * @param edge
   *          the edge
   * @return the target
   */
  V getTarget(E edge);

  /**
   * Returns the weight of the given edge.
   * 
   * @param edge
   *          the edge
   * @return the edge weight
   */
  double getWeight(E edge);

  /**
   * Sets the edge weight.
   * 
   * @param edge
   *          the edge
   * @param w
   *          the weight
   */
  void setWeight(E edge, double w);
}
