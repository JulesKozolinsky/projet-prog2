package packages

package sugar
import entities._

/** Cette classe doit être utilisée à chaque fois que l'on fait référence à une entité sur la grille
  *
  * @param l Numéro de la ligne
  * @param c Numéro de la colonne
  */

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
}


class Vector (point1:Position,point2:Position)
{
  def norme : Int = math.sqrt((point1.l - point2.l)*(point1.l - point2.l) + (point1.c - point2.c)*(point1.c - point2.c)).toInt
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
