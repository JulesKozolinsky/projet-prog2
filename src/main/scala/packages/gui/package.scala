package packages

import packages.game._
import entities._
import scala.math
import java.awt.image._
import java.awt.Image
import java.awt.Dimension
import javax.swing.ImageIcon

/** Contient des classes permettant de générer l'interface graphique du jeu
  *
  * Objet principal : MainFrameGui
  * Contient un GamePanel qui fait la séparation entre la grille (GameGrid) et les options (GameOptions)
  */
package object gui {

  /** Le niveau dans lequel on joue. Cette variable est mise à jour par MainFrameGUI */
  var current_level = new Level("/test1.xml")

  /** Vaut true si le jeu est en pause et false sinon. Cette variable est mise à jour par MainFrameGUI */
  var paused = false 


  /** Tableau contenant les skins de tous les types de tour
    *
    * Le type de tour par défaut au début du jeu est le premier qui apparaît dans ce tableau.
    * Attention, ce tableau ne peut pas être vide.
    */
  val tower_skins_array = new Array[TowerSkin](unlocked_towers.size)

  def actualize_tower_skins(){
    var i = 0 
    unlocked_towers.foreach({tt: TowerType => tower_skins_array(i) =  new TowerSkin(tt);i+= 1 })
  }
  actualize_tower_skins()

  /** Type actuel. Cette variable est mise à jour par TowerChoice*/
  var current_tower_type = tower_skins_array(0).tower_type

  /** Convertit un TowerType en skin */
  def tower_skins(t:TowerType) : Skin = {
    tower_skins_array(t.id-1)
  }

  /** Tableau contenant les skins de tous les types de monstre
    *
    * Attention, ce tableau ne peut pas être vide.
    */
  val monster_skins_array = Array(new Skin("/monster1.png"),new Skin("/monster2.png"),new Skin("/monster3.png"),new Skin("/monster4.png"),new Skin("/monster5.png"),new Skin("/monster6.png"),new Skin("/monster7.png"),new Skin("/monster8.png"))
  /** Convertit un MonsterType en skin */
  def monster_skins(t:MonsterType) : Skin =
    {
      monster_skins_array(t.id-1)
    }

  /** Permet de modifier la dimension de toutes les tower_icons */
  def resize_icons() {
    tower_skins_array.foreach(el => el.need_resize)
    monster_skins_array.foreach(el => el.need_resize)
  }


  /** Permet de créer une nouvelle image à partir de la première en changeant ses dimensions
    *
    * Si keep_ratio vaut true, maximise la taille de l'image afin qu'elle puisse rentrer dans la dimension imposée mais sans pour autant déformer l'image de départ.
    *
    * @param image L'image de départ
    * @param dim Les dimensions de la nouvelle image
    * @param keep_ratio Lorsque keep_ratio vaut true, on conserve le ratio.
    */
  def zoom_image (image : Image ,dim : Dimension, scale : Int = 1, keep_ratio : Boolean = true) = {
    if(dim.width != 0 && dim.height != 0){
      var new_dim = dim
      if(keep_ratio)
        new_dim = get_keep_ratio_dim(new Dimension(image.getWidth(null), image.getHeight(null)), dim) 
      new_dim.width /= scale
      new_dim.height /= scale
      image.getScaledInstance(new_dim.width,new_dim.height,java.awt.Image.SCALE_SMOOTH)
    } else
      image
  }

  /** Permet de calculer la dimension pour garder le ratio d'une image 
    * 
    * @param img_dim Taille de l'image que l'on souhaite faire rentrer dans unrectangloe de dimension bounds
    * @param bound Dimension du rectangle contenant l'image
    * @param scale Permet de diviser la taille de l'image par scale. Par exemple s'il y a 9 monstres , le scale correspondant est de 3 */
  def get_keep_ratio_dim (img_dim : Dimension, bounds : Dimension) = 
  {
    val ratio:Float= img_dim.width.toFloat/ (img_dim.height.toFloat);
    val new_width = math.min(bounds.width,(ratio * bounds.height).toInt);
    val new_height = (new_width / ratio).toInt
    new Dimension(new_width,new_height)
  }

  /** Renvoie une image de la taille de la fenêtre */
  def full_screen_image (file:String) : ImageIcon = {
    new ImageIcon(new ImageIcon(getClass.getResource(file)).getImage.getScaledInstance(java.awt.Toolkit.getDefaultToolkit().getScreenSize.width,java.awt.Toolkit.getDefaultToolkit().getScreenSize.height,java.awt.Image.SCALE_SMOOTH))
  }


  /**Renvoie la chaîne de caractères représentant la fraction des deux entiers en paramètre*/
  def fraction_to_string(dividend:Int , divisor : Int) : String = {
    dividend.toString + "/" + divisor.toString
  }
}
