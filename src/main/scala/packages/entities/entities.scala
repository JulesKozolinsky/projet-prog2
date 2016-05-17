package packages
package entities

import sugar._
import map._
import scala.util.Random



//PARTIE OU ON DEFINIT LES CLASSES ABSTRAITES DESQUELLES DESCENDENT LES OBJETS


/** Les Tileable sont l'ensembles de éléments susceptibles d'apparaître sur la map : tours, monstres, cailloux, arbres, alliés éventuels... */
abstract class Tileable ()
{
  /** Coordonnées de l'objet */
  var pos : Position
  val ttype : TileableType
}


/** Les Actor sont ceux parmi les Tileable qui sont appelés dans l'exécution d'un round, essentiellement les tours et les monstres */
abstract class Actor() extends Tileable
{
  /** temps depuis lequel l'Actor n'a pas agit */
  var wait_since : Int = 0
}


/** Classe abstraite des tours */
abstract class Tower () extends Actor
{
  /** Type de la tour */
  val tower_type : TowerType

  /** Etat de la tour (correspondrait à la vie) */
  var condition : Int

  /** Retire des "points de vie" à la tour quand elle se fait attaquer par un monstre spécial */
  def broken : Unit = {if (condition>1) {condition = condition - 1} else {Map.remove_tower(pos) ; throw new Exception ("tower in position "+ pos.l + ";" + pos.c + " removed")}}

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
  def receive_damages(dam:Int):Unit = {this.life = math.max (0,life - dam)}
}



/** Les Monstres sont les ennemis */
abstract class Monster () extends Living
{
  /** position d'apparition du monstre */
  var init_pos : Position

  /** choix du chemin le plus court */
  var path_choice : Int = 0

  /** type du monstre en question */
  val monster_type : MonsterType

  /** déplacement horizontal (-1 vers la gauche, 0 non déplacement, 1 pour la droite) */
  var orientation_h : Int = 0

  /** déplacement vertical (-1 vers le haut, 0 non déplacement, 1 vers le bas) */
  var orientation_v : Int = 0

  /** vitesse en pixel horizontale */
  var speed_pix_h : Float = 0

  /** vitesse en pixel verticale */
  var speed_pix_v : Float = 0

  /** Position en pixels sur la grille */
  var pix_pos = new Position_Real(0,0)

  /** fonction rendant des points de vie au monstre */
  def receive_life(qty:Int) : Unit = {this.life = math.min(life + qty, monster_type.max_life)}

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
        val future_pos = Map.next_case(pos,init_pos,path_choice)
        this.orientation_h = future_pos.c - pos.c
        this.orientation_v = future_pos.l - pos.l
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

