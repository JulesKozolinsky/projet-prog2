package packages
import sugar._

/** Contient les entités du jeu, i.e. les différents types d'éléments qui vont apparaître sur la map */



package object entities
{

  /** Cette fonction renvoie le premier monstre qui apparaît dans la liste des cibles (on va dire aléatoire car compliqué) */
  def lazi (l:List[Monster]) : List[Monster] =
  {
    if (l.isEmpty)
    {
      l
    }
    else
    {
      List(l(0))
    }
  }

  /** Cette fonction renvoie les monstres se trouvant sur la case la plus peuplée parmis les cases visibles */
  def case_max (l:List[Monster]) : List[Monster] =
  {
    var pos_set = Set[(Position,Int)] ()
    l.foreach { (m:Monster) => pos_set = pos_set + new Tuple2(m.pos,0) }
    l.foreach { (m:Monster) => var Some(m_nbr) = pos_set.find {(x:Tuple2[Position,Int]) => (m.pos == x._1)}
      pos_set = (pos_set - m_nbr) + (new Tuple2(m_nbr._1, m_nbr._2 + 1))   } // a ce stade on a un set avec les positions et es monstres 
    l
  }

  /** Cette fonction renvoie les monstres se trouvant sur la case la plus avancée dans le parcours des monstres vers La Commune */
  def case_closest (l:List[Monster]) : List[Monster] =
  {
    l
  }


  /** Cette fonction renvoie un monstre se trouvant sur la case la plus avancée dans le parcours des monstres vers La Commune */
  def closest (l:List[Monster]) : List[Monster] =
  {
    l
  }


}
