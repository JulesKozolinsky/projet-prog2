package packages

package sugar

import swing._
/** Cette classe doit être utilisée à chaque fois que l'on fait référence à une entité sur la grille
  * 
  * @param l Numéro de la ligne
  * @param c Numéro de la colonne
  */

class Position(l_arg:Int, c_arg:Int)
{
  var l = l_arg
  var c = c_arg
}

/** Extension de la classe GridPanel permettant de modifier un élément grâce à sa position
  * 
  * Utilisation : var p = new Position(1,2)7
  * 
  * myPosGridPanel(p).text = "Nouveau nom pour l'élément (1,2)"
  */
class PosGridPanel(lines : Int, cols : Int) extends GridPanel(lines,cols)
{
  def apply(p : Position) : Component = {
    contents(p.l * cols + p.c)
  }
}



class Vector (point1:Position,point2:Position)
{
  def norme : Double = math.sqrt((point1.l - point2.l)*(point1.l - point2.l) + (point1.c - point2.c*(point1.c - point2.c )))
}
