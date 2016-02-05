package packages
package map

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
  var path : Array[Position] = {new Array[Position] (0)}

  /** */
  var tower_resources : Array[String] = {Array("tower1.png","tower2.png")}

  /** */

  /** */
  def compute_path () : Array[Position] = {new Array [Position] (0)}

  /** */
  def move_monster (monster:Monster) = {}

  /** */
  def remove_monster (monster:Monster) = {}

  /** */
  def next_case (p:Position) : Position = {new Position (0,0)}

  /** */
  def get_targets (tower:Tower) : Array[Monster] = {new Array[Monster] (0)}

  /** */
  def new_tower (t:TowerType) (p:Position) : Boolean = {true}

  /** */
  def free_for_tower (p:Position) : Boolean = {true /* la poisition n'est pas dans le chemin */}
}
// get_targets (Tour (range,position)) Vector(2 pts) (norme)
