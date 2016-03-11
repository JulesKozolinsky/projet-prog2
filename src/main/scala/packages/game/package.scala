package packages
import entities._

/** Contient des classes permettant la gestion du déroulement du jeu.
  *
  * Contient les classe Level et Round. Un niveau est une suite de rounds. Entre chaque round (ie une vague de monstres), le joueur peu plasser de nouvelles tours pour défendre le passage.
  */

package object game {

  /** Argent initial du joueur */
  val money_default = 200000

  /** Nombre initial de vies */
  val life_default = 10

  /** Les tours débloquées au début de la partie */
  val unlocked_towers_default = List[TowerType] (Tower1Type, Tower2Type)

  /** Les monstres débloqués au début de la partie */
  val unlocked_monsters_default = List[MonsterType] (Monster1Type, Monster2Type, Monster3Type, Monster4Type, Monster5Type, Monster6Type, Monster7Type, Monster8Type)

  /** Argent du joueur */
  var money = money_default

  /** Nombre de vies du joueur */
  var life = life_default

  /** Les tours débloquées à ce stade du jeu */
  var unlocked_towers = unlocked_towers_default

  /** Les monstres débloqués à ce stade du jeu */
  var unlocked_monsters = unlocked_monsters_default

}
