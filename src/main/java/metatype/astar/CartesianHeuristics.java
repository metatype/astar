package metatype.astar;

import metatype.astar.Graph.Heuristic;

/**
 * Defines functions for estimating distance on Cartesian graphs.
 * 
 * @author metatype
 */
public class CartesianHeuristics {
  /**
   * Defines a vertex positioned on a rectilinear coordinate system.
   */
  public interface CartesianVertex {
    /**
     * Returns the x-coordinate.
     * 
     * @return x
     */
    double getX();

    /**
     * Returns the y-coordinate.
     * 
     * @return y
     */
    double getY();
  }

  /**
   * Adds a tie-breaker to another estimate to prefer the current path when there
   * are many paths of similar costs to the goal.
   * 
   * @param <V>
   *          the vertex type
   */
  public static class Tiebreaker<V extends CartesianVertex> implements
      Heuristic<V> {
    private final double incr;
    private final Heuristic<V> delegate;

    public Tiebreaker(double incr, Heuristic<V> delegate) {
      this.delegate = delegate;
      this.incr = incr;
    }

    public double estimate(V from, V to) {
      double h = delegate.estimate(from, to);
      h += incr * Math.abs(from.getX() * to.getY() - to.getX() * from.getY());

      return h;
    }
  }

  /**
   * Estimates distance using a taxicab, or Manhattan, approximation.  Movement
   * is only allowed on Cartesian grid coordinates (N, S, E, w).
   * 
   * @param <V>
   *          the vertex type
   */
  public static class Taxicab<V extends CartesianVertex> implements
      Heuristic<V> {
    public double estimate(V from, V to) {
      return Math.abs(from.getX() - to.getX())
           + Math.abs(from.getY() - to.getY());
    }
  }

  /**
   * Estimates distance using a modified taxicab approach that allows diagonal
   * movement.
   * 
   * @param <V>
   *          the vertex type
   */
  public static class Diagonal<V extends CartesianVertex> implements
      Heuristic<V> {
    public double estimate(V from, V to) {
      return Math.max(Math.abs(from.getX() - to.getX()),
                      Math.abs(from.getY() - to.getY()));
    }
  }

  /**
   * Estimates distance using a straight line approximation.
   * 
   * @param <V>
   *          the vertex type
   */
  public static class Euclidean<V extends CartesianVertex> implements
      Heuristic<V> {
    public double estimate(V from, V to) {
      return Math.sqrt(Math.pow((from.getX() - to.getX()), 2)
                     + Math.pow((from.getY() - to.getY()), 2));
    }
  }
}
