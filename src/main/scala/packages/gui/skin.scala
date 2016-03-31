package packages
package gui

import entities._

import swing._

import javax.swing.ImageIcon
import java.awt.Dimension
import java.awt.image.BufferedImage


/** Cette classe permet de stocker des informations concernant l'affichage d'un élément de l'interface
  *
  * On garde le fichier en mémoire parce que c'est le seul moyen de faire le zoom que j'ai trouvé
  * Utilisation de l'évaluation paresseuse : on ne crée une image que si on en a besoin. Par consséquent, lorsque toutes les images doivent changer de taille, il faut le préciser à l'objet (resize_all)
  * @param a_file Image associée au skin
  */
class Skin(a_file:String)
{
  /** Le fichier contenant l'image dont on veut le skin*/
  private val file = a_file
  /** Contient l'image que l'on renvoie*/
  private var main_image : Option[BufferedImage] = None
  /** Ce booléen permet de savoir si on doit mettre à jour ou non l'image */
  private var up_to_date = false

  /** Renvoie l'image à la taille voulue lorque up_to_date a été mis à false.
    * 
    * On peut ne passer aucun argument à cette fonction, dans ce cas, l'image ne changera pas de taille
    * @param dim Dimension du rectangle dans lequel on veut faire tenir l'image
    * @param scale Facteur avec lequel on divise la taille del'image. Permet de mettre scale² images sur le rectangle de dimension dim
    * @param keep_ratio On peut mettre ce paramètre à false si l'on souhaite que le ratio ne soit pas conservé et que l'image soit de la taille de dim*/
  def apply(dim : Dimension = new Dimension(0,0), scale : Int = 1, keep_ratio : Boolean = true):BufferedImage =  {
    if(!up_to_date){
      if(dim.width != 0 && dim.height != 0)
        resize(dim,scale)
      up_to_date = true
    }
    main_image match {
      case None => throw new UninitializedError 
      case Some(img) => img
    }
  }

  /** Cette fonction doit être appelée lorsque l'image doit être redimensionnée*/
  def need_resize () {
    up_to_date = false
  }

  /** Permet de redimensionner l'image */
  private def resize(dim:Dimension, scale : Int) {
    val img = new ImageIcon(zoom_image(new ImageIcon(getClass.getResource(file)).getImage,dim,scale)).getImage
    val buffered = new BufferedImage(img.getWidth(null),img.getHeight(null), BufferedImage.TYPE_INT_ARGB)
    buffered.getGraphics().drawImage(img, 0, 0 , null)
    main_image = Some(buffered)
  }
}


/** Cette classe permet de stocker des informations concernant l'affichage d'une tour dans l'interface
  *
  * L'icône de choix de la tour reste constante tout au long du jeu.
  * En revanche, on garde en mémoire le nom du fichier pour la grille afin de pouvoir adapter le taille de l'image de tour à la taille des cases de la grille.
  * @param choice_file Image affichée dans le menu de choix des tours
  * @param grid_file Image utilisée pour l'affichage de la tour dans la grille
  * @param tower_type_a Type de la tour
  * @param init_dim Dimension initiale de l'icône de la grille (dimensions des boutons de la grille au délarrage du jeu)
  */
class TowerSkin(tower_type_a : TowerType) extends Skin(tower_type_a.main_icon){
  /** Icon du choix de la tour */
  val choice_icon =  new ImageIcon(getClass.getResource(tower_type_a.choice_icon))

  /** Type de la tour */
  val tower_type = tower_type_a
}

