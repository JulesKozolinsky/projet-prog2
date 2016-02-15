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
class MainFrameGUI extends swing.MainFrame {
  //nom de la fenêtre principale
  title = "Tower defense"
  maximize () //la fenêtre est maximisée à l'ouverture
  val frame = new GamePanel
  contents = frame
  size = new Dimension(800, 600)
  
}

/** Contient la grille et les options du jeu
  */
class GamePanel extends BorderPanel {
  /** Options et informations sur le jeu*/
  val game_opt = new GameOptions
  /** Grille du jeu */
  val game_grid = new GameGrid(9,20)

  add(game_opt,BorderPanel.Position.North)
  add(game_grid,BorderPanel.Position.Center)

  /** Permet d'actualiser les fonctions de l'interface à partir des informations données par la map.*/
  def actualize() {}

}


/** Contient toutes les options et les informations sur le jeu
  * 
  * Exemples : information sur la vie et l'argent, options sur la tour à utiliser
  */
class GameOptions extends BorderPanel {
  border = BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0,0,0,0))
  /** Affichage du nombre de vies */
  val life_info = new InfoGame("")
  /** Affichage argent restant */
  val money_info = new InfoGame("")
  /** Choix de la tour */
  val tower_choice = new TowerChoice(tower_skins)
  add(new GridPanel(2,1){vGap = 10; contents += life_info;contents += money_info}, BorderPanel.Position.West) //on affiche la vie et l'argent dans une colonne tout à gauche
  add(tower_choice , BorderPanel.Position.East) //on affiche le choix des tours tout à droite
}



/******************************* Grille  du jeu **********************************/



/** Contient la grille des cases du jeu
  * 
  * On suppose qu'il y a au minimum une ligne et une colonne
  * @param nb_lines Nombre de lignes dans la grille
  * @param nb_columns Nombre de colonnes dans la grille
  */
class GameGrid(nb_line:Int, nb_columns:Int) extends PosGridPanel(nb_line, nb_columns) {
  
  for(i<-0 to this.rows - 1) //rows et columns sont héritées de GridPanel
    {
      for(j<-0 to this.columns - 1)
        {
          contents += new Button(""){
            action = new Action(""){
              background = new Color(0,0,0,0)
              rolloverEnabled = false
              
              def apply(){
                contentAreaFilled = false
                add_tower(new Position(i,j),tower_skins(current_skin))
              }
            }
          }
        }
    }

  /** Permet de réagir au clics de l'utilisateur */
  val reactor = new Object with Reactor
  /* gestion des changements de taille de la fenêtre */
  reactor.listenTo(get_button(new Position(0,0)))
  reactor.reactions += {
    //code exécuté quand les boutons de la grille sont redimensionnés
    case UIElementResized(_) =>  
      for(i <- 0 to tower_skins.size - 1)
        {
          tower_skins(i).resize_grid_icon(get_button(new Position(0,0)).size) 
        }
      this.repaint //permet d'actualiser tous les boutons
  }


  /** Ajoute une tour sur la map 
    * 
    * @param t Type de la tour ajoutée.
    */
  def add_tower(pos:Position , skin : TowerSkin)
  {
    /*if(Map.new_tower(skin.tower_type,pos)) // on doit vérifié que la map autorise une création de tour à cet endroit
      {
        show_tilable(pos,skin)
      }*/
    show_tilable(pos,skin) //TODO : delete this line when Jules' bug is fixed
  }

  /** Récupère un bouton à une position donnée */
  def get_button(pos:Position):Button = 
  {
    this(pos)  match {
      case but : Button => (but)
      case _  => throw new ClassCastException
    }
  }


  /**Affiche un tileble avec le skin spécifié au bouton à la position pos */
  def show_tilable(pos : Position, skin : TowerSkin)
  {
    val to_modify = get_button(pos)
    to_modify.icon = skin.grid_icon
    to_modify.repaint
  }

  def actualize()
  {

  }
}


/* ********************* Tower choice **********************/
/** Cette classe permet de stocker des informations consernant l'affichage d'une tour dans l'interface
  * 
  * L'icone de choix de la tour reste constante tout au long du jeu. 
  * En revanche, on garde en mémoire le nom du fichier pour la grille afin de pouvoir adapter le taille de l'image de tour à la taille des cases de la grille.
  * @param choice_file Image affichée dans le menu de choix des tours
  * @param grid_file Image utilisée pour l'affichage de la tour dans la grille
  * @param tower_type_a Type de la tour 
  * @param init_dim Dimension initiale de l'icône de la grille (dimensions des boutons de la grille au délarrage du jeu)
  */
class TowerSkin(choice_file:String, grid_file_a:String, tower_type_a : TowerType){
  /** Icon du choix de la tour */
  val choice_icon =  new ImageIcon(getClass.getResource(choice_file))

  /** Nom du fichier de l'image pour la grille */
  private val grid_file = grid_file_a

  /** Icone de la grille */
  var grid_icon = new ImageIcon(getClass.getResource(grid_file))
  
  /** Type de la tour */
  val tower_type = tower_type_a

  /** Permet de changer la taille de grid_icons
    * 
    * Maximise la taille de l'image afin qu'elle puisse rentrer dans la dimension imposée mais sans pour autant déformer grid_icon_full
    * @param new_dim Les dimensions de la nouvelle image
    */
  def resize_grid_icon(new_dim : Dimension)
  {
    grid_icon.setImage(zoom_image((new ImageIcon(getClass.getResource(grid_file))).getImage, new_dim))
  }
}



/** Permet de choisir entre plusieurs tours
  * 
  * @param : Tableau contenant le nom des fichiers correspondant aux icônes des différentes tours.
  */
class TowerChoice(tower_skins:Array[TowerSkin]) extends BoxPanel(Orientation.Horizontal) {
  
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
class InfoGame(file : String) extends GridPanel(1,2){
  /** text contient le nombre de vies par exemple*/
  var text = "100"
  /** Icone représentant la donnée affichée*/
  
  val icone = new Label("",new ImageIcon(getClass.getResource("/little_heart.png")),Alignment(0))
  /** Label affichant l'information */
  val info = new Label("100")
  contents += icone; contents += info

}

