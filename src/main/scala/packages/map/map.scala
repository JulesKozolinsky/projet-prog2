package packages
package map

import entities._
import sugar._
import graph._
import dijkstra._

/** Crée l'object Map, qui gère le déplacement des monstres */
object Map
{
  /** hauteur de la carte */
  val height : Int = 10

  /** largeur de la carte */
  val width : Int = 20

  /** la carte ground représente les obstacles, i.e. ce qui ne bouge a priori pas */
  private var ground = initialize_matrix_ground(height,width)

  /** la carte towers représente les tours (une tour ou liste vide)*/
  private var towers = initialize_matrix_towers(height,width)

  /** la carte monsters représente les montres, il peut y en avoir plusieurs sur une même case,
    * d'où l'ensemble (Set) de monstres par case
    */
  private var monsters = initialize_matrix_monsters(height,width)

  /** le chemin où les monstrers peuvent se déplacer */
  var path : Array[Array[List[Position]]] = initialize_path(height)

  /** Réinitialise les cartes */
  def initialize():Unit = {
    ground = initialize_matrix_ground(height,width)
    towers = initialize_matrix_towers(height,width)
    monsters = initialize_matrix_monsters(height,width)
    compute_path
  }




/******************* CALCUL DE CHEMIN **************************/

  /** Calcule le chemin avec les tours existantes à l'aide d'un algorithme de plus court chemin */
  def compute_path : Unit = {
    // définie le graphe sur lequel les monstres peuvent bouger
    val g = new WeightedGraph(1)
    // définie une matrice où les noeuds sont stockés
    var m = Array.ofDim[g.Node](height,width)
    for ( l <- 0 to (height-1) ; c <- 0 to (width-1) ) {
      m(l)(c) = g.addNode
      m(l)(c).pos = new Position(l,c)
    }
    // crée les arêtes du graphe
    for ( l <- 0 to (height-1) ; c <- 0 to (width-1) ) {
      if (towers(l)(c).isEmpty) {
        if ( (l != height-1) && (towers(l+1)(c)).isEmpty) {
          (m(l)(c)).connectWith(m(l+1)(c))
        }
        if ( (c != (width-1)) && (towers(l)(c+1)).isEmpty) {
          (m(l)(c)).connectWith(m(l)(c+1))
        }
      }
    }
    // on parcourt les cases de départ
    for (i <- 0 to (height-1)) {

      // distance minimale initiale
      var distance_min = height*width
      // la liste des chemins de plus courts chemin
      var res_list = List[List[Position]]()
      // on parcourt les cases de sorties
      for (j <- 0 to (height-1)) {

        // calcule le plus court chemin avec dijkstra
        val start = m(i)(0)
        val target = m(j)(width-1)
        val (path_dijkstra,distance) = Dijkstra_algo.compute_dijkstra(g,start,target)

        // on regarde si c'est le chemin le plus court jusqu'à présent
        if (distance < distance_min) {
          // la distance minimale est changée
          distance_min = distance
          // liste résultat
          var res = List[Position]()
          //Calcule les positions à partir des noeuds
          path_dijkstra.foreach { (n : WeightedGraph#Node) => res = (n.pos)::res }
          res_list = List(res)
        }
        else {
          if (distance == distance_min) {
            //liste resultat
            var res = List[Position]()
            //Calcule les positions à partir des noeuds
            path_dijkstra.foreach { (n : WeightedGraph#Node) => res = (n.pos)::res }
            // on ajoute un nouveau plus court chemin
            res_list = res::res_list
          }
        }

      }
      // on ajoute le résultat
      path(i) = res_list.toArray
    }
  }


  /** Donne la case suivante d'un monster à partir du chemin*/
  def next_case (p:Position,o:Position,c:Int) : Position = {
    if (p.c == width-1) {
      throw new Exception("monster will go off the grid")}
    else {
      var temp = (path(o.l))(c)
      while (!(temp.isEmpty) && !(temp.head == p)) {
        temp = temp.tail
      }
      if (temp.head == p) {
        (temp.tail).head
      }
      else {throw new Exception("next_case non founded")}
    }
  }







/******************* OPERATIONS SUR LES TOURS **************************/

    /** Renvoie vrai si une tour se trouve sur la case indiquée */
    def is_tower (p:Position) : Boolean = {
      if ( (p.l >= 0) && (p.l <= (height-1)) && (p.c >= 0) && (p.c <= (width-1)) ) {
        !((towers(p.l)(p.c)).isEmpty)
      }
      else {
        throw new IllegalArgumentException("wrong position : out of bounds ")
      }
    }

    /** Renvoie vrai et crée une nouvelle tour si possible, sinon renvoie faux*/
    def new_tower (t:TowerType,p:Position) : Boolean = {
        if ( is_tower (p) )
          {false}
        else {
          var tower = t.get_instance(p)
          towers(p.l)(p.c) = tower::(towers(p.l)(p.c))
          var b = true
          try { compute_path
          } catch {
              case e : NoSuchElementException => {
                remove_tower(p)
                b = false
              }
          }
          b
        }
      }

    /** Supprime une tour de la carte à la position (l,c) */
    def remove_tower (p:Position) : Unit = {
      if (towers(p.l)(p.c).isEmpty) {
        throw new Exception("Position without any tower")
      }
      else {
        towers(p.l)(p.c) = List[Tower]()
      }
    }

    /** Renvoie la tour située à la position (l,c) */
    def get_tower (p:Position) : Tower = {
        if (is_tower (p)) {
          (towers(p.l)(p.c)).head
        }
        else {
          throw new IllegalArgumentException("There is no tower here")
        }
    }

    /** renvoie une liste des cases visibles par la tour avec les monstres à l'intérieur de chaque case (format liste [tuple2]) et dont le premier élément est la position de la tour ; les positions ne comprenant pas de monstres ne sont pas intégrées à la liste (IMPORTANT) */
    def get_targets (tower:Tower) : List[(Position,List[Monster])] = {
      // On calcule les cases à portée de la tour
      var distance_of_case = List[Position]()
      for ( l <- 0 to (height-1) ) {
        for ( c <- 0 to (width-1) ) {
            val coord = new Position(l,c)
            val v = tower.pos.to_vect(coord)
            if (v.norme <= (tower.tower_type).range)
            {distance_of_case = coord::distance_of_case}
          }
        }
        // Pour chaque case, on ajoute les monstres considérés à la liste
        var answer = List[(Position,List[Monster])] ()
        for ( x <- distance_of_case )
        {
	  if (  !monsters(x.l)(x.c).toList.isEmpty  )
	   {
             answer = new Tuple2 ( x , monsters(x.l)(x.c).toList )::answer
	   }
        }
        (new Tuple2 (tower.pos , List[Monster] ()))::answer
  }






/******************* OPERATIONS SUR LES MONSTRES **************************/

    /** Supprime le monstre monster de la 1ere position et l'ajoute sur la deuxieme */
    def move_monster (monster:Monster,p1:Position,p2:Position) : Unit = {
      remove_monster (monster,p1)
      add_monster (monster,p2)
    }

    /** supprime le monstre à la position p de la carte monsters */
    def remove_monster (monster:Monster,p:Position) : Unit = {
      if ( !( monsters(p.l)(p.c) contains monster ))
      {throw new Exception("monster is not in this box")}
      monsters(p.l)(p.c) = monsters(p.l)(p.c) - monster
    }

    /** ajoute un monstre à la position p de la carte monsters */
    def add_monster (monster:Monster,p:Position) : Unit = {
      if ( monsters(p.l)(p.c) contains monster )
      {throw new Exception("monster is already in this box")}
      monsters(p.l)(p.c) = monsters(p.l)(p.c) + monster
    }


    /** Renvoie l'ensemble des type de monstres situés à la position (l,c), et la quantité de chaque */
    def get_monsters (p:Position) : Set[Tuple2[MonsterType,Int]] = {
      var map_set = monsters(p.l)(p.c) //Set[Monster]
//version gab
      var monster_set = Set[Tuple2[MonsterType,Int]]() // l'ensemble que l'on va remplir
      map_set.foreach {(monster:Monster) => // on parcourt la case
       {
         var m_type = monster.monster_type // le type du monstre en cours
         var monster_type_found = false // à priori pas encore dans notre ensemble
         var old_t = (m_type,0) // valeurs inutiles, sert à transmettre l'information de
         var new_t = (m_type,0) // la ligne suivante à l'extérieur du "monster_set.foreach"
	 // on parcourt notre ensemble en cours de remplissage, si on trouve dedans le type du monstre en cours on stock les modifications à apporter.
         monster_set.foreach {(t:(MonsterType,Int)) => {if (t._1 == m_type) {new_t = (m_type,t._2 + 1) ; old_t = t ; monster_type_found = true} }}
         if (monster_type_found)  {monster_set = (monster_set - old_t) + new_t} else {monster_set = monster_set + (new Tuple2(m_type,1))}
       }
      }
      monster_set
    }
//fin version gab

//ancienne version
/*
      while (!map_set.isEmpty) {
        var m = (map_set.head).monster_type //m est le type du monstre
        // On parcourt le set "monster_set"
        var monster_set_rest = monster_set
        monster_set = Set[Tuple2[MonsterType,Int]]()
        var monster_type_founded = false
        while (!monster_set_rest.isEmpty ) {
          var t = monster_set_rest.head
          var x = t._1
          if (x == m) {
            // le MonsterType est déjà présent dans le set du résultat,
            // on augmente le nombre d'un
            t = new Tuple2(m,t._2 + 1)
            monster_type_founded = true
          }
          monster_set = monster_set.+(t)
          monster_set_rest = monster_set_rest.tail
        }
        // le monsterType n'est pas encore présent dans le set du résultat,
        // on l'ajoute donc
        if (!monster_type_founded) {
          monster_set = monster_set.+(new Tuple2(m,1))
        }
        map_set = map_set.tail
      }
      monster_set
    }
*/

    /** Renvoie l'ensemble des monstres situés à la position (l,c) */
    def get_real_monsters (p:Position) : Set[Monster] = {monsters(p.l)(p.c)}

    /** Ajoute un monster sur la carte en position (height/2,0) */
    def new_monster (m:Monster) : Unit = {
      val p = m.init_pos
      monsters(p.l)(p.c) = (monsters(p.l)(p.c)).+(m)
    }

}
