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
  border = null
  //on remplit la grille de Labels afin de laisser swing calculer la taille de cases
  for(i<-0 to rows - 1) {//rows et columns sont héritées de GridPanel
    for(j<-0 to columns - 1) {
      contents += new Label
    }
  }


  override def paint(g:Graphics2D) {
    super.paint(g)
    g.drawLine(0,0,100,100)
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
          //n'est pas correctement actualisée : on aura width = 0 et height = 0
          if(Map.is_tower(pos)){ // S'il y une tour sur la case, on appelle build tower
            (contents(l*columns + c) match {
              case t:TowerCell => t
              case _ => throw new  ClassCastException
            }).build_tower(Map.get_tower(pos).tower_type)
          }
        }else{ //s'il y a au moins un monstre
          contents(l*columns + c) = new MonsterCell(monsters)
        }
      }}
    repaint
  }
}

class Cell(pos:Position) extends Button("")
{
  override def paint(g:Graphics2D){
    g.setColor(new java.awt.Color(100,100,100))
    g.drawRect (0, 0, size.width-1, size.height-1); 
  }
}

/** Cellule de la grille contenant un bouton permettant d'ajouter des tours 
  * 
  * Par défaut, la cellule ne contient pas de tour et on ne sait pas encore quel type de tour elle va contenir
  */
class TowerCell(pos:Position) extends Cell(pos)
{
  var is_tower = false
  var tower_type:TowerType = Tower1Type
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

  override def paint(g:Graphics2D){
    super.paint(g)
    if(is_tower)
      g.drawImage(tower_skins(tower_type)(size), null,0,0)
  }

  /** Permet de construire une tour sur la case */
  def build_tower(t:TowerType){
    tower_type = t
    is_tower = true
  }
}

class MonsterCell(wave : Set[Tuple2[MonsterType,Int]]) extends Cell (new Position(0,0))
{
  /**Définit la racine carrée du nombre maximal de monstres par case*/
  var scale = 3//Math.sqrt(wave.size).ceil.toInt
  val monsters = wave
  
  action = new Action(""){
    //lorsqu'on clique sur une case avec des monstres
    def apply(){
      println("Vous avez cliqué sur un monstre.")
    }
  }


  override def paint(g:Graphics2D){
    super.paint(g)
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


/* Ancienne version de monstercell 
/** Cellule de la grille contenant des monstres */
class MonsterCell(wave : Set[Tuple2[MonsterType,Int]]) extends GridPanel(Math.sqrt(wave.size).ceil.toInt,Math.sqrt(wave.size).ceil.toInt)
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
