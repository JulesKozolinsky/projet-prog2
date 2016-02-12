package packages

/** Contient les entités du jeu, i.e. les différents types d'éléments qui vont apparaître sur la map */
package object entities
{
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
}
