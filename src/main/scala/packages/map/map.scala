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

  /** le chemin où les monstrers peuvent se déplacer*/
  var path : Array[Position] = {new Array[Position] (0)}

  /** là où sont stockées les images associées aux tours*/
  var tower_resources : Array[String] = {Array("tower1.png","tower2.png")}

  /** Calcule le chemin à partir d'une position jusqu'à la fin (appelé au début du round)*/
  def compute_path () : Array[Position] = {new Array [Position] (0)}

  /** Supprime le monstre monster de la 1ere position et l'ajoute sur la deuxieme*/
  def move_monster (monster:Monster) (p1:Position) (p2:Position) = {}

  /** */
  def remove_monster (monster:Monster) = {}

  /** Donne la case suivante d'un monster à partir du chemin*/
  def next_case (p:Position) : Position = {new Position (0,0)}

  /** */
  def get_targets (tower:Tower) : Array[Monster] = {new Array[Monster] (0)}

  /** */
  def new_tower (t:TowerType) (p:Position) : Boolean = {true}

  /** */
  def free_for_tower (p:Position) : Boolean = {true /* la poisition n'est pas dans le chemin */}
}
// get_targets (Tour (range,position)) Vector(2 pts) (norme)
