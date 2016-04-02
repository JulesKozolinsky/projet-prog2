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

  //on remplit la grille de Labels afin de laisser swing calculer la taille de cases
  for(i<-0 to rows - 1) {//rows et columns sont héritées de GridPanel
    for(j<-0 to columns - 1) {
      contents += new Label
    }
  }


    /** Renvoie la position en pixels du centre d'une cellule
    * 
    * @param pos Position de la cellule considérée (mais cette fois pas en pixel)
    */
  def center_of_cell(pos:Position):Position = {
    //taille en pixels d'une cellule de la grille
    val cell_size = contents(0).size
    new Position(pos.l * cell_size.height + cell_size.height/2 ,
      pos.c * cell_size.width + cell_size.width/2
    )
  }

  /** Permet de peindre un laser allant d'une case vers une autre*/
  private def paintLaser(g: Graphics2D, cell1 : Position , cell2 :Position, color : java.awt.Color )
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
    for(s <- current_level.get_shots){
      val col = (Map.get_tower(s._1).tower_type.color)
      paintLaser(g,s._1,s._2, new java.awt.Color(col._1, col._2, col._3))
    }
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
          contents(l*columns + c) = new MonsterCell(monsters,pos)
        }
      }}
    repaint
  }
}


/** Classe mère des cellules de la grille principale */
class Cell(pos_a:Position) extends Button("")
{
  /** Position de la case au sein de la grille principale*/
  val pos = pos_a

  rolloverEnabled = false //évite une actualisation du bouton au moment où la souris passe par dessus

  override def paint(g:Graphics2D){
    g.setColor(new java.awt.Color(100,100,100))
    g.drawRect (0, 0, size.width-1, size.height-1); 
  }

  def click_action(){
    InfoPanel.change_main_unit_cell(pos)
  }
}

/** Cellule de la grille contenant un bouton permettant d'ajouter des tours 
  * 
  * Par défaut, la cellule ne contient pas de tour et on ne sait pas encore quel type de tour elle va contenir
  */
class TowerCell(pos:Position) extends Cell(pos)
{
  /** Vaut true si une tour est construite sur la case, false sinon */
  var is_tower = false
  /** Type de la tour construite sur la case à condition que is_tower vaille true. 
    * 
    * Si is_tower vaut false, le type de tour n'a aucun sens
    */
  var tower_type:TowerType = Tower1Type

  /** Permet de construire une tour sur la case.
    * 
    * Cette méthode est appelée par la fonction actualize de gamegrid
    */
  def build_tower(t:TowerType){
    tower_type = t
    is_tower = true
  }

  override def click_action(){
    
    if(!current_level.create_new_tower(current_tower_type,pos))
    {
      super.click_action
      if(current_level.in_a_round)
        Log("Vous ne pouvez pas placer de tour pendant un round.")
      else{
        if(Map.is_tower(pos))
          Log("Vous ne pouvez pas placer de tour ici : une tour se trouve déjà sur cette case.")
        else {
          if(current_tower_type.price > game.money)
            Log("Vous n'avez pas assez d'argent pour construire cette tour.")
          else
            Log("Vous ne pouvez pas placer de tour ici.")
        }
      }
    }else
    {
      Log("Vous avez créé la tour <i>" + current_tower_type.name + "</i> à la position (ligne : " + (pos.l + 1) +", colonne : " + (pos.c + 1) +").")
    }
    MainFrameGUI.actualize()
  }


  action = new Action(""){
    //lorsqu'on clique sur le bouton, une tour est crée si level l'autorise
    def apply(){
      click_action
    }
  }


  override def paint(g:Graphics2D){
    super.paint(g)
    if(is_tower)
      g.drawImage(tower_skins(tower_type)(size), null,0,0)
  }
}




/** Cellule de la grille contenant autant de monstres qu'on le souhaite
  * 
  * @param pos Position de la cellule dans la grille
  */
class MonsterCell(wave : Set[Tuple2[MonsterType,Int]], pos : Position) extends Cell (pos)
{
  /**Définit la racine carrée du nombre maximal de monstres par case*/
  var scale = 3//Math.sqrt(wave.size).ceil.toInt
  val monsters = wave

  override def click_action(){
    super.click_action
    Log("Vous avez cliqué sur un monstre.")
    MainFrameGUI.actualize()
  }
  
  action = new Action(""){
    //lorsqu'on clique sur une case avec des monstres
    def apply(){
      click_action
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

