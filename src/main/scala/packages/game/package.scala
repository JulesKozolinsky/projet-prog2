package packages

/** Contient des classes permettant la gestion du déroulement du jeu.
  *
  * Contient les classe Level et Round. Un niveau est une suite de rounds. Entre chaque round (ie une vague de monstres), le joueur peu plasser de nouvelles tours pour défendre le passage.
  */

package object game {

  /** Argent du joueur */
  var money = 100000

  /** Nombre de vies restantes */
  var life = 30
}
