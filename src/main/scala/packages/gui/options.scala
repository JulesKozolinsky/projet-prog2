//Ce fichier contient les classes concernant les interactions avec l'utilisateur sur le panneau supérieur du jeu

package packages
package gui

import entities._
import map._
import game._
import sugar._

import swing._

import javax.swing.ImageIcon
import javax.swing.BorderFactory



/** Contient toutes les options et les informations sur le jeu
  *
  * Exemples : information sur la vie et l'argent, options sur la tour à utiliser
  */
class GameOptions extends BorderPanel {
  border = Swing.EmptyBorder(5, 5, 5, 5)
  /** Affichage du nombre de vies */
  val life_info = new InfoGame("/little_heart.png",game.life.toString)
  /** Affichage or restant */
  val money_info = new InfoGame("/little_money.png",game.money.toString)
  /**Affichage du numéro du round en cours sur le nombre de rounds total*/
  val rounds_info = new InfoGame("Round ",fraction_to_string(game.round_counter,game.number_of_round), false)
  /**Affichage du numéro du level en cours sur le nombre de levels total*/
  val levels_info = new InfoGame("Niveau ",fraction_to_string(1,game.number_of_level), false)

  /** Or et vie */
  val life_money = new BoxPanel(Orientation.Vertical){
    contents += life_info
    contents += money_info
    contents += rounds_info
    contents += levels_info
  }

  val rounds_level = new BoxPanel(Orientation.Vertical){
    contents += rounds_info
    contents += levels_info
  }
  /** Choix de la tour */
  val tower_choice = new TowerChoice
  /** Démarrage d'un round */
  var round_button = PlayPauseButton

  /** Bouton permettant d'accélérer le jeu pendant la phase d'attaque des monstres */
  var next_button = new Button(""){
    action = new Action(""){
      icon  = new ImageIcon(getClass.getResource("/next-button.png"))
      rolloverIcon  = new ImageIcon(getClass.getResource("/next-button-rollover.png"))
      background = new Color(0,0,0,0)
      border = null
      contentAreaFilled =false

      def apply() {
        tick = (tick/2).toInt
        if (tick == 0) {//invariant : tick >0
          tick = 1
        }
        MainFrameGUI.timer.setDelay(tick)
      }
    }
  }

  /** Bouton permettant de ralentir le jeu pendant la phase d'attaque des monstres */
  var back_button = new Button(""){
    action = new Action(""){
      icon  = new ImageIcon(getClass.getResource("/back-button.png"))
      rolloverIcon = new ImageIcon(getClass.getResource("/back-button-rollover.png"))
      background = new Color(0,0,0,0)
      border = null
      contentAreaFilled =false

      def apply() {
        tick = (tick*2).toInt 
        MainFrameGUI.timer.setDelay(tick)
      }
    }
    
  }

  val left_features = new BoxPanel(Orientation.Horizontal){
    contents += life_money
    contents += Swing.HStrut(15)
    contents += back_button
    contents += round_button
    contents += next_button
    contents += Swing.HStrut(15)
    contents += rounds_level
  }

  add(left_features, BorderPanel.Position.West) //on affiche la vie et l'argent dans une colonne tout à gauche
  add(tower_choice , BorderPanel.Position.East) //on affiche le choix des tours tout à droite

  /** Synchronise le nombre de vies et l'or restant avec level */
  def actualize()
  {
    life_info.set_text(game.life.toString)
    money_info.set_text(game.money.toString)   
  }

  def actualize_end_round()
  {
    rounds_info.set_text(fraction_to_string(game.round_counter,game.number_of_round))
    levels_info.set_text(fraction_to_string(1,game.number_of_level))
    PlayPauseButton.actualize_end_round
  }
}

/************************* Pause, play, accélération ********************************/

