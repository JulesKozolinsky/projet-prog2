package packages
package gui

import entities._
import map._
import game._
import sugar._

import swing._
import scala.swing.event._

import javax.swing.Icon
import javax.swing.border.Border //permet d'ajouter des bordures aux composantes afin qu'elles ne soient pas toutes collées
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import java.awt.Color
import java.awt.Image._
import java.awt.Dimension._

import scala.math
import javax.swing.ImageIcon
import java.awt.Dimension


/******************************** Organisation de la fenêtre **************/
/** Main Frame de la GUI
  */
object MainFrameGUI extends swing.MainFrame {
  //nom de la fenêtre principale
  title = "Tower defense"
  maximize () //la fenêtre est maximisée à l'ouverture
  val frame = new GamePanel
  contents = frame
  size = new Dimension(800, 600)
  def actualize() {
    frame.actualize()
  }

}

/** Contient la grille et les options du jeu
  */
class GamePanel extends BorderPanel {
  /** Options et informations sur le jeu*/
  val game_opt = new GameOptions
  /** Grille du jeu */
  val game_grid = new GameGrid(Map.height,Map.width)

  add(game_opt,BorderPanel.Position.North)
  add(game_grid,BorderPanel.Position.Center)

  /** Permet d'actualiser les fonctions de l'interface à partir des informations données par la map.*/
  def actualize() {
    game_opt.actualize()
    game_grid.actualize()
  }

}


/** Contient toutes les options et les informations sur le jeu
  *
  * Exemples : information sur la vie et l'argent, options sur la tour à utiliser
  */
class GameOptions extends BorderPanel {
  border = BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0,0,0,0))
  /** Affichage du nombre de vies */
  val life_info = new InfoGame("/little_heart.png",game.life.toString)
  /** Affichage argent restant */
  val money_info = new InfoGame("/little_money.png",game.money.toString)
  /** Choix de la tour */
  val tower_choice = new TowerChoice
  /** Démarrage d'un round */
  val round_button = new Button(""){
    action = new Action(""){
      println(getClass.getResource("/play_round_little.png").toString)
      icon  = new ImageIcon(getClass.getResource("/play_round_little.png"))

      def apply(){
        //TODO
      }
    }
  }

  /** Argent et vie */
  val infos = new BoxPanel(Orientation.Vertical){ contents += life_info;contents += money_info}

  add(new GridPanel(1,2){hGap = 25; contents += infos; contents += round_button}, BorderPanel.Position.West) //on affiche la vie et l'argent dans une colonne tout à gauche
  add(tower_choice , BorderPanel.Position.East) //on affiche le choix des tours tout à droite


  def actualize()
  {
    life_info.set_text(game.life.toString)
    money_info.set_text(game.money.toString)
  }
}



/* ************************ Skin *****************************/
/** Cette classe permet de stocker des informations concernant l'affichage d'un élément de l'interface
  *
  * On garde le fichier en mémoire parce que c'est le seul moyen de faire le zoom que j'ai trouvé *
  * @param a_file Image associée au skin
  */
class Skin(a_file:String)
{
  private val file = a_file
  /**L'icone correspondant au skin */
  var icon = new ImageIcon(getClass.getResource(file))

  /** Permet de changer la taille de l'icone
    *
    * Maximise la taille de l'image afin qu'elle puisse rentrer dans la dimension imposée mais sans pour autant la déformer
    * @param new_dim Les dimensions de la nouvelle image
    */
  def resize(new_dim : Dimension)
  {
    icon.setImage(zoom_image((new ImageIcon(getClass.getResource(file))).getImage, new_dim))
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

/* ********************* Tower choice **********************/

/** Permet de choisir entre plusieurs tours
  *
  */
class TowerChoice extends BoxPanel(Orientation.Horizontal) {

  /** Valeur par défaut de tower_choice. Le bouton est sélectionné automatiquement au début*/
  val default_skin_button =
    if(tower_skins.size > 0){
      new ToggleButton(""){
        action = new Action(""){
          icon = tower_skins(0).choice_icon
          def apply(){current_skin = 0}
        }
      }
    }else
      throw new IllegalArgumentException("Il faut renseigner au moins un skin.")

  /** Groupe de bouttons dans lequel un seul boutton peut être sélectionné à la fois*/
  val button_group = new ButtonGroup(default_skin_button){}
  set_default_options()
  for(i<-1 to tower_skins.length - 1)
  {
    button_group.buttons += new ToggleButton(""){

      action = new Action(""){ //lorsqu'on clique sur un bouton de choix de la tour
        icon = tower_skins(i).choice_icon
        def apply(){current_skin = i}
      }
    }
  }
  contents ++= button_group.buttons


  /** Permet de remettre le choix par défaut.
    *
    *  Cette méthode pourrait être appliquée à chaque niveau.
    */
  def set_default_options ()
  {
    button_group.select(default_skin_button)
    current_skin = 0
  }
}

/********************************** Info game *****************************/
/** Permet d'afficher une information sur le jeu avec une icône et un texte
  *
  * Exemples : nombre de vies, argent restant
  * @param file Fichier contenant une icône
  */
class InfoGame(file : String, text : String) extends GridPanel(1,2){

  /** Icone représentant la donnée affichée*/

  val icone = new Label("",new ImageIcon(getClass.getResource(file)),Alignment(0))
  /** Label affichant l'information */
  val info = new Label(text)
  //hGap = 5
  contents += icone; contents += info

  /** Change la valeur */
  def set_text(text : String)
  {
    info.text = text
  }

}
