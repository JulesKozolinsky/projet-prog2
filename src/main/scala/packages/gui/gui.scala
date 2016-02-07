package packages

package gui

import swing._
import javax.swing.Icon
import javax.swing.border.Border
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import java.awt.Color
import sugar._
import java.awt.Image._ 
import scala.swing.event._

/** Main Frame de la GUI
  */
class MainFrameGUI extends swing.MainFrame {
  //nom de la fenêtre principale
  title = "Tower defense"
  val frame = new GamePanel
  contents = frame
  
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
  * Exemple : information sur la vie et l'argent, options sur la tour à utiliser
  */
class GameOptions extends BorderPanel {
  border = BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0,0,0,0))
  /** Affichage du nombre de vies */
  val life_info = new InfoGame("")
  /** Affichage argent restant */
  val money_info = new InfoGame("")
  /** Choix de la tour */
  val tower_choice = new TowerChoice(Array("/choice_tower1.png","/choice_tower2.png"))
  add(new GridPanel(2,1){vGap = 10; contents += life_info;contents += money_info}, BorderPanel.Position.West) //on affiche la vie et l'argent dans une colonne tout à gauche
  add(tower_choice , BorderPanel.Position.East) //on affiche le choix des tours tout à droite
}

/** Contient la grille des cases du jeu
  * 
  * @param nb_lines Nombre de lignes dans la grille
  * @param nb_columns Nombre de colonnes dans la grille
  */
class GameGrid(nb_line:Int, nb_columns:Int) extends PosGridPanel(nb_line, nb_columns) {
  
  for(i<-0 to this.rows - 1) //rows et columns sont héritées de GridPanel
    {
      for(j<-0 to this.columns - 1)
        {
          contents += new Button("Blub")
        }
    }
  (this(new Position(1,2)) match {
    case but : Button => but
    case _  => throw new ClassCastException
  }).text = "Changé"

  val button = this(new Position(1,1))
  val reactor = new Object with Reactor

  reactor.listenTo(button)
  reactor.reactions += {
    case UIElementResized(_) => () //code exécuté quand les boutons de la grille sont redimensionnés
  }
}

/** Permet de choisir entre plusieurs tours
  * 
  * @param : Tableau contenant le nom des fichiers correspondant aux icônes des différentes tours.
  */
class TowerChoice(files:Array[String]) extends BoxPanel(Orientation.Horizontal) {
  //hGap = 7
  /** Groupe de bouttons dans lequel un seul boutton peut être sélectionné à la fois*/
  val buttonGroup = new ButtonGroup
  for(i<-0 to files.length - 1)
    {
      buttonGroup.buttons += new ToggleButton(""){
        icon = new ImageIcon(getClass.getResource(files(i)))
        iconTextGap = 0
      }
    }
  //on ajoute les bouttons au contenu du panel
  contents ++= buttonGroup.buttons
}

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
  //val image = (new ImageIcon(getClass.getResource("/little_heart.png"))).getImage
  /** Label affichant l'information */
  val info = new Label("100")
  contents += icone; contents += info

}
