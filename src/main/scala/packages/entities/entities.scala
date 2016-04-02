package packages
package entities

import sugar._
import map._



//PARTIE OU ON DEFINIT LES CLASSES ABSTRAITES DESQUELLES DESCENDENT LES OBJETS


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


/** Classe abstraite des tours */
abstract class Tower () extends Actor
{
  /** Type de la tour */
  val tower_type : TowerType

  /** Etat de la tour (correspondrait à la vie) */
  var condition : Int

  /** Retire des "points de vie" à la tour quand elle se fait attaquer par un monstre spécial */
  def broken : Unit = {if (condition>1) {condition = condition - 1} else {Map.remove_tower(pos)}}

  /** le apply des tours est le fait de tirer sur un/des monstres ;
    *
    * targets est une liste contenant toutes les cibles sur qui la tour va tirer ; si la liste est vide le wait_since peut augmenter mais ne retombe pas à 0
    * @return la liste des monstres tués par la tour pendant l'éxécution
    */
  def apply () : List[Monster] = {
    var L : List[Monster] = List()

    if (wait_since == tower_type.frequency)
    {
      var targets = tower_type.priority ( Map.get_targets(this) )
      if (targets.isEmpty) {wait_since = math.min( tower_type.frequency , (1 + wait_since) )}
      else
      {
        wait_since = 0
        targets.foreach {(m:Monster) =>
          {
            m.receive_damages (tower_type.power)
            if (m.life == 0) {L=m::L}
          }
        }
      }
    }
    else {wait_since = 1 + wait_since}
    L
  }

}


/** Les Living sont parmi les Actor ceux qui sont dotés de vie (monstres, éventuellement héros) */
abstract class Living () extends Actor
{
  /** les Living sont dotés d'un nombre fini de points de vie */
  var life : Int

  /** fonction retirant une quantité de point de vie au Living */
  def receive_damages(dam:Int):Unit = {this.life = math.max (0,this.life - dam)}
}



/** Les Monstres sont les ennemis */
abstract class Monster () extends Living
{
  /** position d'apparition du monstre */
  var init_pos : Position

  /** choix du chemin le plus court */
  var path_choice : Int

  /** type du monstre en question */
  val monster_type : MonsterType

  /** le apply du monstre le fait bouger s'il est temps et renvoie True dans le cas où il est parvenu en fin de map et fait ainsi perdre une vie au joueur */
  def apply () : Boolean  = {
    var is_in_the_end = false
    if (wait_since == monster_type.slowness) // si il est bien temps d'agir
    {
      if ((pos).c == (Map.width-1)) {is_in_the_end = true} // si on était à la fin de la map on se signale
      else                                                 // sinon on avance
      {
        Map.move_monster (this,pos,Map.next_case(pos,init_pos,path_choice))
        pos = Map.next_case(pos,init_pos,path_choice)
        wait_since = 0
      }
    }
    else {wait_since = wait_since + 1}  // s'il n'était pas temps d'agir...

    is_in_the_end
  }

}










// PARTIE OU ON DEFINIT LES TYPES DES OBJETS
// Attention si on ajoute des nouvelles tours ou monstres (9, 10, etc) penser à les rajouter dans game : package.scala

abstract class TileableType ()
{
  /** nom de l'objet considéré */
  val name : String

  /** description de l'objet considéré */
  val description : String

  /** icône principale visible sur la grille */
  val main_icon : String
}

/** Les différents types de tour  */

abstract class TowerType extends TileableType {

  /** fonction qui permet de construire une tour du type en question à la position pos */
  def get_instance (pos:Position) : Tower

  /** la fréquence d'une tour est le nombre de tick entre deux tirs */
  val frequency : Int

  /** la priorité d'une tour est une fonction qui prend en entrée les monstres accessibles par la tour et renvoie la liste des monstres attaqués par la tour */
  val priority : (List[(Position,List[Monster])]) => List[Monster]

  /** la portée d'une tour détermine la distance maximale à laquelle elle peut tirer */
  val range : Int

  /** le prix d'une tour */
  val price : Int

  /** la puissance de feu d'une tour */
  val power : Int

  /** le round à partir duquel la tour est disponible (indéxé depuis 1) */
  val round_to_unlock : Int

  /** icône du bouton sélécteur de tour à poser */
  val choice_icon : String

  /** Couleur associée à la tour */
  val color : Tuple3[Int,Int,Int]

}


