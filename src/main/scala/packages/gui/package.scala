package packages

import scala.math
import javax.swing.ImageIcon
import java.awt.Dimension

/** Contient des classes permettant de générer l'interface graphique du jeu
  * 
  */
package object gui {
  /** Permet de créer une nouvelle icône à partir de la première en changeant ses dimensions
    * 
    * L'objectif est de maximiser la taille de l'icône afin qu'elle puisse rentrer dans la dimension imposée mais sans pour autant déformer l'image de départ.
    * 
    * @param icon L'icon de départ
    * @param dim Les dimensions de la nouvelle icône
    */
  def zoom_icon (icon : ImageIcon ,dim : Dimension) = {
    val ratio:Float= (icon.getImage().getWidth(null)).toFloat/ (icon.getImage().getHeight(null));
    val new_width = math.min(dim.width,(ratio * dim.height).toInt);
    val new_height = (new_width / ratio).toInt
    new ImageIcon(icon.getImage.getScaledInstance(new_width,new_height,java.awt.Image.SCALE_SMOOTH))
  }
}

