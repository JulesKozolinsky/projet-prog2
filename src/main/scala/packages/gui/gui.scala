package packages
package gui

import game._
import map._
import entities._

import swing._
import scala.swing.event._

import javax.swing.ImageIcon
import java.awt.Dimension
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.swing.Timer
import java.awt.Desktop
import java.net.URI

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.Color



/******************************** Organisation de la fenêtre **************/
/** Main Frame de la GUI
  * 
  * Contient le fonction actualize qui permet une actualisation complète du jeu
  * 
  */
object MainFrameGUI extends swing.MainFrame {
  //nom de la fenêtre principale
  title = "Tower defense"
  var frame = new GamePanel

  start_game

  /** Permet de recommencer le jeu */
  def start_game(){
    frame = new GamePanel
    
    //maximize () //la fenêtre est maximisée au début de chaque niveau
    size = new Dimension(800, 600)
    preferredSize = new Dimension(800, 600)
    maximize

    contents = frame

    life = life_default
    money = money_default

    Map.initialize() //on remet la map à zéro
    current_level = new Level("/test1.xml") //chargement d'un nouveau niveau

    //actualisation
    visible = true
    actualize
    resize_icons
    repaint
  }

  /** Permet une actualisation complète 
    * 
    * si on est dans un round, la fonction actualize correspond à un tick 
    */
  def actualize() {
    frame.actualize()
    MainFrameGUI.visible = true
  }

  /** Fonction actualise à appeler uniquement à la fin des rounds */
  def actualize_end_round()
  {
    frame.actualize_end_round
  }

  /** Envoie un tick à tous les éléments du jeu */
  def send_tick(){
    if(!current_level.actualize){ //si le round est terminé
      stop_round //on arrête le timer
       if(current_level.has_won)
        {
          game_won
          current_level = new Level("/test1.xml")
        }
        
      if(current_level.has_lost)
      {
        game_over
        current_level = new Level("/test1.xml")
        //game_over2
      }
      actualize_end_round
    }
    actualize
  }

  /** Fonction à appliquer à chaque tick du timer */
  val taskPerformer = new ActionListener() {
    def actionPerformed(evt:ActionEvent) {
      send_tick
    }
  }
  var timer = new Timer(tick, taskPerformer)
  def start_round(){
    timer.start
    current_level.start_round
  }

  def stop_round(){
    timer.stop
  }


  /****************** Game won et game over **/
  /** Affiche un gros bouton "gagné" */
  def game_won(){
    contents = new Button(""){
      action = new Action(""){
        icon =  full_screen_image("/game_won.jpg")
          def apply (){start_game}
        
    }}
    maximize
    repaint
  }

  /** Affiche un gros bouton "Game over"*/
  def game_over(){
    contents = new Button(){
      action = new Action(""){
      def apply (){
        start_game
      }
      icon =  full_screen_image("/game_over.jpg")
    }}
    maximize
    repaint
  }

  /** Popopo ! */
  def game_over2(){ 
    if(Desktop.isDesktopSupported())
    {
	val url : String = "https://www.youtube.com/embed/XMdoGqcnBoo?autoplay=1"
      Desktop.getDesktop().browse(new URI(url));
    }
  }


  /** Gestion des évènements de la fenêtre
    * 
    * Lorsqu'on ferme la fenêtre, on doit arrêter le round en cours sinon le programme continue seul
    * Pause automatique lorsque le focus n'est plus sur la fenêtre
    */
  val reactor = new Object with Reactor
  reactor.listenTo(this)
  reactor.reactions += {
    case WindowClosing(_) => //Permet à la fenêtre de se fermer (si on ne fait pas ça, elle se rouvre)
      stop_round
    case WindowDeactivated(_) => //Pause automatique lorsque le focus n'est plus sur la fenêtre
      if(current_level.in_a_round && !paused)
        timer.stop
    case WindowActivated(_) =>
      if(current_level.in_a_round && !paused)
        timer.start
    case UIElementResized(_) =>
      resize_icons //les icônes doivent être actualisées
      actualize
      repaint //permet d'actualiser tous les boutons
  }


  
}



/** Contient la grille et les options du jeu
  * 
  * Les options de jeu sont en haut
  * et la grille est le Component central.
  */
class GamePanel extends BorderPanel {
  /** Options et informations sur le jeu*/
  val game_opt = new GameOptions
  /** Grille du jeu */
  val game_grid = new GameGrid(Map.height,Map.width)
  val info_panel = InfoPanel

  add(game_opt,BorderPanel.Position.North)
  add(game_grid,BorderPanel.Position.Center)
  add(info_panel,BorderPanel.Position.East)

  /** Permet d'actualiser les fonctions de l'interface à partir des informations données par la map.*/
  def actualize() {
    game_opt.actualize()
    game_grid.actualize()
  }

  def actualize_end_round(){
    game_opt.actualize_end_round()
  } 
}






