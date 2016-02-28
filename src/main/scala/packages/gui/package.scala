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

  //def actualize_gui
  var current_level = new Level("/test1.xml")
  var current_round = new Round(List[Tuple2[Set[Tuple2[MonsterType,Int]],Int]]())

  /** Permet de créer un Timer
    *
    * Usage : Timer(100){println("hey")} crée un timer qui affichera "hey" toutes les 100 millisecondes.
    */
  object Timer {
    /**
      *
      * @param interval Interval entre les ticks
      * @param repeats true si le Timer doit se répéter à l'infini, false sinon. La valeur par défaut est true
      * @param op La fonction à appliqcuer à chaque tick
      */
    def apply(interval: Int, repeats: Boolean = true)(op: => Unit) {
      val timeOut = new javax.swing.AbstractAction() {
        def actionPerformed(e : java.awt.event.ActionEvent) = op
      }
      /** Timer de java*/
      val t = new javax.swing.Timer(interval, timeOut)
      t.setRepeats(repeats)
      t.start()
    }
  }

  /** Tableau contenant les skins de tous les types de tour
    *
    * Le type par défaut au début du jeu est le premier qui apparaît dans ce tableau.
    * Attention, ce tableau ne peut pas être vide.
    */
  val tower_skins_array = Array(new TowerSkin("/choice_tower1.png", "/tower1.png",new Tower1Type), new TowerSkin("/choice_tower2.png", "/tower2.png",new Tower2Type))
  def tower_skins(t:TowerType) : Skin = {
    t match {
      case Tower1Type() => tower_skins_array(0)
      case Tower2Type() => tower_skins_array(1)
    }
  }


  val monster_skins_array = Array(new Skin("/monster1.png"),new Skin("/monster2.png"))
  def monster_skins(t:MonsterType) : Skin = 
    {
      t match {
        case Monster1Type() => monster_skins_array(0)
        case Monster2Type() => monster_skins_array(1)
      }
    }

  /** Indice dans skins du skin actuellement sélectionné dans tower_choice */
  var current_tower_type = tower_skins_array(0).tower_type

 /* def get_icon(skin:Skin,scale : Int):ImageIcon =
  {
    skin(scale) match {
      case Some(ic) => ic
      case None => throw new IllegalArgumentException("La redimension 1 n'est pas définie pour les tours.") // ne doit jamais arriver
    }
  }*/


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
