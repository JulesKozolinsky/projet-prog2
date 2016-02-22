package packages

/** Contient des classes permettant la gestion du déroulement du jeu.
  * 
  * Contient les classe Level et Round. Un niveau est une suite de rounds. Entre chaque round (ie une vague de monstres), le joueur peu plasser de nouvelles tours pour défendre le passage.
  */

package object game {
  /** Période du tick en millisecondes */
  val tick = 100

  /** Argent du joueur */
  var money = 100

  /** Nombre de vies restantes */
  var life = 10
}
