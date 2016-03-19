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
  * Plusieurs tailles de l'image sont conservées :
  * 0 correspondant à la taille de l'image initiale
  * 1 est la taille d'une cellule de la grille
  * n est l'icône de la taille correspondant à une cellule divisée en n² cellules
  * Utilisation de l'évaluation paresseuse : on ne crée une image que si on en a besoin. De plus, lorsque toutes les images doivent changer de taille, il faut le préciser à l'objet (resize_all)
  * @param a_file Image associée au skin
  */
class Skin2(a_file:String)
{
  /** Le fichier contenant l'image dont on veut le skin*/
  private val file = a_file
  private var main_image : Option[BufferedImage] = None
  private var up_to_date = false

  def apply(dim : Dimension = new Dimension(0,0), scale : Int = 1, keep_ratio : Boolean = true):BufferedImage =  {
    if(!up_to_date){
      resize(dim,scale)
      up_to_date = true
    }
    main_image match {
      case None => throw new UninitializedError 
      case Some(img) => img
    }
  }

  def need_resize () {
    up_to_date = false
  }

  private def resize(dim:Dimension, scale : Int) {
    val img = new ImageIcon(zoom_image(new ImageIcon(getClass.getResource(file)).getImage,dim,scale)).getImage
    val buffered = new BufferedImage(img.getWidth(null),img.getHeight(null), BufferedImage.TYPE_INT_ARGB)
    buffered.getGraphics().drawImage(img, 0, 0 , null);
    main_image = Some(buffered)
  }
}

/* ************************ Skin *****************************/
/** Cette classe permet de stocker des informations concernant l'affichage d'un élément de l'interface
  *
  * On garde le fichier en mémoire parce que c'est le seul moyen de faire le zoom que j'ai trouvé
  * Plusieurs tailles de l'image sont conservées :
  * 0 correspondant à la taille de l'image initiale
  * 1 est la taille d'une cellule de la grille
  * n est l'icône de la taille correspondant à une cellule divisée en n² cellules
  * Utilisation de l'évaluation paresseuse : on ne crée une image que si on en a besoin. De plus, lorsque toutes les images doivent changer de taille, il faut le préciser à l'objet (resize_all)
  * @param a_file Image associée au skin
  */
class Skin(a_file:String)
{
  /** Le fichier contenant l'image dont on veut le skin*/
  private val file = a_file

  /** Tableau contenant différentes tailles pour l'icône
    *
    * 0 correspondant à la taille de l'image initiale
    * 1 est la taille d'une cellule de la grille
    * n est l'icône de la taille correspondant à une cellule divisée en n² cellules
    */
  private var icons = Array[Option[ImageIcon]](Some(icon0))

  /**Permet de savoir si l'image est bien à jour ou non. 
    * 
    * La fonction resize_all permet justement qu'aucune des images ne soit à jour, sauf 0
    */
  private var up_to_date = Array[Boolean](true)

  /** Permet d'obtenir l'image dans sa dimension d'origine */
  private def icon0(): ImageIcon = {
    new ImageIcon(getClass.getResource(file))
  }

  
  def apply(scale:Int, dim:Dimension):ImageIcon =  {
    resize(scale,dim)
    icons(scale) match {
      case Some(ic) => ic
      case None => throw new IllegalArgumentException("La fonction resize a échoué.") // ne doit jamais arriver
    }}

  /** Permet de réinitialiser le tableau d'icônes */
  def init()  {
    icons = Array[Option[ImageIcon]](Some(icon0()))
  }

  /** Permet de dire à skin qu'il faut recalculer les images quand on en aura besoin */
  def resize_all(){
    for(i <- 1 to icons.size - 1){
      up_to_date(i) = false
    }
  }

  /** Permet de changer la taille de l'icône
    *
    * Maximise la taille de l'image afin qu'elle puisse rentrer dans la dimension imposée mais sans pour autant la déformer
    * @param new_dim Les dimensions de la nouvelle image
    * @param scale Échelle redimensionnée
    */
  def resize( scale : Int , new_dim : Dimension)
  {
    if(scale < icons.size){ //si l'image peut déjà se trouver dans le tableau
      if(!up_to_date(scale)){
        up_to_date(scale) = true
        icons(scale) = icons(scale) match {
          case None => Some(new ImageIcon(zoom_image(icon0.getImage,new_dim)))
          case Some(ic) => ic.setImage(zoom_image(icon0.getImage,new_dim))
            Some(ic)
        }
      }}
      else
      {
        while(icons.size < scale)
        {
          icons = icons.:+(None) //:+ append
          up_to_date = up_to_date.:+(false)
        }

        icons = icons.:+ (Some(new ImageIcon(zoom_image(icon0.getImage,new_dim))))
        up_to_date = up_to_date.:+(true)
      }
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
class TowerSkin(choice_file:String, grid_file_a:String, tower_type_a : TowerType) extends Skin(grid_file_a){
  /** Icon du choix de la tour */
  val choice_icon =  new ImageIcon(getClass.getResource(choice_file))

  /** Nom du fichier de l'image pour la grille */
  private val grid_file = grid_file_a

  /** Type de la tour */
  val tower_type = tower_type_a
}
