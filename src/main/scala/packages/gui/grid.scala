//contient l'implémentation de la grille

package packages
package gui

import entities._
import map._
import game._
import sugar._

import swing._
import scala.swing.event._
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image._


/** Contient la grille des cases du jeu
  *
  * On suppose qu'il y a au minimum une ligne et une colonne
  * @param nb_lines Nombre de lignes dans la grille
  * @param nb_columns Nombre de colonnes dans la grille
  */
class GameGrid(nb_line:Int, nb_columns:Int) extends PosGridPanel(nb_line, nb_columns) {

  //on remplit la grille de Labels afin de laisser swing calculer la taille de cases
  for(i<-0 to rows - 1) {//rows et columns sont héritées de GridPanel
    for(j<-0 to columns - 1) {
      contents += new Label
    }
  }

  /** Appelle le repaint des boutons contenus dans la grille (normalement, il devrait le faire mais il semble que ce ne soit pas le cas)*/
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
        if(monsters.size == 0 ){ //s'il n'y a pas de monstres sur la case
          contents(l*columns + c) = new TowerCell(pos)
          MainFrameGUI.visible = true // On doit réactualiser GUI car sinon la taille de notre nouvelle MonsterCell
          //n'est pas correctement actualisée : on aura width = 0 et height = 0
          if(Map.is_tower(pos)){ // S'il y une tour sur la case, on appelle build tower
            (contents(l*columns + c) match {
              case t:TowerCell => t
              case _ => throw new  ClassCastException
            }).build_tower(Map.get_tower(pos).tower_type)
          }
        }else{ //s'il y a au moins un monstre
          contents(l*columns + c) = new MonsterCell(monsters)
          MainFrameGUI.visible = true
        }
      }}
    repaint
  }
}

/** Cellule de la grille contenant un bouton permettant d'ajouter des tours 
  * 
  * Par défaut, la cellule ne contient pas de tour et on ne sait pas encore quel type de tour elle va contenir
  */
class TowerCell(pos:Position) extends Button("")
{
  /** skin associé à la cellule*/
  var skin : Option[Skin] = None
  action = new Action(""){
    background = new Color(0,0,0,0)
    rolloverEnabled = false
    contentAreaFilled = false

    //lorsqu'on clique sur le bouton, une tour est crée si level l'autorise
    def apply(){
      current_level.create_new_tower(current_tower_type,pos)
       MainFrameGUI.actualize()
    }
  }

  /** en plus du repaint, on vérifie que la taille est correcte */
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

class MonsterCell(wave : Set[Tuple2[MonsterType,Int]]) extends Button ("blub")
{
  var scale = 3//Math.sqrt(wave.size).ceil.toInt
  val monsters = wave
  
  action = new Action(""){

    //lorsqu'on clique sur le bouton, une tour est crée si level l'autorise
    def apply(){
      println("Vous avez cliqué sur un monstre.")
    }
  }


  override def paint(g:Graphics2D){
    var i = 0
    wave.foreach(
      m =>{
        val skin = monster_skins(m._1);
        g.drawImage(skin(size,scale), null, (i % scale) * skin().getWidth, (i /scale)* skin().getHeight)
        i += 1
      }
    )
  }


}



/** Cellule de la grille contenant des monstres */
/*class MonsterCell(wave : Set[Tuple2[MonsterType,Int]]) extends GridPanel(Math.sqrt(wave.size).ceil.toInt,Math.sqrt(wave.size).ceil.toInt)
{
  /** Taille des images de monstres dans le skin correspondant */
  var scale = Math.sqrt(wave.size).ceil.toInt
  val monsters = wave
  //on remplit la grille de Labels afin de laisser swing calculer la taille de cases
  for(i<-0 to monsters.size - 1){
    contents += new Label()
    }

  /** Initialisation des images de monstre
    *
    * L'initialisation n'est pas dans le constructeur car celle-ci doit avoir après un MainFrameGUI.visible
    *  pour que les tailles de label et de case soient à jour
    */
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

 }*/
