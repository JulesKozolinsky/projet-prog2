package packages
package gui

import entities._

import swing._

import javax.swing.ImageIcon

/* ************************ Skin *****************************/
/** Cette classe permet de stocker des informations concernant l'affichage d'un élément de l'interface
  *
  * On garde le fichier en mémoire parce que c'est le seul moyen de faire le zoom que j'ai trouvé
  * Plusieurs tailles de l'image sont conservées :
  * 0 correspondant à la taille de l'image initiale
  * 1 est la taille d'une cellule de la grille
  * n est l'icone de la taille correspondant à une cellule divisée en n2 cellules
  * Utilisation de l'évaluation paresseuse : on ne crée une image que si on en a besoin. De plus, lorsque toutes les images doivent changer de taille, il faut le préciser à l'objet
  * @param a_file Image associée au skin
  */
class Skin(a_file:String)
{
  private val file = a_file

  /** Tableau contenant différentes tailles pour l'icone
    *
    * 0 correspondant à la taille de l'image initiale
    * 1 est la taille d'une cellule de la grille
    * n est l'icone de la taille correspondant à une cellule divisée en n2 cellules
    */
  private var icons = Array[Option[ImageIcon]](Some(icon0))

  /**Permet de savoir si l'image est bien à jour ou non. */
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

  /** Permet de réinitialiser le tableau d'icones */
  def init()  {
    icons = Array[Option[ImageIcon]](Some(icon0()))
  }

  def resize_all(){
    for(i <- 1 to icons.size - 1){
      up_to_date(i) = false
    }
  }

  /** Permet de changer la taille de l'icone
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
  * L'icone de choix de la tour reste constante tout au long du jeu.
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
