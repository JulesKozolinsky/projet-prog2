package packages
package entities


/** Les Tileable sont l'ensembles de éléments susceptibles d'apparaître sur la map : tours, monstres, cailloux, arbres, alliés éventuels... */
abstract class Tileable ()
{
  /** Coordonnées de l'objet */
  var l : Int
  var c : Int
}


/** Les Actor sont ceux parmi les Tileable qui sont appelés dans l'éxécution d'un round, essentiellement les tours et les monstres */
abstract class Actor() extends Tileable
{
  /** temps depuis lequel l'Actor n'a pas agit */
  var wait_since : Int

  /** fonction qui fait agir (avancer ou tirer) l'Actor */
  def act : Unit
}


/** Les Moveable sont parmi les Actor ceux qui bougent (essentiellement les monstres) */
abstract class Moveable () extends Actor
{
  /** la lenteur d'un monstre est le nombre de tick entre deux déplacements */
  val slowness : Int

  /** le act des moveable est le fait de bouger */
  def act : Unit = {}
}

abstract class Tower () extends Actor
{
  /** la fréquence d'une tour est le nombre de tick entre deux tirs */
  val frequency : Int

  /** la priorité d'une tour détermine la manière dont elle choisit le monstre sur lequel elle tire */
  val priority : Int

  /** la portée d'une tour détermine la distance maximale à laquelle elle peut tirer */
  val range : Int

  /** le prix d'une tour est ... son prix */
  val price : Int

  /** la puissance de feu d'une tour */
  val power : Int

  /** le act des tours est le fait de tirer sur un/des monstres */
  def act : Unit = {}
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

/** Tour1 avec comme priorité le monstre le plus proche */
class Tower1 (ligne:Int,colonne:Int) extends Tower
{
  val frequency = 10
  val priority = 0
  val range = 3
  val price = 20
  val power = 5
  var l = ligne
  var c = colonne
  var wait_since = 0
}

/** Tour2 avec comme priorité le monstre le plus faible */
class Tower2 (ligne:Int,colonne:Int) extends Tower
{
  val frequency = 12
  val priority = 1
  val range = 3
  val price = 35
  val power = 6
  var l = ligne
  var c = colonne
  var wait_since = 0
}

class Monster1 () extends Enemy
{
  val gold = 6
  val slowness = 12
  var l = 4
  var c = 0
  var life = 40
  var wait_since = 0
}

class Monster2 () extends Enemy
{
  val gold = 8
  val slowness = 12
  var l = 4 
  var c = 0
  var life = 50
  var wait_since = 0
}
