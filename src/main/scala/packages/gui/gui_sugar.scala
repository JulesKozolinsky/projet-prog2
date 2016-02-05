package packages
package gui

import sugar._
import swing._
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
