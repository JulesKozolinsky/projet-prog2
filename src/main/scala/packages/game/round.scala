package packages
package game

import map._
import entities._
import sugar._



/** Gestion d'un round (vague d'attaquant).
  *
  * Pendant le round, le joueur ne peut pas ajouter de tour.
  * Il regarde les tours défendre le passage contre une vague de monstres
  * @param wave : List[Tuple2[Set[Tuple2[Monster,Int]],Int]]
  */
class Round(wave:List[Tuple2[scala.collection.mutable.Set[Tuple2[Any,Int]],Int]]) {

  /** Nombre de vies restantes, à initialiser avec Level.life et à renvoyer à Level en fin de round */
  var lives = 0

  /** Nombre de tick depuis le début du round */
  var tick = 0

  /** Ensemble des monstres en vie */
  var monsters = Set[Monster] ()

  /** Les différentes vagues d'attaquants */
  var waves : List[(Set[(MonsterType,Int)],Int)] = List[(Set[(MonsterType,Int)],Int)]  () // à modifier bien sûr

  def rem_monster (monster:Monster) : Unit = {
    monsters = monsters - monster
    Map.remove_monster(monster,monster.pos)
  }

  /** Actualise l'état de l'objet.
    *
    *  Cette méthode doit être appelée à chaque tick
    */
  def actualize (): Unit = {
    var l = 0
    var c = 0

  /* ici on ajoute dans le set de monstres une éventuelle nouvelle vague */
  if (!waves.isEmpty)
    {
//     if waves.hd
    }


  /* cette première boucle parcourt la map, trouve les tours et les fait tirer  */
    for (l <- 0 to Map.height - 1 ; c <- 0 to Map.width - 1)
    {
      val p = new Position(l,c)
      if (Map.is_tower (p))
      {
        var killed = (Map.get_tower (p)).apply
        killed.foreach {rem_monster}
      }
      else
      {}

    }

  /* ici on parcourt les monstres : on les fait avancer, et éventuellement enlever une vie au joueur */
    monsters.foreach { (m:Monster) => if (m.apply) {lives = lives-1} }
    
    is_finished ()
    tick = tick + 1
  }


  /** Permet de savoir quand le round est terminé
    *
    *  @return true si le round est fini, false sinon.
    */
  def is_finished () : Boolean = {(monsters.isEmpty && waves.isEmpty) || (lives == 0)}
}
