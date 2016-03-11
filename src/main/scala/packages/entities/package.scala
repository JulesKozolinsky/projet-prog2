package packages

/** Contient les entités du jeu, i.e. les différents types d'éléments qui vont apparaître sur la map */



package object entities
{

  /** Cette fonction prend le premier */
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


def case_max (l:List[Monster]) : List[Monster] = 
  {
    l
  }


def case_closest (l:List[Monster]) : List[Monster] = 
  {
    l
  }






}