/**Bouton pour lancer un nouveau round et pour mettre en pause la jeu*/
object PlayPauseButton extends Button("")
{
  /** Icone du bouton lorsque le jeu est en pause ou bien lorsqu'on n'est pas dans un round*/
  val play_icon = new ImageIcon(getClass.getResource("/play-button.png"))
  /** Icone du bouton lorsqu'un round est en cours*/
  val pause_icon = new ImageIcon(getClass.getResource("/pause-button.png"))
  val pause_icon_rollover = new ImageIcon(getClass.getResource("/pause-button-rollover.png"))
  val play_icon_rollover = new ImageIcon(getClass.getResource("/play-button-rollover.png"))
 

  action = new Action(""){
    icon  = play_icon
    rolloverIcon = play_icon_rollover
    background = new Color(0,0,0,0)
    border = null
    contentAreaFilled =false

    def apply(){
      
      if (!current_level.in_a_round) {
        paused = false
        
        /*icon = pause_icon
        rolloverIcon = pause_icon_rollover
        println("Début round")
        MainFrameGUI.visible*/
        MainFrameGUI.start_round
      }
      /*else {
        if (paused) { //le bouton change d'apparence lorsque l'on est dans un round. On peut alors faire une pause.
          paused = false
          MainFrameGUI.timer.start
          icon = pause_icon
          rolloverIcon = pause_icon_rollover
        }
        else {
          paused = true
          MainFrameGUI.timer.stop
          icon = play_icon
          rolloverIcon = play_icon_rollover
        }

      }*/
      icon = pause_icon
      println("clicked")
      MainFrameGUI.visible = true
    }
  }

  def actualize_end_round(){
    // on n'est plus dans un round. On doit donc mettre le bouton play
    println("fin round")
    //icon = play_icon
    rolloverIcon = play_icon_rollover
    MainFrameGUI.visible = true
  }
}

/* ********************* Tower choice **********************/

/** Permet de choisir entre plusieurs tours
  *
  */
class TowerChoice extends BoxPanel(Orientation.Horizontal) {

  private var need_default_button = true
  var default_button:ToggleButton = null
  val button_group = new ButtonGroup

  /** Permet d'ajouter un choix de tour pour le foreach */
  private def add_button(ts : TowerSkin)
  { 
    val new_button = new ToggleButton(""){

      action = new Action(""){ //lorsqu'on clique sur un bouton de choix de la tour
        background = new Color(170,170,170,255)
        icon = ts.choice_icon
        preferredSize = new Dimension(ts.choice_icon.getImage.getWidth(null),ts.choice_icon.getImage.getHeight(null))

        def apply(){
          current_tower_type = ts.tower_type
          InfoPanel.change_main_unit(current_tower_type) //on change l'info associée
          MainFrameGUI.visible = true
        }
      }
    }
    if(need_default_button){
      default_button = new_button 
      need_default_button = false
    }
    button_group.buttons += new_button
  }


  tower_skins_array.foreach(add_button)
  set_default_options

  contents ++= button_group.buttons


  /** Permet de remettre le choix par défaut.
    *
    *  Cette méthode pourrait être appliquée à chaque niveau.
    */
  def set_default_options ()
  {
    button_group.select(default_button)
    current_tower_type = tower_skins_array(0).tower_type
  }
}

/********************************** Info game *****************************/
/** Permet d'afficher une information sur le jeu avec une icône et un texte
  *
  * Exemples : nombre de vies, argent restant
  * @param file Fichier contenant une icône
  * @param is_icon Permet de préciser que le file n'est en fait pas une icône et qu'en réalité on veut du texte
  */
class InfoGame(file : String, text : String, is_icon : Boolean = true) extends BoxPanel(Orientation.Horizontal){
  border = Swing.EmptyBorder(1, 0, 0, 0)

  /** Icone représentant la donnée affichée*/

  val icone =
    if (is_icon)
      new Label("",new ImageIcon(getClass.getResource(file)),Alignment(0))
    else
      new Label(file)

  /** Label affichant l'information */
  val info = new Label(text){
    border = Swing.EmptyBorder(0,5,0,0)
  }
  contents += icone
  contents += Swing.HGlue
  contents += info

  /** Change la valeur du texte*/
  def set_text(text : String)
  {
    info.text = text
  }

}
