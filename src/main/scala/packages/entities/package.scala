package packages
import sugar._

/** Contient les entités du jeu, i.e. les différents types d'éléments qui vont apparaître sur la map */



package object entities
{

  /** Cette fonction renvoie le premier monstre qui apparaît dans la liste des cibles (on va dire aléatoire car compliqué) */
  def lazi (l:List[(Position,List[Monster])]) : List[Monster] =
  {
    if (l.isEmpty) {List[Monster]()}
    else {l(0)._2}
  }

  /** Cette fonction renvoie les monstres se trouvant sur la case la plus peuplée parmis les cases visibles */
  def case_max (l:List[(Position,List[Monster])]) : List[Monster] =
  {
    var targets = List[Monster]()
    l.foreach {(x:(Position,List[Monster])) => if (x._2.size > targets.size) {targets = x._2}}
    targets
  }

  /** Cette fonction renvoie les monstres se trouvant sur la case la plus avancée dans le parcours des monstres vers La Commune */
  def case_closest (l:List[(Position,List[Monster])]) : List[Monster] =
  {
    var targets = List[Monster] ()
    //path.foreach {(p:Position) => } PROBLEME : JE VEUX ITERER LE PATH DANS LE BON SENS ET JE SAIS PAS CE QUE FAIT FOREACH
    l(0)._2
  }

  /** Cette fonction renvoie un monstre se trouvant sur la case la plus avancée dans le parcours des monstres vers La Commune */
  def closest (l:List[(Position,List[Monster])]) : List[Monster] =
  {
    l(0)._2
  }

  /** Cette fonction renvoie les monstres se trouvant dans la lignée du monstre le plus avancé */
  def line (l:List[(Position,List[Monster])]) : List[Monster] =
  {
    l(0)._2
  }


}
