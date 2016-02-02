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

  /** fonction qui fait agir (avancer ou tirer) l'Actor */
  def apply : Unit
}


/** Les Moveable sont parmi les Actor ceux qui bougent (essentiellement les monstres) */
abstract class Moveable () extends Actor
{
  /** la lenteur d'un monstre est le nombre de tick entre deux déplacements */
  var slowness : Int
}


abstract class TowerType
case class Tower1 extends TowerType
case class Tower2 extends TowerType

class Tower (taillpe : TowerType, ligne : Int, colonne : Int) extends Actor
{
  /** la fréquence d'une tour est le nombre de tick entre deux tirs */
  var frequency : Int = 10

  /** la priorité d'une tour détermine la manière dont elle choisit le monstre sur lequel elle tire */
  var priority : Int = 0

  /** la portée d'une tour détermine la distance maximale à laquelle elle peut tirer */
  var range : Int = 3

  /** le prix d'une tour est ... son prix */
  var price : Int = 35

  /** la puissance de feu d'une tour */
  var power : Int = 6

  /** la position où l'on place la tour */
  var pos = new Position (ligne, colonne)

  /** le compteur d'activité */
  var wait_since = 0

  taillpe match {
    case Tower1() => {
      this.frequency =  10
      this.priority = 0
      this.range = 3
      this.price = 20
      this.power = 5
    }
    case Tower2() => {
      this.frequency =  12
      this.priority = 0
      this.range = 3
      this.price = 35
      this.power = 6
    }
  }

  /** le apply des tours est le fait de tirer sur un/des monstres
    * @param pot_targets est un tableau contenant toutes les cibles que la tour a en visu ; si le tableau est non vide et  : si pas de cible en vue la tour ne tire pas et on ne remet pas son wait_since à zéro, on n'appelle pas non plus apply
    */
  def apply  : Unit = {
    if (this.wait_since == this.frequency)
    {
      var pot_targets = new Array [Monster] (0) /* pour l'instant, ensuite on utilisera map.get_targets(this) */
      if (pot_targets.isEmpty)
      {
        this.wait_since = math.min(frequency,(1+this.wait_since))
      }
      else
      {
        (pot_targets(0)).receive_damages (this.power)
        this.wait_since = 0
      }
    }
    else
    {
      this.wait_since = 1+this.wait_since
    }
  }

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
  def apply () : Unit = {
    if (this.wait_since == this.slowness)
      {
        /*this.pos = Map.next_case(this.pos)*/
        this.wait_since = 0
      }
    else
    {
      this.wait_since = this.wait_since + 1
    }
  }

}
