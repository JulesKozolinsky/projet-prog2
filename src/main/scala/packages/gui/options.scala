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
  border = BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0,0,0,0))
  /** Affichage du nombre de vies */
  val life_info = new InfoGame("/little_heart.png",game.life.toString)
  /** Affichage or restant */
  val money_info = new InfoGame("/little_money.png",game.money.toString)
  /**Affichage du numéro du round en cours sur le nombre de rounds total*/
  val rounds_info = new InfoGame("Round ",fraction_to_string(3,4), false)
  /**Affichage du numéro du level en cours sur le nombre de levels total*/
  val levels_info = new InfoGame("Niveau ",fraction_to_string(3,4), false)

  /** Or et vie */
  val life_money = new BoxPanel(Orientation.Vertical){ 
    border = BorderFactory.createMatteBorder(0, 0, 0, 15, new Color(0,0,0,0))
    contents += life_info
    contents += money_info
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

      def apply() {
        MainFrameGUI.timer.setDelay(tick/4)
      }
    }
  }

   /** Bouton permettant de ralentir le jeu pendant la phase d'attaque des monstres */
  var back_button = new Button(""){
    action = new Action(""){
      icon  = new ImageIcon(getClass.getResource("/back-button.png"))

      def apply() {
        MainFrameGUI.timer.setDelay(tick*4)
      }
    }
  }

  val left_features = new BoxPanel(Orientation.Horizontal){
    //hGap = 25
    contents += life_money
    contents += back_button 
    contents += round_button
    contents += next_button
  }

  add(left_features, BorderPanel.Position.West) //on affiche la vie et l'argent dans une colonne tout à gauche
  add(tower_choice , BorderPanel.Position.East) //on affiche le choix des tours tout à droite

  /** Synchronise le nombre de vies et l'or restant avec level */
  def actualize()
  {
    life_info.set_text(game.life.toString)
    money_info.set_text(game.money.toString)
    rounds_info.set_text(fraction_to_string(3,4))
    levels_info.set_text(fraction_to_string(3,10))
    if (!current_level.in_a_round) {round_button.icon = new ImageIcon(getClass.getResource("/play_round_little.png"))}
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

  action = new Action(""){
      icon  = play_icon

      def apply(){
        if (!current_level.in_a_round) {
          MainFrameGUI.start_round
          icon = pause_icon
        }
        else {
          if (paused) { //le bouton change d'apparence lorsque l'on est dans un round. On peut alors faire une pause.
            paused = false
            MainFrameGUI.timer.start
            icon = pause_icon
          }
          else {
            paused = true
            MainFrameGUI.timer.stop
            icon = play_icon
          }

        }
      }
    }
}

/* ********************* Tower choice **********************/

/** Permet de choisir entre plusieurs tours
  *
  */
class TowerChoice extends BoxPanel(Orientation.Horizontal) {

  /** Valeur par défaut de tower_choice. Le bouton est sélectionné automatiquement au début*/
  val default_button =
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
  val button_group = new ButtonGroup(default_button){}
  set_default_options()
  for(i<-1 to tower_skins_array.size - 1)
  {
    button_group.buttons += new ToggleButton(""){

      action = new Action(""){ //lorsqu'on clique sur un bouton de choix de la tour
        icon = tower_skins_array(i).choice_icon
        def apply(){
          current_tower_type = tower_skins_array(i).tower_type
          InfoPanel.change_main_unit(current_tower_type) //on change l'info associée 
          MainFrameGUI.visible = true
        }
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
   border = BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0,0,0,0))

  /** Icone représentant la donnée affichée*/

  val icone = 
    if (is_icon)
      new Label("",new ImageIcon(getClass.getResource(file)),Alignment(0))
    else
      new Label(file)

  /** Label affichant l'information */
  val info = new Label(text)
  contents += icone
  contents += Swing.HGlue
  contents += info

  /** Change la valeur du texte*/
  def set_text(text : String)
  {
    info.text = text
  }

}
