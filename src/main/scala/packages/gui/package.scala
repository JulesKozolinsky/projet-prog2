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
  */
package object gui {
  val tick = 100

  //def actualize_gui
  var current_level = new Level("/test1.xml")
 // var current_round = new Round(List[Tuple2[Set[Tuple2[MonsterType,Int]],Int]]())


  /** Tableau contenant les skins de tous les types de tour
    *
    * Le type par défaut au début du jeu est le premier qui apparaît dans ce tableau.
    * Attention, ce tableau ne peut pas être vide.
    */
  val tower_skins_array = Array(new TowerSkin("/choice_tower1.png", "/tower1.png", Tower1Type), new TowerSkin("/choice_tower2.png", "/tower2.png", Tower2Type))
  def tower_skins(t:TowerType) : Skin = {
    t match {
      case Tower1Type => tower_skins_array(0)
      case Tower2Type => tower_skins_array(1)
    }
  }


  val monster_skins_array = Array(new Skin("/monster1.png"),new Skin("/monster2.png"),new Skin("/monster3.png"),new Skin("/monster4.png"),new Skin("/monster5.png"),new Skin("/monster6.png"))
  def monster_skins(t:MonsterType) : Skin = 
    {
      t match {
        case Monster1Type => monster_skins_array(0)
        case Monster2Type => monster_skins_array(1)
        case Monster3Type => monster_skins_array(2)
        case Monster4Type => monster_skins_array(3)
        case Monster5Type => monster_skins_array(4)
        case Monster6Type => monster_skins_array(5)
      }
    }

  /** Indice dans skins du skin actuellement sélectionné dans tower_choice */
  var current_tower_type = tower_skins_array(0).tower_type



  /** Permet de créer une nouvelle image à partir de la première en changeant ses dimensions
    *
    * Maximise la taille de l'image afin qu'elle puisse rentrer dans la dimension imposée mais sans pour autant déformer l'image de départ.
    *
    * @param image L'image de départ
    * @param dim Les dimensions de la nouvelle image
    */
  def zoom_image (image : Image ,dim : Dimension) = {
    if(dim.width != 0 && dim.height != 0){
      val ratio:Float= (image.getWidth(null)).toFloat/ (image.getHeight(null));
      val new_width = math.min(dim.width,(ratio * dim.height).toInt);
      val new_height = (new_width / ratio).toInt
      image.getScaledInstance(new_width,new_height,java.awt.Image.SCALE_SMOOTH)
    } else
      image
  }
}
