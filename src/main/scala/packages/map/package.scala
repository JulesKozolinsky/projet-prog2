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
    var matrix = Array.ofDim[Set[Monster]](n,p)
    for ( l <- 0 to (n-1) ; c <- 0 to (p-1) ) {
      matrix(l)(c) = Set[Monster]()
    }
    matrix
  }

  /** initialise le chemin */
  def initialize_path (n:Int,p:Int) = {
    var matrix = Array.ofDim[Array[List[Position]]](n,p)
    for (l <- 0 to (n-1) ; c <- 0 to (p-1)) {
      matrix(l)(c) = new Array[List[Position]](0)
    }
    matrix
  }

  /** initialise les cases de départ */
  def initialize_origin (n : Int) : Set[Position] = {
   var set =  Set[Position]()
   for (k <- 0 to n-1) {
     set = set + (new Position(k,0))
   }
   set
 }

}
