package packages
package gui

import entities._
import map._
import game._
import sugar._

import swing._
import scala.swing.event._

//import javax.swing.
import javax.swing.BorderFactory
import javax.swing.JLabel
import java.awt.Dimension
import java.awt.Font

import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.Timer

object InfoPanel extends BoxPanel(Orientation.Vertical){
  contents += new TowerTypeInfo(current_tower_type)
  contents += Swing.VGlue
  contents += Swing.VGlue
  contents += Log
  preferredSize = new Dimension(250,preferredSize.height)
  
  
  private def create_info_unit(tileable_type : TileableType) : InfoUnit = {
    tileable_type match {
      case tt:TowerType =>  new TowerTypeInfo(tt)
      case mt:MonsterType => new MonsterTypeInfo(mt)
    }
  }

  private def delete_secundary_units(){
    for(i<- 1 to contents.length - 3)
      {
        contents(i) = Swing.VGlue
      }
  }

  /** Cette fonction permet de changer le type de tour ou de monstre dont les infos sont affichées dans le bloc principal */
  def change_main_unit_tileable(tileable_type :TileableType) {
    contents(0) = create_info_unit(tileable_type)
    delete_secundary_units
    repaint
  }

  def change_main_unit_cell(pos : Position){
    contents(0) = new CellInfo(pos)
    delete_secundary_units
    repaint
  }

  /** Permet d'ajouter ou de changer la deuxième info unit*/
  def change_second_unit(tileable_type : TileableType)
  {
    contents(1) = create_info_unit(tileable_type)
    MainFrameGUI.actualize
  }
}




class InfoUnit extends BoxPanel(Orientation.Vertical) {


  /** Nom de l'unité d'information */
  val title = new BoxPanel(Orientation.Horizontal) {
    border = Swing.EmptyBorder(10,10,0,0)
    val font_title = new Font("Courier New", Font.BOLD, 17)
    contents += new Label(""){
      font = font_title
    }
    contents += Swing.HGlue
  }
  contents += title

  border = BorderFactory.createMatteBorder(4, 5, 2, 5, new Color(0,0,0,100))
  
  /** Permet d'ajouter une nouvelle information à la liste des informations */
  def add_attribute (name : String, description :String, file : String = "")
  {
    contents += new Attribute(name,description,file)
  }

  def set_title(s : String){
    title.contents(0) match {
      case l : Label => l.text = s
      case _ => new ClassCastException("Le titre n'est pas un Label.")
    }
  }

}

class TileableAttribute(quantity : Int,tileable_type : TileableType) extends BoxPanel(Orientation.Horizontal)
{
  border = Swing.EmptyBorder(0,0,10,10)
  val quantity_label = new Label(quantity.toString){
     border = Swing.EmptyBorder(0,0,0,10)
  }
  val link_to_info = new Button(tileable_type.name){
    action = new Action(tileable_type.name){
      contentAreaFilled = false
      def apply(){
        InfoPanel.change_second_unit(tileable_type)
      }
    }
  }

  contents += quantity_label
  //contents += Swing.HStrut(10)
  contents += link_to_info
}

/** Permet de connaitre les différentes entités à l'intérieur d'une case */
class CellInfo(pos:Position) extends InfoUnit {
  set_title("Terrain")
  
  if(Map.is_tower(pos))
    {
      add_attribute("Contenu", "")
      contents += new TileableAttribute(1,Map.get_tower(pos).tower_type)
    }else {
      val monsters = Map.get_monsters(pos)
      if(Map.get_monsters(pos).isEmpty)
      {
        add_attribute("Contenu","Cette case est vide.")
      }else
      {
        add_attribute("Contenu","")
        for(c <- monsters){
          contents += new TileableAttribute(c._2,c._1.monster_type)
        }
      }
    }

}



/** Permet de donner des informations sur une tour */
class TowerTypeInfo(tower_type:TowerType) extends InfoUnit {
  
  set_title(tower_type.name)
  add_attribute("Description : ", tower_type.description)
  add_attribute("Prix : ", tower_type.price.toString)
  
  add_attribute("Puissance d'attaque : ", tower_type.power.toString)
  add_attribute("Fréquence de tir : ", tower_type.frequency.toString)
  //contents += Swing.VGlue

}

class TowerInfo(tower : Tower) extends TowerTypeInfo(tower.tower_type){
  
}

class MonsterTypeInfo(monster_type : MonsterType) extends InfoUnit {
  set_title(monster_type.name)
  add_attribute("Description : ", monster_type.description)
  add_attribute("Vies : ", fraction_to_string(monster_type.max_life,monster_type.max_life))
  add_attribute("Prime : ", monster_type.gold.toString)
  add_attribute("Vitesse : ", monster_type.slowness.toString)
}

/*class MonsterInfo(monster : Monster) extends InfoUnit {
set_title(monster.monster_type.name)
add_attribute("Description : ", monster.monster_type.description)
add_attribute("Vies : ", fraction_to_string(monster.life,monster.monster_type.max_life))
add_attribute("Prime : ", monster.monster_type.gold.toString)
add_attribute("Vitesse : ", monster.monster_type.slowness.toString)
add_attribute("Orientation horizontale :", monster.orientation_h.toString)
add_attribute("Orientation verticale :", monster.orientation_v.toString)
add_attribute("Absolute padding h :", monster.absolute_padding_h.toString)
add_attribute("Absolute padding v :", monster.absolute_padding_v.toString)
}*/


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
