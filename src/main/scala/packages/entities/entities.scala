package packages
package entities

import sugar._

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
  val slowness : Int

  /** le act des moveable est le fait de bouger */
  def apply : Unit = {}
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

  var pos = new Position (ligne, colonne)
  var wait_since = 0

  /** le act des tours est le fait de tirer sur un/des monstres */
  def apply : Unit = {}

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
      this.priority = 1
      this.range = 3
      this.price = 35
      this.power = 6
    }
  }

}


/** Les Living sont parmi les Moveable ceux qui sont dotés de vie (monstres, éventuellement héros) */
abstract class Living () extends Moveable
{
  /** les Living sont dotés d'un nombre fini de points de vie */
  var life : Int

  /** fonction retirant une quantité de point de vie au Living */
  def receive_damages(dam:Int):Unit = {}
}


/** les Enemy sont les monstres */
abstract class Enemy () extends Living
{
  /** quantité d'argent reçue par le joueur en tuant ce monstre */
  val gold : Int
}


class Monster1 () extends Enemy
{
  val gold = 6
  val slowness = 12
  var pos = new Position(4,0)
  var life = 40
  var wait_since = 0
}

class Monster2 () extends Enemy
{
  val gold = 8
  val slowness = 12
  var pos = new Position(4,0)
  var life = 50
  var wait_since = 0
}
