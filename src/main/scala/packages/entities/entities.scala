package packages
package entities

import sugar._
import map._

abstract class TileableType () {}

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
  val slowness : Int
}


/** Les différents types de tour  */

abstract class TowerType extends TileableType { def get_instance (pos:Position) : Tower}
case object Tower1Type extends TowerType {def get_instance (pos:Position) = new Tower1(pos)}
case object Tower2Type extends TowerType {def get_instance (pos:Position) = new Tower2(pos)}



/** Classe abstraite des tours */
abstract class Tower () extends Actor
{
  /** la fréquence d'une tour est le nombre de tick entre deux tirs */
  val frequency : Int

  /** Type de la tour */
  val tower_type : TowerType

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
    * targets est un tableau contenant toutes les cibles sur qui la tour va tirer ; si le tableau est vide le wait_since peut augmenter mais ne retombe pas à 0
    */
  def apply () : List[Monster] = {
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
    val tower_type = Tower1Type
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
    val tower_type = Tower2Type
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



abstract class MonsterType extends TileableType {def get_instance () : Monster }
case object Monster1Type extends MonsterType {def get_instance () = new Monster1()}
case object Monster2Type extends MonsterType {def get_instance () = new Monster2()}
case object Monster3Type extends MonsterType {def get_instance () = new Monster3()} 
case object Monster4Type extends MonsterType {def get_instance () = new Monster4()}
case object Monster5Type extends MonsterType {def get_instance () = new Monster5()}
case object Monster6Type extends MonsterType {def get_instance () = new Monster6()}



/** Les Monstres sont les ennemis */
abstract class Monster () extends Living
{
  /** butin obtenu lors de la mort du monstre */
  val gold : Int

  var life : Int
  var wait_since : Int

  val monster_type : MonsterType

  /** le apply du monstre le fait bouger s'il est temps et renvoie True dans le cas où il est parvenu en fin de map et fait ainsi perdre une vie au joueur */
  def apply () : Boolean  = {
    var x = false
    if (this.wait_since == this.slowness)
    {
      if ((this.pos).c == (Map.width-1))
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


class Monster1 () extends Monster {
  val gold = 6
  val slowness = 12
  var wait_since = 0
  var pos = new Position (Map.height / 2,0)
  var life = 40
  val monster_type = Monster1Type
}

class Monster2 () extends Monster {
  val gold = 8
  val slowness = 10
  var wait_since = 0
  var pos = new Position (Map.height / 2,0)
  var life = 50
  val monster_type = Monster2Type
}
class Monster3 () extends Monster {
  val gold = 8
  val slowness = 10
  var wait_since = 0
  var pos = new Position (Map.height / 2,0)
  var life = 50
  val monster_type = Monster3Type
}
class Monster4 () extends Monster {
  val gold = 8
  val slowness = 10
  var wait_since = 0
  var pos = new Position (Map.height / 2,0)
  var life = 50
  val monster_type = Monster4Type
}
class Monster5 () extends Monster {
  val gold = 8
  val slowness = 10
  var wait_since = 0
  var pos = new Position (Map.height / 2,0)
  var life = 50
  val monster_type = Monster5Type
}
class Monster6 () extends Monster {
  val gold = 8
  val slowness = 10
  var wait_since = 0
  var pos = new Position (Map.height / 2,0)
  var life = 50
  val monster_type = Monster6Type
}
