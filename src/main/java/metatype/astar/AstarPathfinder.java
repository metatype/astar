package metatype.astar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Finds the shortest distance between two points using the A* search
 * algorithm.  Uses an heuristic estimate to improve the routing speed and prune
 * the search space.
 * 
 * @see http://en.wikipedia.org/wiki/A*_search_algorithm
 * 
 * @author metatype
 * 
 * @param <V>
 *          the vertex type
 * @param <E>
 *          the edge type
 */
public class AstarPathfinder<V, E> {
  /**
   * Finds the shortest path between the start and end vertexes.
   * 
   * @param graph
   *          the graph to search
   * @param start
   *          the starting vertex
   * @param end
   *          the ending vertex
   * 
   * @return the edges comprising the path, or {} if no path exists
   */
  public Iterable<E> route(Graph<V, E> graph, V start, V end) {
    Node goal = new AstarPathfinder<V, E>().findShortestPath(new Path(
        graph, start, end));
    
    LinkedList<E> path = new LinkedList<E>();
    if (goal != null) {
      while (goal.edge != null) {
        path.addFirst(goal.edge);
        goal = goal.prev;
      }
    }

    return path;
  }
  
  private Node findShortestPath(Path r) {
    // track queue insertions to improve path selection
    int order = 0;
    
    // nodes that have already been visited
    Set<Node> closed = new HashSet<Node>();
    
    // nodes that are candidates for exploration
    PriorityQueue<Node> open = new PriorityQueue<Node>();
    
    Node n = r.getNode(r.start);
    n.reset(null, null, 0, order++);
    open.add(n);
    
    // the path cost
    double g, f = 0;
    
    Node current = n;
    while (!open.isEmpty()) {
      current = open.remove();
      
      // all done!
      if (current.vertex == r.end) {
        return current;
      }
      
      closed.add(current);
      for (E edge : r.graph.outboundEdges(current.vertex)) {
        n = r.getNode(r.graph.getTarget(edge));

        g = current.g + r.graph.getWeight(edge);
        f = g + n.h;
        
        // we already found a better path
        if (closed.contains(n) && f >= n.f()) {
          continue;
        }
        
        // need to explore this alternative
        if (!open.contains(n) || f < n.f()) {
          open.remove(n);
          n.reset(current, edge, g, order++);
          open.add(n);
        }
      }
    }
    return null;
  }

  private class Path {
    private final Graph<V, E> graph;
    private final V start;
    private final V end;

    private final Map<V, Node> nodes;

    public Path(Graph<V, E> graph, V start, V end) {
      assert graph != null;
      assert start != null;
      assert end != null;

      this.graph = graph;
      this.start = start;
      this.end = end;

      nodes = new HashMap<V, Node>();
    }
    
    private Node getNode(V vertex) {
      Node n = nodes.get(vertex);
      if (n == null) {
        double h = graph.getHeuristic().estimate(vertex, end);
        n = new Node(vertex, h);
        
        nodes.put(vertex, n);
      }
      return n;
    }
  }
  
  private class Node implements Comparable<Node> {
    private final V vertex;
    private final double h;
    
    private Node prev;
    private E edge;
    private double g;
    private int order;
    
    public Node(V vertex, double h) {
      this.vertex = vertex;
      this.h = h;
    }

    private void reset(Node prev, E edge, double g, int order) {
      this.prev = prev;
      this.edge = edge;
      this.g = g;
      this.order = order;
    }
    
    private double f() {
      return g + h;
    }
    
    @Override
    public String toString() {
      return String.format("%s/%f", vertex, h);
    }
    
    @Override
    public int compareTo(Node o) {
      if (this == o) {
        return 0;
      }

      // compare total estimated cost
      int diff = (int) Math.signum(f() - o.f());
      
      // prefer shortest remaining cost
      if (diff == 0) {
        diff = (int) Math.signum(h - o.h);
      }
      
      // prefer most recently inserted
      if (diff == 0) {
        diff = order = o.order;
      }
      return diff;
    }
  }
}
