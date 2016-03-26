package packages

import entities._
import sugar._

/** Contient la carte du jeu surlaquelle évoluent les Tileable (i.e. les monstres, les tours, mais aussi des obstacles) */
package object map
{
  /** initialise la matrice ground */
  def initialize_matrix_ground (n:Int,p:Int) = {
    var matrix = Array.ofDim[List[Tileable]](n,p)
    for ( l <- 0 to (n-1) ; c <- 0 to (p-1) ) {
      matrix(l)(c) = List[Tileable]()
    }
    matrix
  }

  /** initialise la matrice towers */
  def initialize_matrix_towers (n:Int,p:Int) = {
    var matrix = Array.ofDim[List[Tower]](n,p)
    for ( l <- 0 to (n-1) ; c <- 0 to (p-1) ) {
      matrix(l)(c) = List[Tower]()
    }
    matrix
  }

  /** initialise la matrice monsters */
  def initialize_matrix_monsters (n:Int,p:Int) = {
    var matrix = Array.ofDim[scala.collection.mutable.Set[Monster]](n,p)
    for ( l <- 0 to (n-1) ; c <- 0 to (p-1) ) {
      matrix(l)(c) = scala.collection.mutable.Set[Monster]()
    }
    matrix
  }

  /** initialise le chemin */
  def initialize_path (n:Int) = {
    var tab1 = new Array[Array[List[Position]]](n)
    for (l <- 0 to (n-1)) {
      tab1(l) = new Array[List[Position]](0)
    }
    tab1
  }
}