  /**Identifiant de la tour **/
  val id : Int

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
  val id = 1
  val frequency =  15
  val priority = coward_smart _
  val range = 5
  val price = 20
  val power = 16
  val round_to_unlock = 1
  val name = "Tirs sur ambulances"
  val description = "Tour avec une grande portée et qui vise en priorité les monstres ayant une vie faible"
  val main_icon = "/tower1.png"
  val choice_icon = "/choice_tower1.png"
  val color = (0,0,0)
  val in_cell_pos  = new Position(0,0)
}
case object Tower2Type extends TowerType
{
  def get_instance (pos:Position) = new Tower2(pos)
  val id = 2
  val frequency =  50
  val priority = case_max _
  val range = 2
  val price = 35
  val power = 17
  val round_to_unlock = 1
  val name = "Grosse Bertha"
  val description = "Une espèce de gros canon lent à charger, qui tire sur la case la plus peuplée"
  val main_icon = "/tower2.png"
  val choice_icon = "/choice_tower2.png"
  val color = (255,0,0)

}
case object Tower3Type extends TowerType
{
  def get_instance (pos:Position) = new Tower3(pos)
  val id = 3
  val frequency =  6
  val priority = line _
  val range = 3
  val price = 50
  val power = 10
  val round_to_unlock = 1
  val name = "Tir laser"
  val description = "Cette tour trouve le monstre le plus avancé dans le chemin et tire sur tous les monstres dans la lignée de celui-ci"
  val main_icon = "/tower3.png"
  val choice_icon = "/choice_tower3.png"
  val color = (0,255,0)
}
case object Tower4Type extends TowerType
{
  def get_instance (pos:Position) = new Tower4(pos)
  val id = 4
  val frequency = 22
  val priority = case_closest _
  val range = 3
  val price = 35
  val power = 6
  val round_to_unlock = 1
  val name = "Mortier"
  val description = "Cette tour tire sur tous les monstres présents à la case la plus proche de la fin"
  val main_icon = "/tower4.png"
  val choice_icon = "/choice_tower4.png"
  val color = (0,230,255)
}
case object Tower5Type extends TowerType
{
  def get_instance (pos:Position) = new Tower5(pos)
  val id = 5
  val frequency = 6
  val priority = closest _
  val range = 3
  val price = 14
  val power = 12
  val round_to_unlock = 1
  val name = "Tir de précision"
  val description = "Cette tour tire sur un des monstres les plus avancé dans le chemin"
  val main_icon = "/tower5.png"
  val choice_icon = "/choice_tower5.png"
  val color = (255,126,0)
}
case object Tower6Type extends TowerType
{
  def get_instance (pos:Position) = new Tower6(pos)
  val id = 6
  val frequency = 8
  val priority = coward _
  val range = 5
  val price = 18
  val power = 12
  val round_to_unlock = 1
  val name = "Les faibles d'abord"
  val description = "Cette tour tire sur un des monstres ayant la vie la plus faible, en cas d'égalilté sur celui le plus avancé"
  val main_icon = "/tower6.png"
  val choice_icon = "/choice_tower6.png"
  val color = (122,13,123)
}
case object Tower7Type extends TowerType
{
  def get_instance (pos:Position) = new Tower7(pos)
  val id = 7
  val frequency = 4
  val priority = coward _
  val range = 15
  val price = 20
  val power = 16
  val round_to_unlock = 1
  val name = "Sniper"
  val description = "Tour avec une portée énorme, qui se comporte comme \"Tir de précision\""
  val main_icon = "/tower7.png"
  val choice_icon = "/choice_tower7.png"
  val color = (252,0,255)
}
case object Tower8Type extends TowerType
{
  def get_instance (pos:Position) = new Tower8(pos)
  val id = 8
  val frequency =  2
  val priority = closest _
  val range = 3
  val price = 35
  val power = 5
  val round_to_unlock = 1
  val name = "Sulfatteuse"
  val description = "Cette tour est peu puissante, mais tire à toute vitesse"
  val main_icon = "/tower8.png"
  val choice_icon = "/choice_tower8.png"
  val color = (135,42,42)
}




abstract class MonsterType extends TileableType
{
  /** fonction qui permet de construire un monstre du type en question à la position de début de map */
  def get_instance () : Monster

  /**Identifiant du monstre **/
  val id : Int

  /** la lenteur d'un monstre est le nombre de tick entre deux déplacements */
  val slowness : Int

  /** butin obtenu lors de la mort du monstre */
  val gold : Int

  /** Vie initiale d'un monstre*/
  val max_life : Int

  /** le round à partir duquel la tour est susceptible d'apparaître (indéxés depuis 1) */
  val round_to_unlock : Int

  /** le nombre de vies que le monstre enlève au joueur quand il arrive en fin de map */
  val damages : Int

  /** Position dans la cellule (3x3)*/
  val in_cell_pos : Position
}

