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
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.Timer


/******************************** Organisation de la fenêtre **************/
/** Main Frame de la GUI
  */
object MainFrameGUI extends swing.MainFrame {
  //nom de la fenêtre principale
  title = "Tower defense"
  maximize () //la fenêtre est maximisée à l'ouverture
  val frame = new GamePanel
  //game_over
  contents = frame
  size = new Dimension(800, 600)
  def actualize() {
    if(!current_level.actualize){
      stop_round
      if(current_level.has_won)
        game_won
      if(current_level.has_lost)
        game_over
    }
      
    frame.actualize()
  }

  val taskPerformer = new ActionListener() {
    def actionPerformed(evt:ActionEvent) {
      actualize
    }
  }
  val timer = new Timer(tick, taskPerformer)
  def start_round(){
    timer.start
    current_level.start_round
  }

  def stop_round(){
    timer.stop
  }

  
  val reactor = new Object with Reactor
  reactor.listenTo(this)
  reactor.reactions += {
    case WindowClosing(_) => //Permet à la fenêtre de se fermer (si on ne faity pas ça, elle se rouvre
      stop_round
    case WindowDeactivated(_) => //Pause automatique lorsque le focus n'est plus sur la fenêtre
      if(current_level.in_a_round)
        timer.stop
    case WindowActivated(_) =>
      if(current_level.in_a_round)
        timer.start
  }

  def game_over(){
    contents = new Button("Perdu")
    repaint
    //visible = true
  }
  def game_won(){
    contents = new Button("Gagné")
    repaint

    //visible = true
  }

  def restart_game(){
    contents = new GamePanel
    current_level = new Level("/test1.xml")
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
      icon  = new ImageIcon(getClass.getResource("/play_round_little.png"))

      def apply(){
        MainFrameGUI.start_round
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

/* ********************* Tower choice **********************/

/** Permet de choisir entre plusieurs tours
  *
  */
class TowerChoice extends BoxPanel(Orientation.Horizontal) {

  /** Valeur par défaut de tower_choice. Le bouton est sélectionné automatiquement au début*/
  val default_skin_button =
    if(tower_skins_array.size > 0){
      new ToggleButton(""){
        action = new Action(""){
          icon = tower_skins_array(0).choice_icon
          def apply(){current_tower_type = tower_skins_array(0).tower_type}
        }
      }
    }else
      throw new IllegalArgumentException("Il faut renseigner au moins un skin.")

  /** Groupe de bouttons dans lequel un seul boutton peut être sélectionné à la fois*/
  val button_group = new ButtonGroup(default_skin_button){}
  set_default_options()
  for(i<-1 to tower_skins_array.size - 1)
  {
    button_group.buttons += new ToggleButton(""){

      action = new Action(""){ //lorsqu'on clique sur un bouton de choix de la tour
        icon = tower_skins_array(i).choice_icon
        def apply(){current_tower_type = tower_skins_array(i).tower_type}
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
    current_tower_type = tower_skins_array(0).tower_type
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
  contents += icone; contents += info

  /** Change la valeur */
  def set_text(text : String)
  {
    info.text = text
  }

}