case object Tower1Type extends TowerType
{
  def get_instance (pos:Position) = new Tower1(pos)
  val frequency =  15
  val priority = coward_smart _
  val range = 5
  val price = 20
  val power = 5
  val round_to_unlock = 1
  val name = "Tirs sur ambulances"
  val description = "Tour avec une grande portée et qui vise en priorité les monstres ayant une vie faible"
  val main_icon = "/tower1.png"
  val choice_icon = "/choice_tower1.png"
  val color = (0,0,0)
}
case object Tower2Type extends TowerType
{
  def get_instance (pos:Position) = new Tower2(pos)
  val frequency =  50
  val priority = case_max _
  val range = 2
  val price = 35
  val power = 10
  val round_to_unlock = 1
  val name = "Grosse Bertha"
  val description = "Une espèce de gros canon lent à charger, voit pas loin mais puissant et bouzille toute une case "
  val main_icon = "/tower2.png"
  val choice_icon = "/choice_tower2.png"
  val color = (255,0,0)

}
case object Tower3Type extends TowerType
{
  def get_instance (pos:Position) = new Tower3(pos)
  val frequency =  8
  val priority = lazi _
  val range = 3
  val price = 35
  val power = 6
  val round_to_unlock = 1
  val name = "Tour 3"
  val description = "ceci est une Tour de type 3"
  val main_icon = "/tower3.png"
  val choice_icon = "/choice_tower3.png"
  val color = (0,255,0)
}
case object Tower4Type extends TowerType
{
  def get_instance (pos:Position) = new Tower4(pos)
  val frequency =  8
  val priority = lazi _
  val range = 3
  val price = 35
  val power = 6
  val round_to_unlock = 1
  val name = "Tour 4"
  val description = "ceci est une Tour de type 4"
  val main_icon = "/tower4.png"
  val choice_icon = "/choice_tower4.png"
  val color = (0,230,255)
}
case object Tower5Type extends TowerType
{
  def get_instance (pos:Position) = new Tower5(pos)
  val frequency =  8
  val priority = lazi _
  val range = 3
  val price = 35
  val power = 6
  val round_to_unlock = 1
  val name = "Tour 5"
  val description = "ceci est une Tour de type 5"
  val main_icon = "/tower5.png"
  val choice_icon = "/choice_tower5.png"
  val color = (255,126,0)
}
case object Tower6Type extends TowerType
{
  def get_instance (pos:Position) = new Tower6(pos)
  val frequency =  8
  val priority = lazi _
  val range = 3
  val price = 35
  val power = 6
  val round_to_unlock = 1
  val name = "Tour 6"
  val description = "ceci est une Tour de type 6"
  val main_icon = "/tower6.png"
  val choice_icon = "/choice_tower6.png"
  val color = (122,13,123)
}
case object Tower7Type extends TowerType
{
  def get_instance (pos:Position) = new Tower7(pos)
  val frequency =  8
  val priority = lazi _
  val range = 3
  val price = 35
  val power = 6
  val round_to_unlock = 1
  val name = "Tour 7"
  val description = "ceci est une Tour de type 7"
  val main_icon = "/tower7.png"
  val choice_icon = "/choice_tower7.png"
  val color = (252,0,255)
}
case object Tower8Type extends TowerType
{
  def get_instance (pos:Position) = new Tower8(pos)
  val frequency =  8
  val priority = lazi _
  val range = 3
  val price = 35
  val power = 6
  val round_to_unlock = 1
  val name = "Tour 8"
  val description = "ceci est une Tour de type 8"
  val main_icon = "/tower8.png"
  val choice_icon = "/choice_tower8.png"
  val color = (135,42,42)
}




abstract class MonsterType extends TileableType
{
  /** fonction qui permet de construire un monstre du type en question à la position de début de map */
  def get_instance () : Monster

  /** la lenteur d'un monstre est le nombre de tick entre deux déplacements */
  val slowness : Int

  /** butin obtenu lors de la mort du monstre */
  val gold : Int

  /** Vie initiale d'un monstre*/
  val max_life : Int

  /** le round à partir duquel la tour est susceptible d'apparaître (indéxés depuis 1) */
  val round_to_unlock : Int
}

case object Monster1Type extends MonsterType
{
  def get_instance () = new Monster1()
  val slowness = 5
  val gold = 6
  val max_life = 40
  val round_to_unlock = 1
  val name = "Monstre 1"
  val description = "ceci est un Monstre de type 1"
  val main_icon = "/monster1.png"
}
case object Monster2Type extends MonsterType
{
  def get_instance () = new Monster2()
  val slowness = 5
  val gold = 6
  val max_life = 50
  val round_to_unlock = 1
  val name = "Battering ram"
  val description = "Ce monstre avance en ligne droite et détruit les tours devant lui"
  val main_icon = "/monster2.png"
}
case object Monster3Type extends MonsterType
{
  def get_instance () = new Monster3()
  val slowness = 5
  val gold = 6
  val max_life = 50
  val round_to_unlock = 1
  val name = "Monstre 3"
  val description = "ceci est un Monstre de type 3"
  val main_icon = "/monster3.png"
}
case object Monster4Type extends MonsterType
{
  def get_instance () = new Monster4()
  val slowness = 5
  val gold = 6
  val max_life = 50
  val round_to_unlock = 1
  val name = "Monstre 4"
  val description = "ceci est un Monstre de type 4"
  val main_icon = "/monster4.png"
}
case object Monster5Type extends MonsterType
{
  def get_instance () = new Monster5()
  val slowness = 5
  val gold = 6
  val max_life = 50
  val round_to_unlock = 1
  val name = "Monstre 5"
  val description = "ceci est un Monstre de type 5"
  val main_icon = "/monster5.png"
}
case object Monster6Type extends MonsterType
{
  def get_instance () = new Monster6()
  val slowness = 5
  val gold = 6
  val max_life = 50
  val round_to_unlock = 1
  val name = "Monstre 6"
  val description = "ceci est un Monstre de type 6"
  val main_icon = "/monster6.png"
}
case object Monster7Type extends MonsterType
{
  def get_instance () = new Monster7()
  val slowness = 5
  val gold = 6
  val max_life = 50
  val round_to_unlock = 1
  val name = "Monstre 7"
  val description = "ceci est un Monstre de type 7"
  val main_icon = "/monster7.png"
}
case object Monster8Type extends MonsterType
{
  def get_instance () = new Monster8()
  val slowness = 5
  val gold = 6
  val max_life = 50
  val round_to_unlock = 1
  val name = "Monstre 8"
  val description = "ceci est un Monstre de type 8"
  val main_icon = "/monster8.png"
}










