package packages

import packages.game._
import entities._
import scala.math
import java.awt.image._
import java.awt.Image
import java.awt.Dimension

/** Contient des classes permettant de générer l'interface graphique du jeu
  *
  */
package object gui {

  //def actualize_gui
  var current_level = new Level("")
  var current_round = new Round(List[Tuple2[scala.collection.mutable.Set[Tuple2[Any,Int]],Int]]())

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
  val tower_skins = Array(new TowerSkin("/choice_tower1.png", "/tower1.png",new Tower1Type), new TowerSkin("/choice_tower2.png", "/tower2.png",new Tower2Type))

  /** Indice dans skins du skin actuellement sélectionné dans tower_choice */
  var current_skin = 0



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
