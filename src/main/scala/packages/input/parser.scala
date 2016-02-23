package packages
package parser

import entities._
import game._


/** Crée l'objet Parser qui parse du XML en scala */
object Parser
{
<<<<<<< HEAD
  /** Parse le XML */
=======
  /**Parse le XML */
>>>>>>> 8cc147550c364449bd3d410b98fc792548aba6f7
  def parse (file:String) : List[Round] = {
    var list_of_rounds = List[Round]()
    var file_level = scala.xml.XML.loadFile(file + ".xml")

    // On énumère chaque round et on ajoute chacun à la liste list
    (file_level \ "round").foreach { round =>
      var round_list = List[Tuple2[scala.collection.mutable.Set[Tuple2[Any,Int]],Int]]()

      //Pour chaque round, on regarde tous les monstres
      (round \ "monster").foreach { monster =>
        val date = (monster \ "@date").text.toInt
        var m =
        // on est obligé de regarde monstre par monster lequel on souhaite ajouter
        if (monster \ "name" == "Monster1") {
          new Monster1()
        }
        if (monster \ "name" == "Monster2") {
          new Monster2()
        }
        // on regarde la date du monster pour l'ajouter dans la liste
        var founded_date = false
        // on parcourt round_list
        //var round_list_rest = round_list TODO
        //while (!round_list_rest.isEmpty) {
        for (x <- round_list) {
          //var x =
          if (x._2 ==  date )
          {
            founded_date = true
            var founded_monster = false
            // On parcourt alors le set
            for (y <- x._1) {
              if ( (y._1).toString == ((monster \ "name")+"()") ) {
                founded_monster = true
              //  y._2 = y._2 + 1
              }
            }
            if (!founded_monster) {
            //  x._1 = x._1 + (new Tuple2(m,1))
            }
          }
        }
        // La date n'est pas encore présente dans la liste, on la rajoute donc

        if (!founded_date) {
          round_list = (new Tuple2(scala.collection.mutable.Set(new Tuple2(m,1)),date))::round_list

          //round_list = (new Tuple2(scala.collection.mutable.Set(new Tuple2(m match {case x:Monster => x
          //  case _ => throw new ClassCastException},0)),date))::round_list

        }
      }
      // on cree notre Round
      val r = new Round(round_list)
      // on ajoute notre round à la liste des rounds
      list_of_rounds = r::list_of_rounds
    }
    // retourne la liste des rounds dans le bon ordre
    list_of_rounds.reverse

  }
}
//(MonsterType*INT set)*int list
//List[Tuple2[Set[Tuple2[Monster,Int]],Int]]
//List[Round]()
