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
  /** renvoie true si p1 et p2 représentent la même Position */
  def same_position (p1:Position,p2:Position) : Boolean =
    { p1.l == p2.l & p1.c == p2.c}
}
