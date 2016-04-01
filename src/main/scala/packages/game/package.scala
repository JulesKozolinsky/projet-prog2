package packages
import entities._

/** Contient des classes permettant la gestion du déroulement du jeu.
  *
  * Contient les classe Level et Round. Un niveau est une suite de rounds. Entre chaque round (ie une vague de monstres), le joueur peu plasser de nouvelles tours pour défendre le passage.
  */

package object game {

  /** Argent initial du joueur */
  val money_default = 200

  /** Argent du joueur */
  var money = money_default

  /** Nombre initial de vies */
  val life_default = 10

  /** Nombre de vies du joueur */
  var life = life_default

  /** Les tours débloquées au début de la partie */
  val unlocked_towers_default = List[TowerType] (Tower1Type, Tower2Type, Tower3Type, Tower4Type, Tower5Type, Tower6Type, Tower7Type, Tower8Type)

    /** Les tours débloquées à ce stade du jeu */
  var unlocked_towers = unlocked_towers_default

  /** Les monstres débloqués au début de la partie */
  val unlocked_monsters_default = List[MonsterType] (Monster1Type, Monster2Type, Monster3Type, Monster4Type, Monster5Type, Monster6Type, Monster7Type, Monster8Type)
 
  /** Les monstres débloqués à ce stade du jeu */
  var unlocked_monsters = unlocked_monsters_default

  /** Liste qui contient tous les types de monstres */
  val all_towers = List[TowerType] (Tower1Type, Tower2Type, Tower3Type, Tower4Type, Tower5Type, Tower6Type, Tower7Type, Tower8Type)

  /** Liste qui contient tous les types de monstres */
  val all_monsters = List[MonsterType] (Monster1Type, Monster2Type, Monster3Type, Monster4Type, Monster5Type, Monster6Type, Monster7Type, Monster8Type)

  /** Nombre total de level */ //cela at-il vraiment un sens ?? à supprimer je pense
  var number_of_level = 17

  /** Level en cours */
  var current_level = "Je peux écrire n'importe quoi ici de toute façon ça va être modifié \n Vivent Marx, Engels, Lénine & Beyonce"

  /** Nombre total de round dans le level */
  var number_of_round = 4

  /** Numéro du  round en cours */
  var round_counter = 1

  /** Initialisation de Game */
  def game_initialize () : Unit = 
  {
    money = money_default
    life = life_default
    unlocked_towers = unlocked_towers_default
    unlocked_monsters = unlocked_monsters_default
    round_counter = 1
  }
}
