package packages
package gui

import sugar._
import swing._
import javax.swing.ImageIcon
import java.awt.Graphics
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

/**Pour le moment inutilisée */
class ImageLabel(legend:String, image: Image) extends javax.swing.JLabel(legend)
{
  def paintComponent(g:Graphics2D) {
    super.paintComponent(g);
    if (image == null) return;
    g.drawImage(image, 0, 0, this.size.width, this.size.height, this);
  }
}