// PARTIE OU ON DEFINIT LES OBJETS

class Tower1 (position:Position) extends Tower
{
  val tower_type = Tower1Type
  var pos = position
  var wait_since = 0
  var condition = 4
}
class Tower2 (position:Position) extends Tower
{
  val tower_type = Tower2Type
  var pos = position
  var wait_since = 0
  var condition = 4
}
class Tower3 (position:Position) extends Tower
{
  val tower_type = Tower3Type
  var pos = position
  var wait_since = 0
  var condition = 4
}
class Tower4 (position:Position) extends Tower
{
  val tower_type = Tower4Type
  var pos = position
  var wait_since = 0
  var condition = 4
}
class Tower5 (position:Position) extends Tower
{
  val tower_type = Tower5Type
  var pos = position
  var wait_since = 0
  var condition = 4
}
class Tower6 (position:Position) extends Tower
{
  val tower_type = Tower6Type
  var pos = position
  var wait_since = 0
  var condition = 4
}
class Tower7 (position:Position) extends Tower
{
  val tower_type = Tower7Type
  var pos = position
  var wait_since = 0
  var condition = 4
}
class Tower8 (position:Position) extends Tower
{
  val tower_type = Tower8Type
  var pos = position
  var wait_since = 0
  var condition = 4
}



class Monster1 () extends Monster {
  val monster_type = Monster1Type
  var wait_since = 0
  var init_pos = new Position (1,0)
  var path_choice = 0
  var pos = init_pos
  var life = monster_type.max_life
}
class Monster2 () extends Monster {
  val monster_type = Monster2Type
  var wait_since = 0
  var init_pos = new Position (Map.height / 2,0)
  var path_choice = 0
  var pos = init_pos
  var life = monster_type.max_life

  /** le apply du bélier le fait d'avancer s'il est temps, renvoie True dans le cas où il est parvenu en fin de map et fait ainsi perdre une vie au joueur */
  override def apply () : Boolean  = {
    var is_in_the_end = false
    if (wait_since == monster_type.slowness)
    {
      if ((pos).c == (Map.width-1))
      {
        is_in_the_end = true
      }
      else
      {
	var front = new Position(pos.l,pos.c+1)
	if (! Map.is_tower(front))
	{
          Map.move_monster (this,pos,front)
          pos = front
          wait_since = 0
	}
	else {Map.get_tower(front).broken}
      }
    }
    else
    {
      wait_since = wait_since + 1
    }
    is_in_the_end
  }
}
class Monster3 () extends Monster {
  val monster_type = Monster3Type
  var wait_since = 0
  var init_pos = new Position (Map.height / 2,0)
  var path_choice = 0
  var pos = init_pos
  var life = monster_type.max_life
}
class Monster4 () extends Monster {
  val monster_type = Monster4Type
  var wait_since = 0
  var init_pos = new Position (Map.height / 2,0)
  var path_choice = 0
  var pos = init_pos
  var life = monster_type.max_life
}
class Monster5 () extends Monster {
  val monster_type = Monster5Type
  var wait_since = 0
  var init_pos = new Position (Map.height / 2,0)
  var path_choice = 0
  var pos = init_pos
  var life = monster_type.max_life
}
class Monster6 () extends Monster {
  val monster_type = Monster6Type
  var wait_since = 0
  var init_pos = new Position (Map.height / 2,0)
  var path_choice = 0
  var pos = init_pos
  var life = monster_type.max_life
}
class Monster7 () extends Monster {
  val monster_type = Monster7Type
  var wait_since = 0
  var init_pos = new Position (Map.height / 2,0)
  var path_choice = 0
  var pos = init_pos
  var life = monster_type.max_life
}
class Monster8 () extends Monster {
  val monster_type = Monster8Type
  var wait_since = 0
  var init_pos = new Position (Map.height / 2,0)
  var path_choice = 0
  var pos = init_pos
  var life = monster_type.max_life
}
