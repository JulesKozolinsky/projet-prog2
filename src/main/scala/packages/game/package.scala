package packages

/** Contient des classes permettant la gestion du déroulement du jeu.
  *
  * Contient les classe Level et Round. Un niveau est une suite de rounds. Entre chaque round (ie une vague de monstres), le joueur peu plasser de nouvelles tours pour défendre le passage.
  */

package object game {

  /** Argent du joueur par défaut */
  var money_default = 100

  /** Nombre de vies au début */
  var life_default = 1

  /** Argent du jouer restant */
  var money = money_default

  /** Nombre de vies du joeur */
  var life = life_default
}