case object Monster1Type extends MonsterType
{
  def get_instance () = new Monster1()
  val id = 1
  val slowness = 4
  val gold = 6
  val max_life = 50
  val round_to_unlock = 1
  val damages = 1
  val name = "Éclaireur"
  val description = "Un ennemi tout ce qu'il y a de plus commun"
  val main_icon = "/monster1.png"
  val in_cell_pos = new Position(0,0)
}
case object Monster2Type extends MonsterType
{
  def get_instance () = new Monster2()
  val id = 2
  val slowness = 6
  val gold = 12
  val max_life = 50
  val round_to_unlock = 1
  val damages = 1
  val name = "Bélier"
  val description = "Le bélier avance en ligne droite et détruit les tours devant lui"
  val main_icon = "/monster2.png"
  val in_cell_pos = new Position(0,1)
}
case object Monster3Type extends MonsterType
{
  def get_instance () = new Monster3()
  val id = 3
  val slowness = 20
  val gold = 10
  val max_life = 80
  val round_to_unlock = 1
  val damages = 4
  val name = "Tank"
  val description = "Le tank est très lent mais possède beaucoup de vies et fait perdre 4 points de vies au joueur s'il arrive en fin de map"
  val main_icon = "/monster3.png"
  val in_cell_pos = new Position(0,2)
}
case object Monster4Type extends MonsterType
{
  def get_instance () = new Monster4()
  val id = 4
  val slowness = 5
  val gold = 0
  val max_life = 1
  val round_to_unlock = 1
  val damages = 1
  val name = "Fantôme"
  val description = "Le fontôme est très faible, mais... arriverez vous à le toucher ?"
  val main_icon = "/monster4.png"
  val in_cell_pos = new Position(1,0)
}
case object Monster5Type extends MonsterType
{
  def get_instance () = new Monster5()
  val id = 5
  val slowness = 2
  val gold = 6
  val max_life = 16
  val round_to_unlock = 1
  val damages = 1
  val name = "Soldat à cheval"
  val description = "Doté de peu de vie, cet ennemi avance très rapidemment"
  val main_icon = "/monster5.png"
  val in_cell_pos = new Position(1,1)
}
case object Monster6Type extends MonsterType
{
  def get_instance () = new Monster6()
  val id = 6
  val slowness = 4
  val gold = 3
  val max_life = 30
  val round_to_unlock = 1
  val damages = 1
  val name = "Secours"
  val description = "Ce monstre soigne les monstres sur sa case"
  val main_icon = "/monster6.png"
  val in_cell_pos = new Position(1,2)
}
case object Monster7Type extends MonsterType
{
  def get_instance () = new Monster7()
  val id = 7
  val slowness = 6
  val gold = 600
  val max_life = 600
  val round_to_unlock = 1
  val damages = 9
  val name = "Développeur"
  val description = "Ce monstre est presque invincible..."
  val main_icon = "/monster7.png"
  val in_cell_pos = new Position(2,0)
}
case object Monster8Type extends MonsterType
{
  def get_instance () = new Monster8()
  val id = 8
  val slowness = 2
  val gold = 90
  val max_life = 90
  val round_to_unlock = 1
  val damages = 0
  val name = "Bourgeois à dépouiller"
  val description = "Ce monstre possède peu de vie, est très rapide, ne représente aucun risque pour vous, et vous possède toujours beaucoup d'argent sur lui"
  val main_icon = "/monster8.png"
  val in_cell_pos = new Position(2,1)
}










// PARTIE OU ON DEFINIT LES OBJETS

