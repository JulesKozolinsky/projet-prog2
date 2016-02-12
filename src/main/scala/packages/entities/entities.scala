package packages
package entities

import sugar._
import map._

/** Les Tileable sont l'ensembles de éléments susceptibles d'apparaître sur la map : tours, monstres, cailloux, arbres, alliés éventuels... */
abstract class Tileable ()
{
  /** Coordonnées de l'objet */
  var pos : Position
}


/** Les Actor sont ceux parmi les Tileable qui sont appelés dans l'éxécution d'un round, essentiellement les tours et les monstres */
abstract class Actor() extends Tileable
{
  /** temps depuis lequel l'Actor n'a pas agit */
  var wait_since : Int
}


/** Les Moveable sont parmi les Actor ceux qui bougent (essentiellement les monstres) */
abstract class Moveable () extends Actor
{
  /** la lenteur d'un monstre est le nombre de tick entre deux déplacements */
  var slowness : Int
}


/** Les différents types de tour  */

abstract class TowerType
case class Tower1Type extends TowerType {def get_instance (pos:Position) = new Tower1(pos)}
case class Tower2Type extends TowerType {def get_instance (pos:Position) = new Tower2(pos)}



/** Classe abstraite des tours */
abstract class Tower () extends Actor
{
  /** la fréquence d'une tour est le nombre de tick entre deux tirs */
  val frequency : Int

  /** la priorité d'une tour est une fonction qui prend en entrée les monstres accessibles par la tour et renvoie la liste des monstres attaqués par la tour */
  val priority : (List[Monster]) => List[Monster]

  /** la portée d'une tour détermine la distance maximale à laquelle elle peut tirer */
  val range : Int 

  /** le prix d'une tour */
  val price : Int 

  /** la puissance de feu d'une tour */
  val power : Int 


  /** le apply des tours est le fait de tirer sur un/des monstres ; 
    * apply renvoie la liste des monstres tués par la tour
    * @param pot_targets est un tableau contenant toutes les cibles potentielles (que la tour a en visu) ; si le tableau est non vide et  : si pas de cible en vue la tour ne tire pas et on ne remet pas son wait_since à zéro, on n'appelle pas non plus apply
    */
  def apply : List[Monster] = {
    var L : List[Monster] = List()
    if (this.wait_since == this.frequency)
    {
      var targets = this.priority ( Map.get_targets(this) )
      if (targets.isEmpty)
      {
        this.wait_since = math.min(frequency,(1+this.wait_since))
      }
      else
      {
        targets.foreach ((m:Monster) =>
          {
            m.receive_damages (this.power)
            if (m.life == 0)
              {
                L=m::L
              }
          })

        this.wait_since = 0
      }
    }
    else
    {
      this.wait_since = 1+this.wait_since
    }
    L
  }

}


class Tower1 (position:Position) extends Tower 
  {
    val frequency =  10
    val priority = lazi _
    val range = 3
    val price = 20
    val power = 5
    var pos = position
    var wait_since = 0
  }

class Tower2 (position:Position) extends Tower
  {
    val frequency =  8
    val priority = lazi _
    val range = 3
    val price = 35
    val power = 6
    var pos = position
    var wait_since = 0
  }


/** Les Living sont parmi les Moveable ceux qui sont dotés de vie (monstres, éventuellement héros) */
abstract class Living () extends Moveable
{
  /** les Living sont dotés d'un nombre fini de points de vie */
  var life : Int

  /** fonction retirant une quantité de point de vie au Living */
  def receive_damages(dam:Int):Unit = {this.life = math.max (0,this.life - dam)}
}



abstract class MonsterType
case class Monster1 extends MonsterType
case class Monster2 extends MonsterType


/** Les Monstres sont les ennemis */
class Monster (taillpe : MonsterType) extends Living
{
  /** butin obtenu lors de la mort du monstre */
  var gold = 6

  var slowness = 12
  var pos = new Position (4,0)
  var life = 40
  var wait_since = 0

  taillpe match {
    case Monster1() => {
      var gold = 6
      var slowness = 12
      var life = 40
    }
    case Monster2() => {
      var gold = 8
      var slowness = 12
      var life = 50
    }
  }

  /** le apply du monstre renvoie True dans le cas où il est parvenu en fin de map et fait ainsi perdre une vie au joueur */
  def apply () : Boolean  = {
    var x = false
    if (this.wait_since == this.slowness)
    {
      if ((this.pos).c == (Map.height-1))
      {
        x = true
      }
      else
      {
        Map.move_monster (this,this.pos,Map.next_case(this.pos))
        this.pos = Map.next_case(this.pos)
        this.wait_since = 0
      }
    }
    else
    {
      this.wait_since = this.wait_since + 1
    }
    x
  }

}
