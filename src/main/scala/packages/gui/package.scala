package packages

import entities._
import scala.math
import java.awt.Image
import java.awt.Dimension

/** Contient des classes permettant de générer l'interface graphique du jeu
  * 
  */
package object gui {

  /** Tableau contenant les skins de tous les types de tour 
    * 
    * Le type par défaut au début du jeu est le premier qui apparaît dans ce tableau
    */
  val tower_skins = Array(new TowerSkin("/choice_tower1.png", "/tower1.png",new Tower1), new TowerSkin("/choice_tower2.png", "/tower2.png",new Tower2))

  /** Permet de créer une nouvelle image à partir de la première en changeant ses dimensions
    * 
    * Maximise la taille de l'image afin qu'elle puisse rentrer dans la dimension imposée mais sans pour autant déformer l'image de départ.
    * 
    * @param image L'image de départ
    * @param dim Les dimensions de la nouvelle image
    */
  def zoom_image (image : Image ,dim : Dimension) = {
    val ratio:Float= (image.getWidth(null)).toFloat/ (image.getHeight(null));
    val new_width = math.min(dim.width,(ratio * dim.height).toInt);
    val new_height = (new_width / ratio).toInt
    val img = image
    img.getScaledInstance(new_width,new_height,java.awt.Image.SCALE_DEFAULT)
  }
}

