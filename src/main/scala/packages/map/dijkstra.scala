package packages
package dijkstra

import scala.collection.mutable._
import graph._
import sugar._

class Dijkstra (graph: WeightedGraph) {
  type Node = WeightedGraph#Node
  type Edge = WeightedGraph#Edge
  /**
   * StopCondition provides a way to terminate the algorithm at a certain
   * point, e.g.: When target becomes settled.
   */
  type StopCondition = (Set[Node], Map[Node, Int], Map[Node, Node])
  	=> Boolean

  /**
   * By default the Dijkstra algorithm processes all nodes reachable from
   * <code>start</code> given to <code>compute()</code>.
   */
  val defaultStopCondition: StopCondition = (_, _, _) => true
  var stopCondition = defaultStopCondition

  def compute(start: Node, target: Node):
	  List[Node] = {
    var queue: Set[Node] = new HashSet()
    var settled: Set[Node] = new HashSet()
    var distance: Map[Node, Int] = new HashMap()
    var path: Map[Node, Node] = new HashMap()
    queue += start
    distance(start) = 0

    while(!queue.isEmpty && stopCondition(settled, distance, path)) {
      val u = extractMinimum(queue, distance)
      settled += u
      relaxNeighbors(u, queue, settled, distance, path)
    }
    val path_list = mapToList(path,start,target)
    return path_list
  }


  /**
   * Finds element of <code>Q</code> with minimum value in D, removes it
   * from Q and returns it.
   */
  protected def extractMinimum[T](Q: Set[T], D: Map[T, Int]): T = {


    var u = Q.head
    Q.foreach((node) =>  if(D(u) > D(node)) u = node)
    Q -= u
    return u;
  }

  /**
   * For all nodes <code>v</code> not in <code>S</code>, neighbors of
   * <code>u</code>: Updates shortest distances and paths, if shorter than
   * the previous value.
   */
  protected def relaxNeighbors(u: Node, Q: Set[Node], S: Set[Node],
      D: Map[Node, Int], P: Map[Node, Node]): Unit = {
      for(edge <- graph.edges if(edge.a == u || edge.b == u) ) {
        var v = if(edge.a == u) edge.b else edge.a
        if(!S.contains(v)) {
          if(!D.contains(v) || D(v) > D(u) + edge.getWeight) {
            D(v) = D(u) + edge.getWeight
            P(v) = u
            Q += v
          }
        }
      }
  }


  def print_list (liste : List[Node]) : Unit = {
    liste.foreach {x => println(x.pos.l) ; println(x.pos.c)}
  }

  protected def mapToList(table :Map[Node,Node],start:Node,target:Node) : List[Node] = {
    var res = List[Node](target)
    var current_node = target
    while (current_node != start) {
      println(current_node.pos.l + ";" + current_node.pos.c)
      current_node = table(current_node)
      res = current_node :: res
    }
    println("**************")
    res.reverse
  }

}

object Dijkstra_algo {
  def compute_dijkstra(g: WeightedGraph,begin: WeightedGraph#Node,end: WeightedGraph#Node):
    List[WeightedGraph#Node] = {
    // Set start, target, stop-condition and compute the path
    val (start, target) = (begin, end)
    val dijkstra2 = new Dijkstra(g)
    //  Halt when target becomes settled
    dijkstra2.stopCondition = (S, D, P) => !S.contains(target)
    dijkstra2.compute(start, target)
  }
}
