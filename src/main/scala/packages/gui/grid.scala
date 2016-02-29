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
  for(i<-0 to rows - 1) {//rows et columns sont héritées de GridPanel
    for(j<-0 to columns - 1) {
      contents += new Label()
    }
  }



  /** Permet de réagir au clics de l'utilisateur */
  val reactor = new Object with Reactor

  // gestion des changements de taille de la fenêtre
  reactor.listenTo(MainFrameGUI)
  reactor.reactions += {
    //code exécuté quand la fenetre est redimensionnée
    case UIElementResized(_) =>
      resize_icons
      repaint //permet d'actualiser tous les boutons
  }

  /** Permet de modifier la dimension de toutes les tower_icons */
  private def resize_icons() {
    for(i <- 0 to tower_skins_array.size - 1)
      tower_skins_array(i).resize_all
    for(i <- 0 to monster_skins_array.size - 1)
      monster_skins_array(i).resize_all
  }


  /** Récupère un bouton à une position donnée */
  def get_button(pos:Position):Button = {
    this(pos)  match {
      case but : Button => (but)
      case _  => throw new ClassCastException
    }
  }

  /** Appelle le repaint des boutons contenus dans la grille*/
  override def repaint(){
    for(i<- 0 to contents.size-1){
      contents(i).repaint
    }
    super.repaint
  }

  /** Actualise la grille en fonction de l'état de la Map.*/
  def actualize()
  {
    for(l<-0 to rows - 1) {//rows et columns sont héritées de GridPanel
      for(c<-0 to columns - 1) {
        val pos = new Position(l,c)
        val monsters = Map.get_monsters(pos)
        if(monsters.size == 0 ){
          contents(l*columns + c) = new TowerCell(pos)
          //MainFrameGUI.frame.game_grid.revalidate
          MainFrameGUI.visible = true // WTFFF si on l'enlève, énorme bug.
          if(Map.is_tower(pos)){
            (contents(l*columns + c) match {
              case t:TowerCell => t
              case _ => throw new  ClassCastException
            }).build_tower(Map.get_tower(pos).tower_type)
          }
        }else{
          contents(l*columns + c) = new MonsterCell(monsters)
          MainFrameGUI.visible = true
          (contents(l*columns + c) match {
            case m:MonsterCell => m
            case _ => throw new ClassCastException
          }).initialize_icons
        }
      }}
    repaint
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
      current_level.create_new_tower(current_tower_type,pos)
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

  /** Permet de construire une tour sur la case */
  def build_tower(t:TowerType){
    skin = Some(tower_skins(t))
    icon = tower_skins(t)(1,size) //on récupère le skin de scale 1 et on le crée s'il n'existe pas
      repaint
  }
}

/** Cellule de la grille contenant des monstres */
class MonsterCell(wave : Set[Tuple2[MonsterType,Int]]) extends GridPanel(Math.sqrt(wave.size).ceil.toInt,Math.sqrt(wave.size).ceil.toInt)
{
  var scale = Math.sqrt(wave.size).ceil.toInt
  val monsters = wave
  for(i<-0 to monsters.size - 1){
    contents += new Label()
    }

  /** Initialisation des images de monstre
    *
    * L'initialisation n'est pas dans le constructeur car celle-ci doit avoir après un MainFrameGUI.visible
    *  pour que les taille de label et de case soient à jour*/
  def initialize_icons() {
    var left_monsters = monsters
    var i = 0
    while(! left_monsters.isEmpty) {
      var new_monster = left_monsters.head
      (contents(i) match {
        case l:Label => l
        case _ => throw new ClassCastException
      }).icon = monster_skins(new_monster._1)(scale,contents(0).size)
      left_monsters = left_monsters.tail
      i+= 1
    }
    repaint
  }



  override def repaint ()
  {
    for(i<-0 to monster_skins_array.size - 1)
      monster_skins_array(i).resize(scale,contents(0).size)
    for(i<-0 to contents.size - 1){
      contents(i).repaint
    }
  }

}
