package packages
package gui

import sugar._
import swing._
import javax.swing.ImageIcon



/** Extension de la classe GridPanel permettant de modifier un élément grâce à sa position
  * 
  * Utilisation : var p = new Position(1,2)
  * 
  * myPosGridPanel(p).text = "Nouveau nom pour l'élément (1,2)". Attention à bien convertir au bon type si on veut effectivement avoir text (ce n'est pas pas un attribut de component).
  */
class PosGridPanel(lines : Int, cols : Int) extends GridPanel(lines,cols)
{
  def apply(p : Position) : Component = {
    contents(p.l * cols + p.c)
  }
}

class PushUp(comp : Component) extends BoxPanel(Orientation.Vertical)
{
  contents += comp
  contents += Swing.VGlue
}


