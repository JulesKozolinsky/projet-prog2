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
import javax.swing.BorderFactory
import java.awt.RenderingHints //pour l'anticrénelage



/** Contient la grille des cases du jeu
  *
  * On suppose qu'il y a au minimum une ligne et une colonne
  * @param nb_lines Nombre de lignes dans la grille
  * @param nb_columns Nombre de colonnes dans la grille
  */
class GameGrid(nb_line:Int, nb_columns:Int) extends PosGridPanel(nb_line, nb_columns) {
  //border = BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(0,0,0,255))
  //on remplit la grille de Labels afin de laisser swing calculer la taille de cases
  for(i<-0 to rows - 1) {//rows et columns sont héritées de GridPanel
    for(j<-0 to columns - 1) {
      contents += new Label
    }
  }


  def paintLaser(g: Graphics2D, cell1 : Position , cell2 :Position, color : java.awt.Color )
  {
    g.setColor(color)
    val center1 = center_of_cell(cell1)
    val center2 = center_of_cell(cell2)
    g.drawLine(center1.c,center1.l,center2.c,center2.l)
  }

  override def paint(g:Graphics2D) {
    //anticrénelage pour les lasers
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    super.paint(g)
    paintLaser(g, new Position(0,0), new Position(5,5),new java.awt.Color(255,0,255))
    /*g.setColor(new java.awt.Color(255,0,0))
    g.drawLine(center_of_cell(new Position(0,0)).c,center_of_cell(new Position(0,0)).l,center_of_cell(new Position(5,5)).c,center_of_cell(new Position(5,5)).l)*/
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

  def center_of_cell(pos:Position):Position = {
    val cell_size = contents(0).size
    new Position(pos.c * cell_size.height + cell_size.height/2   ,
      pos.l * cell_size.width + cell_size.width/2 
    )
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

