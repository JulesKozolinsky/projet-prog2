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
  /** Or et vie */
  val infos = new BoxPanel(Orientation.Vertical){ contents += life_info;contents += money_info}

  /** Choix de la tour */
  val tower_choice = new TowerChoice
  /** Démarrage d'un round */
  var round_button = new Button(""){
    action = new Action(""){
      icon  = new ImageIcon(getClass.getResource("/play-button.png"))

      def apply(){
        if (!current_level.in_a_round) {
          MainFrameGUI.start_round
          icon = new ImageIcon(getClass.getResource("/pause-button.png"))
        }
        else {
          if (paused) { //le bouton change d'apparence lorsque l'on est dans un round. On peut alors faire une pause.
            paused = false
            MainFrameGUI.timer.start
            icon = new ImageIcon(getClass.getResource("/pause-button.png"))
          }
          else {
            paused = true
            MainFrameGUI.timer.stop
            icon = new ImageIcon(getClass.getResource("/play-button.png"))
          }

        }
      }
    }
  }
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



  add(new GridPanel(1,2){hGap = 25; contents += infos; contents += back_button ; contents += round_button;
    contents += next_button;}, BorderPanel.Position.West) //on affiche la vie et l'argent dans une colonne tout à gauche
  add(tower_choice , BorderPanel.Position.East) //on affiche le choix des tours tout à droite

  /** Synchronise le nombre de vies et l'or restant avec level */
  def actualize()
  {
    life_info.set_text(game.life.toString)
    money_info.set_text(game.money.toString)
    if (!current_level.in_a_round) {round_button.icon = new ImageIcon(getClass.getResource("/play_round_little.png"))}
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
  */
class InfoGame(file : String, text : String) extends GridPanel(1,2){

  /** Icone représentant la donnée affichée*/

  val icone = new Label("",new ImageIcon(getClass.getResource(file)),Alignment(0))
  /** Label affichant l'information */
  val info = new Label(text)
  contents += icone; contents += info

  /** Change la valeur du texte*/
  def set_text(text : String)
  {
    info.text = text
  }

}
