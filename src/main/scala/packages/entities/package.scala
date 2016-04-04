package packages

import sugar._
import map._

/** Contient les entités du jeu, i.e. les différents types d'éléments qui vont apparaître sur la map */



package object entities
{
  /* trouve la distance à parcourir entre la position pos et la fin (-1 si pas de monstre sur la case car path pas encore abouti) */
  def find_distance (pos:Position) : Int = 
  {
    var monsters = Map.get_real_monsters (pos)
    if (monsters.isEmpty) {-1}
    else
    {
      var m = monsters.head
      var l = Map.path(m.init_pos.l)(m.path_choice)
      var monster_found = false
      var dist = 0
      l.foreach {(p:Position) => if (monster_found) {dist = dist+1} else {if (p == pos) {monster_found = true}}}
      dist
    }
  }

  /** Cette fonction renvoie le premier monstre qui apparaît dans la liste des cibles (on va dire aléatoire car compliqué) */
  def lazi (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var l = l_with_towerpos.tail
    if (l.isEmpty) {List[Monster]()}
    else {List[Monster] (l(0)._2(0))}
  }

  /** Cette fonction renvoie les monstres se trouvant sur la case la plus peuplée parmis les cases visibles */
  def case_max (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var l = l_with_towerpos.tail
    var targets = List[Monster]()
    l.foreach {(x:(Position,List[Monster])) => if (x._2.size > targets.size) {targets = x._2}}
    targets
  }

  /** Cette fonction renvoie les monstres se trouvant sur une des cases parmis les plus avancées dans le parcours des monstres vers La Commune */
  def case_closest (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var l = l_with_towerpos.tail
    var targets = List[Monster] ()
    var current_min = -1
    l.foreach {(x:(Position,List[Monster])) => {if (find_distance (x._1) < current_min) {current_min = find_distance (x._1) ; targets = x._2}}}

    targets
  }

  /** Cette fonction renvoie un monstre se trouvant sur la case la plus avancée dans le parcours des monstres vers La Commune */
  def closest (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var l = l_with_towerpos.tail
    var targets = List[Monster] ()
    var current_min = -1
    l.foreach {(x:(Position,List[Monster])) => {if (find_distance (x._1) < current_min) {current_min = find_distance (x._1) ; targets = List[Monster](x._2(0))}}}

    targets
  }

  /** Cette fonction renvoie les monstres se trouvant dans la lignée du monstre le plus avancé */
  def line (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var t_pos = l_with_towerpos.head._1
    var main_target = closest(l_with_towerpos) // main_target est la liste à 1 élément (voire 0 si tour isolée) qui contient un monstre parmi les plus proches
    var targets = List[Monster] ()
    if (! main_target.isEmpty)
    {
      var target = main_target(0) // target est le monstre "cible principale"
      for (l <- 0 to Map.height - 1 ; c <- 0 to Map.width - 1) // parcours de la map entière
      {
       var current_pos = new Position (l,c)
       if (current_pos.is_on_ray(t_pos,target.pos)) { Map.get_real_monsters(current_pos).foreach {(m:Monster) => if (m != target) {targets = m::targets} } }
      }
      targets = target::targets
    }
    targets
  }

  /** Cette fonction renvoie le monstre avec la vie la plus basse, avec en plus en cas d'égalité choix du monstre le plus loin dans le parcours */
  def coward (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var l = l_with_towerpos.tail
    var targets = List[Monster] ()
    var current_min = -1
    var min_life = 90000

    l.foreach {(x:(Position,List[Monster])) => {x._2.foreach {(m:Monster) => {
      if (m.life < min_life) {targets = List[Monster](m) ; min_life = m.life}
      else {if (m.life == min_life && find_distance (x._1) < current_min ) {targets = List[Monster](m)}}
    }}}}

    targets
  }

  /** Cette fonction renvoie le monstre avec la vie la plus basse + si one shot, choisit le one shot qui a le plus de vie */
  // bon beh là c'est comme pour line : on a besoin de la puissance de la tour pour déterminer le one shot
  def coward_smart (l_with_towerpos:List[(Position,List[Monster])]) : List[Monster] =
  {
    var t_pos = l_with_towerpos.head._1
    var l = l_with_towerpos.tail
    var pow = Map.get_tower(t_pos).tower_type.power
    var min_life = 90000
    var max_life = 0
    var one_shot = false
    var targets = List[Monster] ()
    l.foreach {(x:(Position,List[Monster])) => x._2.foreach {(m:Monster) =>
      {
       if (one_shot) {if (m.life >= max_life) {max_life = m.life ; targets = List[Monster](m)} }
       else {if (m.life <= pow) {one_shot = true ; targets = List[Monster] (m)}
             else {if (m.life <= min_life) {min_life = m.life ; targets = List[Monster](m)} } 
            }
      }
    }}
    targets
  }


}
