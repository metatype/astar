package metatype.astar;

import java.util.HashSet;
import java.util.Set;

import metatype.astar.CartesianHeuristics.CartesianVertex;
import metatype.astar.SimpleGraph.Edge;
import metatype.astar.SimpleGraph.Vertex;

public class SimpleGraph implements Graph<Vertex, Edge> {
  
  public static class Vertex implements CartesianVertex {
    private final double x;
    private final double y;
    private final Set<Edge> edges;
    
    public Vertex(double x, double y) {
      this.x = x;
      this.y = y;
      edges = new HashSet<Edge>();
    }
    
    public Vertex addEdges(Edge... outbound) {
      for (Edge e : outbound) {
        assert e.src == this;
        edges.add(e);
      }
      
      return this;
    }

    @Override
    public double getX() {
      return x;
    }

    @Override
    public double getY() {
      return y;
    }
    
    @Override
    public String toString() {
      return String.format("(%f,%f)", x, y);
    }
  }
  
  public static class Edge {
    private final Vertex src;
    private final Vertex dest;
    private double weight;
  
    public Edge(Vertex src, Vertex dest, double w) {
      this.src = src;
      this.dest = dest;
      this.weight = w;
    }
    
    @Override
    public String toString() {
      return String.format("%s --- [%f] ---> %s", src, weight, dest);
    }
  }

  private final Heuristic<Vertex> estimator;
  
  public SimpleGraph(Heuristic<Vertex> heuristic) {
    this.estimator = heuristic;
  }
  
  @Override
  public Heuristic<Vertex> getHeuristic() {
    return estimator;
  }

  @Override
  public Iterable<Edge> outboundEdges(Vertex source) {
    return source.edges;
  }

  @Override
  public Vertex getTarget(Edge edge) {
    return edge.dest;
  }

  @Override
  public double getWeight(Edge edge) {
    return edge.weight;
  }
  
  @Override
  public void setWeight(Edge edge, double w) {
    edge.weight = w;
  }
  
  public double cost(Iterable<Edge> path) {
    double cost = 0;
    for (Edge e : path) {
      cost += e.weight;
    }
    return cost;
  }
}
