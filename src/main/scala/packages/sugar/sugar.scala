package packages

package sugar
import entities._

/** Cette classe doit être utilisée à chaque fois que l'on fait référence à une entité sur la grille
  *
  * @param l Numéro de la ligne
  * @param c Numéro de la colonne
  */


class Position_Real (l_arg : Double , c_arg : Double)
{
  var l = l_arg
  var c = c_arg
  def scalar_prod (pos2:Position_Real) : Double = {(l * pos2.l) + (c * pos2.c)}
  def get_center = {new Position_Real (l+0.5,c+0.5)}
  def to_vect (pos2:Position_Real) : Position_Real = {new Position_Real (pos2.l - l , pos2.c - c)}
}

class Position (l_arg:Int, c_arg:Int)
{
  var l = l_arg
  var c = c_arg
  override def equals(pos2:Any) : Boolean = {
    pos2 match {
    case pos2:Position => this.l == pos2.l && this.c == pos2.c
    case _ => throw new ClassCastException

  }
  }

  override def toString() : String = {
    "(l=" + l.toString + ", c="+c.toString+")"
  }
  /** Produit scalaire entre this et l'argument, vus comme des vecteurs */
  def scalar_prod (pos2:Position) : Int = {(l * pos2.l) + (c * pos2.c)}

  /** Construit le vecteur this -> argument */
  def to_vect (pos2:Position) : Position = {new Position (pos2.l - l , pos2.c - c)}

  /** Construit un vecteur orthogonal au vecteur this (par (-b,a)) */
  def to_normal_vect () : Position = {new Position (-c,l)}

  /** Construit le point aux coordonées réelles, identique à this en valeur */
  def to_Position_Real () : Position_Real = {new Position_Real (l.toFloat,c.toFloat)}

  /* Renvoie la norme de this vu comme un vecteur */
  def norme () : Int = {c*c+l*l}

  /** Cette fonction renvoie true si le point se situe dans la direction du tir de la tour, false si derrière */
  def is_well_directed (pos_tower:Position,pos_first:Position) : Boolean = 
  {
    var tower_target = pos_tower.to_vect(pos_first)
    var tower_self = pos_tower.to_vect(this)
    tower_target.scalar_prod(tower_self) > 0
  }

  /** Cette fonction renvoie true si le point se trouve à gauche de la demi-droite, et false si à droite */
  def is_on_left (pos_tower:Position,pos_first:Position) : Boolean = 
  {
    var dir_vect = pos_tower.to_vect(pos_first)
    var normal_vect = dir_vect.to_normal_vect.to_Position_Real    
    val tower_self = pos_tower.to_Position_Real.get_center.to_vect(this.to_Position_Real)
    normal_vect.scalar_prod(tower_self) >= 0
  }


  /** Cette fonction sert à trouver les monstres sur la trajectoire d'un tir de tour */
  def is_on_ray (pos_tower:Position,pos_first:Position) : Boolean = 
  {
    var up_left_corner = this
    var up_right_corner = new Position (l,c+1)
    var down_left_corner = new Position (l+1,c)
    var down_right_corner = new Position (l+1,c+1)
    
    (up_left_corner.is_on_left(pos_tower,pos_first) != down_right_corner.is_on_left(pos_tower,pos_first) || up_right_corner.is_on_left(pos_tower,pos_first) != down_left_corner.is_on_left(pos_tower,pos_first)) && (this.is_well_directed(pos_tower,pos_first))
  }

}


class Targets ()
{
  var itself = List[(Position,List[Monster])] ()

  def add_position (p:Position) : Unit = 
  {
    var already_exists = false
    itself.foreach {(x:(Position,List[Monster])) => already_exists = already_exists || (p.equals (x._1) )}
    if (! already_exists) {itself = new Tuple2(p,List[Monster]())::itself}
  }

  def add_monster (p:Position,m:Monster) : Unit = 
  {
    var Some(pos) = itself.find {(x:(Position,List[Monster])) => (x._1 == p)}
    itself = itself.filter {(x:(Position,List[Monster])) => !(x==pos)}
    itself = new Tuple2(pos._1,m::pos._2)::itself
  }

  def empty_target () : Boolean = 
  {
    var found_targets = false
    itself.foreach {(x:(Position,List[Monster])) => found_targets = found_targets || (! x._2.isEmpty) }
    !found_targets
  }

  def simple () : Unit = 
  {
    var new_itself = List[(Position,List[Monster])] ()
    itself.foreach {(x:(Position,List[Monster])) => if (!x._2.isEmpty) {new_itself = x::new_itself}}
    itself = new_itself    
  }

}
