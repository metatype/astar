package metatype.astar;

import junit.framework.TestCase;
import metatype.astar.CartesianHeuristics.Taxicab;
import metatype.astar.SimpleGraph.Edge;
import metatype.astar.SimpleGraph.Vertex;

public class AstarPathfinderTest extends TestCase {
  public void testRoute() {
    SimpleGraph g = new SimpleGraph(new Taxicab<Vertex>());
   
    int xdim = 100;
    int ydim = 100;
    
    Vertex[][] grid = new Vertex[xdim][ydim];
    for (int i = 0; i < xdim; i++) {
      for (int j = 0; j < ydim; j++) {
        grid[i][j] = new Vertex(i, j);
      }
    }
    
    // left + right
    for (int i = 0; i < xdim - 1; i++) {
      for (int j = 0; j < ydim; j++) {
        grid[i][j].addEdges(new Edge(grid[i][j], grid[i+1][j], 1));
        grid[i+1][j].addEdges(new Edge(grid[i+1][j], grid[i][j], 1));
      }
    }

    // up + down
    for (int i = 0; i < xdim; i++) {
      for (int j = 0; j < ydim - 1; j++) {
        grid[i][j].addEdges(new Edge(grid[i][j], grid[i][j+1], 1));
        grid[i][j+1].addEdges(new Edge(grid[i][j+1], grid[i][j], 1));
      }
    }

    // hill!
    for (int j = 0; j < ydim / 2; j++) {
      // -->
      for (Edge e : g.outboundEdges(grid[xdim/2][j])) {
        if (g.getTarget(e) == grid[xdim/2 + 1][j]) {
          g.setWeight(e, 2 * xdim);
        }
      }
      
      // <--
      for (Edge e : g.outboundEdges(grid[xdim/2 + 1][j])) {
        if (g.getTarget(e) == grid[xdim/2][j]) {
          g.setWeight(e, 2 * xdim);
        }
      }
    }
    
    Vertex ll = grid[0][0];
    Vertex ur = grid[xdim - 1][ydim - 1];
    Vertex lr = grid[xdim - 1][0];
    
    AstarPathfinder<Vertex, Edge> astar = new AstarPathfinder<Vertex, Edge>();
    Iterable<Edge> path = astar.route(g, ll, ur);
    assertEquals(xdim + ydim - 2.0, g.cost(path));

    path = astar.route(g, ll, lr);
    assertEquals(xdim + ydim - 1.0, g.cost(path));
  }
}
