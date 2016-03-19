package packages

import sugar._
import map._

/** Contient les entités du jeu, i.e. les différents types d'éléments qui vont apparaître sur la map */



package object entities
{

  /** Cette fonction renvoie le premier monstre qui apparaît dans la liste des cibles (on va dire aléatoire car compliqué) */
  def lazi (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var t_pos = l_with_towerpos.head
    var l = l_with_towerpos.tail
    if (l.isEmpty) {List[Monster]()}
    else {List[Monster] (l(0)._2(0))}
  }

  /** Cette fonction renvoie les monstres se trouvant sur la case la plus peuplée parmis les cases visibles */
  def case_max (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var t_pos = l_with_towerpos.head
    var l = l_with_towerpos.tail
    var targets = List[Monster]()
    l.foreach {(x:(Position,List[Monster])) => if (x._2.size > targets.size) {targets = x._2}}
    targets
  }

  /** Cette fonction renvoie les monstres se trouvant sur la case la plus avancée dans le parcours des monstres vers La Commune */
  def case_closest (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var t_pos = l_with_towerpos.head
    var l = l_with_towerpos.tail
    var targets = List[Monster] ()
    var path_it = Map.path
    while (! path_it.isEmpty) // while plus que foreach pour parcourir la liste dans le bon ordre (même si c'est probablement ce que fait foreach sur les listes)
    {
      l.foreach {(x:(Position,List[Monster])) => {if (x._1 == path_it.head) {targets = x._2}}}
      path_it = path_it.tail
    }
    targets
  }

  /** Cette fonction renvoie un monstre se trouvant sur la case la plus avancée dans le parcours des monstres vers La Commune */
  def closest (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var t_pos = l_with_towerpos.head
    var l = l_with_towerpos.tail
    var targets = List[Monster] ()
    var path_it = Map.path
    while (! path_it.isEmpty) // while plus que foreach pour parcourir la liste dans le bon ordre (même si c'est probablement ce que fait foreach sur les listes)
    {
      l.foreach {(x:(Position,List[Monster])) => {if (x._1 == path_it.head) {targets = x._2}}}
      path_it = path_it.tail
    }
    if (targets.isEmpty) {List[Monster]()}
    else {List[Monster] (targets(0))}
  }

  /** Cette fonction renvoie les monstres se trouvant dans la lignée du monstre le plus avancé */
  // pas fait car on a pour ça besoin de la position de la tour (bah oui) et j'avais prévu tout le code pour qu'on n'en ait pas besoin, pour des raisons de facilités.
  // à voir si on abandonne cette propriété ou si je tente de modifier mon code...
  def line (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var main_target = closest(l_with_towerpos)
    var targets = List[Monster] ()
    if (main_target.isEmpty) {targets}
    else 
    {
      var m_targ = true
    }
    List[Monster] (l_with_towerpos(0)._2(0))
  }

  /** Cette fonction renvoie le monstre avec la vie la plus basse, avec en plus en cas d'égalité choix du monstre le plus loin dans le parcours */
  def coward (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var t_pos = l_with_towerpos.head
    var l = l_with_towerpos.tail
    var min_life = 900
    var targets = List[Monster] ()
    var path_it = Map.path
    while (! path_it.isEmpty) // while plus que foreach pour parcourir la liste dans le bon ordre (même si c'est probablement ce que fait foreach sur les listes)
    {
      l.foreach {(x:(Position,List[Monster])) => x._2.foreach {(m:Monster) => {if (m.life <= min_life) {targets = List[Monster](m)}}}}
      path_it = path_it.tail
    }
    targets
  }

  /** Cette fonction renvoie le monstre avec la vie la plus basse + si one shot, choisit le one shot qui a le plus de vie */
  // bon beh là c'est comme pour line : on a besoin de la puissance de la tour pour déterminer le one shot
  def coward_smart (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var t_pos = l_with_towerpos.head
    var l = l_with_towerpos.tail
    List[Monster] (l(0)._2(0))
  }


}
