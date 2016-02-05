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

  /** la carte ground représente les obstacles, i.e. ce qui ne bouge a priori pas */
  var ground = Array.ofDim[List[Tileable]](height,width)

  /** la carte towers représente les tours (une tour ou liste vide)*/
  var towers = Array.ofDim[List[TowerType]](height,width)

  /** la carte monsters représente les montres, il peut y en avoir plusieurs sur une même case,
    * d'où l'ensemble (Set) de monstres par case
    */
  var monsters = Array.ofDim[Set[Monster]](height,width)

  /** le chemin où les monstrers peuvent se déplacer */
  var path : Array[Position] = {compute_path()}

  /** là où sont stockées les images associées aux tours*/
  var tower_resources : Array[String] = {Array("tower1.png","tower2.png")}

  /** Calcule le chemin à partir d'une position jusqu'à la fin (appelé au début du round) */
  def compute_path () : Array[Position] = {
      var path = new Array[Position](width)
      var c = 0;
      for( c <- 0 to (width-1)) {
        path(c) = new Position (height/2,c)
      }
      path
     }

  /** Supprime le monstre monster de la 1ere position et l'ajoute sur la deuxieme */
  def move_monster (monster:Monster,p1:Position,p2:Position) : Unit = {

  }

  /** supprime le monstre à la position p de la carte monsters*/
  def remove_monster (monster:Monster,p:Position)= {}

  /** ajoute un monstre à la position p de la carte monsters*/
  def add_monster (monster:Monster,p:Position) = {

  }

  /** Donne la case suivante d'un monster à partir du chemin*/
  def next_case (p:Position) : Position = {new Position (0,0)}

  /** renvoie un tableau de monsters susceptibles d'être touchés par une tour donnée*/
  def get_targets (tower:Tower) : Array[Monster] = {new Array[Monster] (0)}

  /** Renvoie vrai et crée une nouvelle tour si possible, sinon renvoie faux*/
  def new_tower (t:TowerType,p:Position) : Boolean = {true}

}
// get_targets (Tour (range,position)) Vector(2 pts) (norme)
