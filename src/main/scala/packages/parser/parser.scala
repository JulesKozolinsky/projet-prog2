package packages
package parser

import entities._
import game._


/** Crée l'objet Parser qui parse du XML en scala */
object Parser
{

  /** Parse le XML */
  def parse (file:String) : List[Round] = {
    var list_of_rounds = List[Round]()

    var m : MonsterType = new Monster1Type
    var r1 = List(new Tuple2(Set(new Tuple2(m,1)),4))
    m = new Monster2Type
    r1  = (new Tuple2(Set(new Tuple2(m,1)),5))::r1
    m = new Monster1Type
    var n : MonsterType = new Monster2Type
    r1 = (new Tuple2(Set(new Tuple2(m,2),new Tuple2(n,1)),10))::r1

    m = new Monster1Type
    var r2 = List(new Tuple2(Set(new Tuple2(m,1)),4))
    m = new Monster2Type
    r2 = (new Tuple2(Set(new Tuple2(m,1)),5))::r2
    m = new Monster2Type
    r2 = (new Tuple2(Set(new Tuple2(m,1)),8))::r2
    m = new Monster1Type
    r2 = (new Tuple2(Set(new Tuple2(m,1)),7))::r2
    m = new Monster2Type
    r2 = (new Tuple2(Set(new Tuple2(m,1)),12))::r2

    list_of_rounds = List(new Round(r2.reverse),new Round(r1.reverse))


    //var file_level = scala.xml.XML.loadFile(getClass.getResource(file).toString)
    /*
    // On énumère chaque round et on ajoute chacun à la liste list_of_rounds
    (file_level \ "round").foreach { round =>
      var round_list = List[Tuple2[Set[Tuple2[MonsterType,Int]],Int]]()

      // On énumère chaque monster et on ajoute chacun au bon couple
      (round \ "monster").foreach { monster =>

        // On initialise date et m avec les attributs du monstre considéré
        val date = (monster \ "@date").text.toInt
        // m représente le MonsterType
        var m =
        // on est obligé de regarde monstre par monster lequel on souhaite ajouter
        if (monster \ "name" == "Monster1") {
          new Monster1Type()
        }
        if (monster \ "name" == "Monster2") {
          new Monster2Type()
        }

        // on regarde la date du monster pour l'ajouter au bon endroit dans la liste
        // found_date représente si un couple contenant la date est présent ou non dans la liste
        var founded_date = false
        // on parcourt round_list
        var round_list_rest = round_list
        round_list = List[Tuple2[Set[Tuple2[MonsterType,Int]],Int]]()
        while (!round_list_rest.isEmpty) {
          // x est un couple x._1 est un set, x._2 la date associée
          var x = round_list_rest.head
          // si on trouve un couple où la date est celle du monstre, alors on le rajoute au set déjà existant
          if (x._2 ==  date ) {
            // on a trouvé un couple où la date est la même que celle du monstre,
            // on regarde alors le MonsterType pour l'ajouter au bon endroit dans le set
            founded_date = true
            //found_monster représente si un couple contenant le MonsterType est présent ou non dans le set
            var founded_monster = false
            // On parcout le set
            var monster_set_rest = x._1
            var monster_set = Set[Tuple2[MonsterType,Int]]()
            while (!monster_set_rest.isEmpty) {
            //for (y <- x._1)
              var y = monster_set_rest.head
              if ( (y._1).toString == ((monster \ "name")+"Type()") ) {
                founded_monster = true
                y = new Tuple2(y._1,y._2 + 1)
              }
              monster_set_rest = monster_set_rest.tail
              monster_set = monster_set.+(y)
            }
            // si on a vu le monstre dans le set, alors le nouveau set correspond à monster_set
            if (founded_monster) {
              x = new Tuple2(monster_set,date)
            }
            // sinon on ajoute au set un couple contenant le MonsterType
            else {
              x = new Tuple2(x._1 + (new Tuple2(m match {case x:MonsterType => x
                case _ => throw new ClassCastException },1)), date)
            }
          }
          round_list_rest = round_list_rest.tail
          round_list = x::round_list
        }

        // La date n'est pas encore présente dans la liste, on la rajoute donc
        if (!founded_date) {
          round_list = (new Tuple2(Set(new Tuple2(m match {case x:MonsterType => x
          case _ => throw new ClassCastException },1)),date))::round_list

        }
      }
      // on cree notre Round
      val r = new Round(round_list.reverse)
      // on ajoute notre round à la liste des rounds
      list_of_rounds = r::list_of_rounds
    } */
    // retourne la liste des rounds dans le bon ordre
    list_of_rounds.reverse

  }
}
