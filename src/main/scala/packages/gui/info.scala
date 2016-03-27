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

object InfoPanel extends BoxPanel(Orientation.Vertical){
  contents += new InfoTower(Tower1Type)
  contents += new InfoTower(Tower1Type)
  
  /** Cette fonction permet de changer le type de tour ou de monstre dont les infos sont affichées dans le bloc principal */
  def change_main_unit(tileable_type : TileableType)
  {
    tileable_type match {
      case tt:TowerType => contents(0) = new InfoTower(tt)
      case mt:MonsterType =>
    }
  }

}

class InfoUnit extends BoxPanel(Orientation.Vertical) {
  border = BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0,0,0,100))

  //add_attribute("blub","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc diam felis, facilisis ut ligula fringilla, ultrices eleifend ligula. Vivamus in elit dui. Suspendisse convallis nisi quis aliquet aliquet. Morbi non lectus quam. Phasellus aliquet quam nec felis tempus dapibus. Donec lacus quam, tempor a dapibus eget, aliquam in sapien. Curabitur accumsan massa velit, feugiat sodales ligula porttitor at. Sed eros felis, rhoncus semper commodo ut, mattis a ligula. Nulla facilisi. Ut a arcu ac erat sodales malesuada. Ut in est vehicula, ullamcorper eros a, laoreet orci.\nAenean maximus egestas laoreet. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum tincidunt mi facilisis, porta dui ut, scelerisque purus. In hac habitasse platea dictumst ","truc")
  

  preferredSize = new Dimension(250,250)
 
  /** Permet d'ajouter une nouvelle information à la liste des informations */
  def add_attribute (name : String, description :String, file : String)
  {
    contents += new Attribute(name,description,file)
  }

}

/** Permet de donner des informations sur une tour */
class InfoTower(tower_type:TowerType) extends InfoUnit {
  val font_title = new Font("Courier New", Font.BOLD, 15)
  contents += new Label(tower_type.name){
    font = font_title
  }
  add_attribute("Description : ", tower_type.description,"")
  add_attribute("Prix : ", tower_type.price.toString,"")
  
  add_attribute("Puissance d'attaque : ", tower_type.power.toString,"")
  add_attribute("Fréquence de tir : ", tower_type.frequency.toString,"")
  contents += Swing.VGlue

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
    //font = font_descr
  }

  val descr_panel = new BoxPanel(Orientation.Horizontal) {
    border = BorderFactory.createMatteBorder(0, 20, 0, 0, new Color(0,0,0,0)) 
    peer.add(descr_label)
    contents += Swing.HGlue
  }
  contents += title_panel
  contents += descr_panel
}
