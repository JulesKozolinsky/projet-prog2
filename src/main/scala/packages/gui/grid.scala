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


  //resize_tower_icons // initialisation de la taille des tours
  for(i<-0 to this.rows - 1) //rows et columns sont héritées de GridPanel
  {
    for(j<-0 to this.columns - 1)
    {
      val set =  Set[Tuple2[MonsterType,Int]]((new Monster1Type,1),(new Monster2Type,2))
      println(set.head)
      contents +=new MonsterCell(set)      
      //new TowerCell(new Position(i,j))
    }
  }
  


  /** Permet de réagir au clics de l'utilisateur */
  val reactor = new Object with Reactor
  /* gestion des changements de taille de la fenêtre */
  reactor.listenTo(MainFrameGUI)
  reactor.reactions += {
    //code exécuté quand la fenetre est redimensionnée
    case UIElementResized(_) =>
      resize_icons
      repaint //permet d'actualiser tous les boutons
  }

  /** Permet de modifier la dimension de toutes les tower_icons */
  private def resize_icons()
  {
    for(i <- 0 to tower_skins.size - 1)
      tower_skins(i).resize_all  
    for(i <- 0 to monster_skins_array.size - 1)
      monster_skins_array(i).resize_all
  }


  /** Récupère un bouton à une position donnée */
  def get_button(pos:Position):Button =
  {
    this(pos)  match {
      case but : Button => (but)
      case _  => throw new ClassCastException
    }
  }

  override def repaint(){
    for(i<- 0 to contents.size-1){
      contents(i).repaint
    }
    super.repaint
  }

  def actualize()
  {

  }
}

/** Cellule de la grille contenant un bouton permettant d'ajouter des tours */
class TowerCell(pos:Position) extends Button("")
{
  var skin : Option[Skin] = None
  action = new Action(""){
    background = new Color(0,0,0,0)
    rolloverEnabled = false
    contentAreaFilled = false
    

    def apply(){
      if(current_level.create_new_tower(tower_skins(current_skin).tower_type,pos)){ // on doit vérifié que la map autorise une création de tour à cet endroit
        skin = Some(tower_skins(current_skin))
        icon = tower_skins(current_skin)(1,size)
        repaint
      }
      MainFrameGUI.actualize()
    }
  }

  override def repaint(){
    
      skin match {
        case Some(s) => s.resize(1,size);
        case None => ()
      }
      super.repaint
    }
}

/** Cellule de la grille contenant des monstres */
class MonsterCell(wave : Set[Tuple2[MonsterType,Int]]) extends GridPanel(Math.sqrt(wave.size).toInt,Math.sqrt(wave.size).toInt)
{
  var scale = Math.sqrt(wave.size).toInt

 // { // ici on met {} dans le vide pour que left monsters ne soit pas un attribut
    var left_monsters = wave
  //println(wave.size)
  
    while(! left_monsters.isEmpty)
    {
      var new_monster = left_monsters.head
      contents += new Label()
      {
        icon = monster_skins(new_monster._1)(scale,new Dimension(0,0))
      }
      left_monsters = left_monsters.tail
    }
//}
  
  
  
 /*contents += new Label()
  {
    icon = monster_skins(0)(scale,new Dimension(0,0))//get_icon(tower_skins(0),1)
  }
 contents += new Label()
  {
    icon = monster_skins(0)(scale,new Dimension(0,0))//get_icon(tower_skins(0),1)
  }
 contents += new Label()
  {
    icon = monster_skins(0)(scale,new Dimension(0,0))//get_icon(tower_skins(0),1)
  }*/


  override def repaint () 
  {
    for(i<-0 to monster_skins_array.size - 1)
      monster_skins_array(i).resize(scale,contents(0).size)
    for(i<-0 to contents.size - 1){
      contents(i).repaint
    }
  }
  
}
