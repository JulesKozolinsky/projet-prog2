package packages

package sugar

/** Cette classe doit être utilisée à chaque fois que l'on fait référence à une entité sur la grille
  *
  * @param l Numéro de la ligne
  * @param c Numéro de la colonne
  */

class Position(l_arg:Int, c_arg:Int)
{
  var l = l_arg
  var c = c_arg
  override def equals(pos2:Any) : Boolean = {
    pos2 match {
    case pos2:Position => this.l == pos2.l && this.c == pos2.c
    case _ => throw new ClassCastException
  }
  }
}


class Vector (point1:Position,point2:Position)
{  def norme : Int = math.sqrt((point1.l - point2.l)*(point1.l - point2.l) + (point1.c - point2.c)*(point1.c - point2.c)).toInt
}
