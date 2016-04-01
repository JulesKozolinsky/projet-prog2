package packages
package gui

import entities._
import map._
import game._
import sugar._

import swing._
import scala.swing.event._

import javax.swing._
import java.awt.Dimension
import java.awt.Font

import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.Timer

object InfoPanel extends BoxPanel(Orientation.Vertical){
  contents += new InfoTower(Tower1Type)
  contents += new InfoTower(Tower1Type)
  contents += Swing.VGlue
  contents += Log
  preferredSize = new Dimension(250,preferredSize.height)
  
  /** Cette fonction permet de changer le type de tour ou de monstre dont les infos sont affichées dans le bloc principal */
  def change_main_unit(tileable_type : TileableType)
  {
    tileable_type match {
      case tt:TowerType => contents(0) = new InfoTower(tt)
      case mt:MonsterType =>
    }
  }

}


/** Cet objet permet d'afficher des messages temporaires en temps réel à destination de l'utilisateur */
object Log extends BoxPanel(Orientation.Horizontal){
  border = BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0,0,0,100))

  /** Fonction à appliquer à chaque tick du timer */
  val taskPerformer = new ActionListener() {
    def actionPerformed(evt:ActionEvent) {
      log_text.setText("")
      MainFrameGUI.actualize
    }
  }

  /** Timer permettant de supprimer le message au bout d'un temps prédéfini*/
  val timer = new Timer(3000,taskPerformer)
  timer.setRepeats(false)


  private val log_text = new JLabel(""){
    setBorder(Swing.EmptyBorder(7,0,7,0))
  }

  peer.add(log_text)
  /** Affiche le message que l'on souhaite pendant un temps déterminé*/
  def apply(s : String) {
    log_text.setText("<html><span style=\"font-weight:normal\">" + s + "</span></html>")
    timer.restart
  }

  /** Permet d'enlever le log et de tuer le timer. Utile pour éteindre le jeu, par exemple*/
  def kill(){
    timer.stop
    log_text.setText("")
  }
}


class InfoUnit extends BoxPanel(Orientation.Vertical) {
  border = BorderFactory.createMatteBorder(4, 5, 2, 5, new Color(0,0,0,100))
  
 
  /** Permet d'ajouter une nouvelle information à la liste des informations */
  def add_attribute (name : String, description :String, file : String)
  {
    contents += new Attribute(name,description,file)
  }

}

/** Permet de donner des informations sur une tour */
class InfoTower(tower_type:TowerType) extends InfoUnit {
  val font_title = new Font("Courier New", Font.BOLD, 15)
  /*contents += new Label(tower_type.name){
    font = font_title
  }*/

  // Titre aligné à gauche :

   val title = new BoxPanel(Orientation.Horizontal) {
    border = Swing.EmptyBorder(0,10,0,0)
    val font_title = new Font("Courier New", Font.BOLD, 15)
    contents += new Label(tower_type.name){
      font = font_title
    }
    contents += Swing.HGlue
  }
  contents += title 

  add_attribute("Description : ", tower_type.description,"")
  add_attribute("Prix : ", tower_type.price.toString,"")
  
  add_attribute("Puissance d'attaque : ", tower_type.power.toString,"")
  add_attribute("Fréquence de tir : ", tower_type.frequency.toString,"")
  //contents += Swing.VGlue

}

/** Permet de connaitre les différentes entités à l'intérieur d'une case */
class CellInfo(pos:Position) extends InfoUnit {

}

/** Cette classe permet de créer une description d'un attribut d'un objet du jeu
  * Le nom de l'attribut apparaît en gras
  * La description de l'attibut apparaît en dessous
  */

class Attribute(title : String, description : String, icon_file : String) extends BoxPanel(Orientation.Vertical)
{
  border = BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(0,0,0,0)) 
  /** Police de caractère du titre */
  val font_title = new Font("Courier New", Font.BOLD, 13)
  /** Police de caractère de la description */
  val font_descr = new Font("Courier New", Font.ITALIC, 12)
  val title_label = new Label (title) {
    font = font_title
  }
  /** Ce panel permet d'aligner le titre de l'attribut à gauche */
  val title_panel = new BoxPanel(Orientation.Horizontal) {
    contents += title_label
    contents += Swing.HGlue
  }
 val descr_label = new JLabel("<html><span style=\"font-weight:normal\"><i>" + description + "</i></span></html>"){

  }

  val descr_panel = new BoxPanel(Orientation.Horizontal) {
    border = BorderFactory.createMatteBorder(0, 20, 0, 0, new Color(0,0,0,0)) 
    peer.add(descr_label)
    contents += Swing.HGlue
  }
  contents += title_panel
  contents += descr_panel
}
