package packages

package gui

import swing._
import javax.swing.Icon

/** Main Frame de la GUI
  */
class MainFrameGUI extends swing.MainFrame {
  //nom de la fenêtre principale
  title = "Tower defense"
}

/** Contient la grille des cases du jeu
  * 
  * @param nb_lines Nombre de lignes dans la grille
  * @param nb_columns Nombre de colonnes dans la grille
  * TODO : remplacer les paramètres par les paramètres de l'objet map.
  */

class Grid(nb_line:Int, nb_columns:Int) extends GridPanel(nb_line, nb_columns) /*TODO : taille map*/ {

}

/** Permet de choisir entre plusieurs tours
  * 
  * @param : Tableau contenant le nom des fichiers correspondant aux icônes des différentes tours.
*/
class TowerChoice(files:Array[String]) extends GridPanel(1,files.length) {

}

/** Permet d'afficher une information sur le jeu avec une icône et un texte 
  * 
  * Exemples : nombre de vie, argent restant
  * @param file Fichier contenant une icône
  */
class InfoGame(file : String) extends GridPanel(1,2){

}
