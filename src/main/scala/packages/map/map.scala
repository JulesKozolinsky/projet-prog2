package packages
package game

import entities._
import sugar._

/** Crée l'object Map, qui gère le déplacement des monstres */
object Map
{
  /** hauteur de la carte */
  val height : Int = 9

  /** largeur de la carte */
  val width : Int = 21

  /** la carte ground représente les tours, les obstacles, i.e. ce qui ne bouge a priori pas */
  var ground = Array.ofDim[List[Tileable]](height,width)

  /** la carte monsters représente les montres, il peut y en avoir plusieurs sur une même case,
    * d'où l'ensemble (Set) de monstres par case
    */
  var monsters = Array.ofDim[Set[Moveable]](height,width)

  /** */
  def compute_path () : Array[Position] = {new Array [Position] (0)}

  /** */
  def move_monster (monster:Monster) = {}

  /** */
  def remove_monster (monster:Monster) = {}

  /** */
  def next_case (p:Position) : Position = {new Position (0,0)}
}
