//contient l'implémentation de la grille
package packages
package gui

import entities._
import map._
import game._
import sugar._

import swing._
import scala.swing.event._


/** Contient la grille des cases du jeu
  *
  * On suppose qu'il y a au minimum une ligne et une colonne
  * @param nb_lines Nombre de lignes dans la grille
  * @param nb_columns Nombre de colonnes dans la grille
  */
class GameGrid(nb_line:Int, nb_columns:Int) extends PosGridPanel(nb_line, nb_columns) {

  contents += new TowerCell(new Position(0,0))
  resize_tower_icons // initialisation de la taille des tours
  for(i<-0 to this.rows - 1) //rows et columns sont héritées de GridPanel
  {
    for(j<-0 to this.columns - 1)
    {
      if (i != 0 && j != 0)
        contents += new MonsterCell(Set[Tuple2[Tileable,Int]]((new Monster1,1),(new Monster1,1),(new Monster1,1),(new Monster1,1)))
    }
  }
  


  /** Permet de réagir au clics de l'utilisateur */
  val reactor = new Object with Reactor
  /* gestion des changements de taille de la fenêtre */
  reactor.listenTo(get_button(new Position(0,0)))
  reactor.reactions += {
    //code exécuté quand les boutons de la grille sont redimensionnés
    case UIElementResized(_) =>
      resize_tower_icons
      this.repaint //permet d'actualiser tous les boutons
  }

  /** Permet de modifier la dimension de toutes les tower_icons */
  private def resize_tower_icons()
  {
    for(i <- 0 to tower_skins.size - 1)
      {
        tower_skins(i).resize(get_button(new Position(0,0)).size, 1)
      }
  }


  /** Récupère un bouton à une position donnée */
  def get_button(pos:Position):Button =
  {
    this(pos)  match {
      case but : Button => (but)
      case _  => throw new ClassCastException
    }
  }

  def actualize()
  {

  }
}

/** Cellule de la grille contenant un bouton permettant d'ajouter des tours */
class TowerCell(pos:Position) extends Button("")
{
  action = new Action(""){
    background = new Color(0,0,0,0)
    rolloverEnabled = false
    contentAreaFilled = false

    def apply(){
      if(current_level.create_new_tower(tower_skins(current_skin).tower_type,pos)) // on doit vérifié que la map autorise une création de tour à cet endroit
        {
          icon = get_icon(tower_skins(current_skin),1)
          repaint
        }
      MainFrameGUI.actualize()
    }
  }
}

/** Cellule de la grille contenant des monstres */
class MonsterCell(wave : Set[Tuple2[Tileable,Int]]) extends GridPanel(Math.sqrt(wave.size).toInt,Math.sqrt(wave.size).toInt)
{
  var left_monsters = wave
  contents += new Label("100")
  {
    icon = get_icon(tower_skins(0),1)
  }
 contents += new Label("100")
  {
    icon = get_icon(tower_skins(0),1)
  }
 contents += new Label("100")
  {
    icon =get_icon(tower_skins(0),1)
  }
 contents += new Label("100")
  {
    icon = get_icon(tower_skins(0),1)
  }
  /*while(! left_monsters.isEmpty)
   {

   }*/
}