class Tower1 (position:Position) extends Tower
{
  val tower_type = Tower1Type
  val ttype = Tower1Type
  var pos = position
  var condition = 4
}
class Tower2 (position:Position) extends Tower
{
  val tower_type = Tower2Type
  val ttype = Tower2Type
  var pos = position
  var condition = 4
}
class Tower3 (position:Position) extends Tower
{
  val tower_type = Tower3Type
  val ttype = Tower3Type
  var pos = position
  var condition = 4
}
class Tower4 (position:Position) extends Tower
{
  val tower_type = Tower4Type
  val ttype = Tower4Type
  var pos = position
  var condition = 4
}
class Tower5 (position:Position) extends Tower
{
  val tower_type = Tower5Type
  val ttype = Tower5Type
  var pos = position
  var condition = 4
}
class Tower6 (position:Position) extends Tower
{
  val tower_type = Tower6Type
  val ttype = Tower6Type
  var pos = position
  var condition = 4
}
class Tower7 (position:Position) extends Tower
{
  val tower_type = Tower7Type
  val ttype = Tower7Type
  var pos = position
  var condition = 4
}
class Tower8 (position:Position) extends Tower
{
  val tower_type = Tower8Type
  val ttype = Tower8Type
  var pos = position
  var condition = 4
}



class Monster1 () extends Monster {
  val monster_type = Monster1Type
  val ttype = Monster1Type
  var init_pos = new Position (1,0)
  var pos = init_pos
  var life = monster_type.max_life

}
class Monster2 () extends Monster {
  val monster_type = Monster2Type
  val ttype = Monster2Type
  var init_pos = new Position (Map.height / 2,0)
  var pos = init_pos
  var life = monster_type.max_life
  val in_cell_pos = new Position(0,1)

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
          val prec_pos = pos
          pos = front
          this.orientation_h = pos.c - prec_pos.c
          this.orientation_v = pos.l - prec_pos.l
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
  val ttype = Monster3Type
  var init_pos = new Position (Map.height / 2,0)
  var pos = init_pos
  var life = monster_type.max_life
}

class Monster4 () extends Monster {
  val monster_type = Monster4Type
  val ttype = Monster4Type
  var init_pos = new Position (Map.height / 2,0)
  var pos = init_pos
  var life = monster_type.max_life
  val rand = new Random()

  /** le receive_damages du fantôme fait que le fantôme n'est touché qu'une fois sur 7 */
  override def receive_damages(dam:Int):Unit = {if (rand.nextInt(7) == 0) {this.life = math.max (0,this.life - dam)}}

}
class Monster5 () extends Monster {
  val monster_type = Monster5Type
  val ttype = Monster5Type
  var init_pos = new Position (Map.height / 2,0)
  var pos = init_pos
  var life = monster_type.max_life
}

class Monster6 () extends Monster {
  val monster_type = Monster6Type
  val ttype = Monster6Type
  var init_pos = new Position (Map.height / 2,0)
  var pos = init_pos
  var life = monster_type.max_life

  /** le apply du soigneur le fait soigner ses comparses */
  override def apply () : Boolean  = {
    var is_in_the_end = false
    if (wait_since == monster_type.slowness) // si il est bien temps d'agir
    {
      if ((pos).c == (Map.width-1)) {is_in_the_end = true} // si on était à la fin de la map on se signale
      else                                                 // sinon on avance
      {
        Map.move_monster (this,pos,Map.next_case(pos,init_pos,path_choice))
        val prec_pos = pos
        pos = Map.next_case(pos,init_pos,path_choice)
        val future_pos = Map.next_case(pos,init_pos,path_choice)
        this.orientation_h = future_pos.c - pos.c
        this.orientation_v = future_pos.l - pos.l
	(Map.get_real_monsters(pos)).foreach {(m:Monster) => {m.receive_life(6)}}
        wait_since = 0
      }
    }
    else {wait_since = wait_since + 1}  // s'il n'était pas temps d'agir...

    is_in_the_end
  }
}
class Monster7 () extends Monster {
  val monster_type = Monster7Type
  val ttype = Monster7Type
  var init_pos = new Position (Map.height / 2,0)
  var pos = init_pos
  var life = monster_type.max_life
}

class Monster8 () extends Monster {
  val monster_type = Monster8Type
  val ttype = Monster8Type
  var init_pos = new Position (Map.height / 2,0)
  var pos = init_pos
  var life = monster_type.max_life
}
