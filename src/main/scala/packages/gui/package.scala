package packages

import entities._
import scala.math
import javax.swing.ImageIcon
import java.awt.Dimension

/** Contient des classes permettant de générer l'interface graphique du jeu
  * 
  */
package object gui {

  /** Tableau contenant les skins de tous les types de tour 
    * 
    * Le type par défaut au début du jeu est le premier qui apparaît dans ce tableau
    */
  val tower_skins = Array(new TowerSkin("/choice_tower1.png", "/tower1.png",new Tower1,new Dimension(20,20)), new TowerSkin("/choice_tower2.png", "/tower2.png",new Tower2,new Dimension(20,20)))

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

