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
  val height : Int = 9

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
  var path : List[Position] = {List[Position]()}

  /** là où sont stockées les images associées aux tours*/
  var tower_resources : Array[String] = {Array("tower1.png","tower2.png")}

  /** Calcule le chemin à partir d'une position jusqu'à la fin (appelé au début du round) */
  private def compute_path () : List[Position] = {
      var path = List[Position]()
      var c = 0;
      for( c <- 0 to (width-1)) {
        path = (new Position (height/2,c))::path
      }
      path.reverse
     }

  /** Calcule le chemin avec les tours existantes à l'aide d'un algorithme de plus court chemin,
    * renvoie la liste des positions et renvoie la liste vide dans le cas où aucune solution n'a
    * été trouvée
    */
  def compute_path2 : Unit = {
    // la liste résultat
    var res = List[Position]()
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
    // calcule le plus court chemin avec dijkstra
    val start = m(height/2)(0)
    val target = m(height/2)(width-1)
    val path_dijkstra = Dijkstra_algo.compute_dijkstra(g,start,target)
    //java.util.
    //Calcule les positions à partir des noeuds
    path_dijkstra.foreach { (n : WeightedGraph#Node) => res = (n.pos)::res }

    path = res
  }


  /** Donne la case suivante d'un monster à partir du chemin*/
  def next_case (p:Position) : Position = {
    if (p.c == width-1) {
      throw new Exception("monster will go off the grid")}
    else {
      var temp = path
      while (!(temp.isEmpty) && !(temp.head == p)) {
        temp = temp.tail
      }
      if (temp.head == p) {
        (temp.tail).head
      }
      else {throw new Exception("next_case non founded")}
    }
  }


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
          try { compute_path2
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

    /** renvoie un tableau de monsters susceptibles d'être touchés par une tour donnée*/
    def get_targets (tower:Tower) : List[Monster] = {
      // On calcule les cases à portée de la tour
      var distance_of_case = List[Position]()
      for ( l <- 0 to (height-1) ) {
        for ( c <- 0 to (width-1) ) {
            val coord = new Position(l,c)
            val v = new Vector(tower.pos,coord)
            if (v.norme <= (tower.tower_type).range)
            {distance_of_case = coord::distance_of_case}
          }
        }
        // Pour chaque case, on ajoute les monstres considérés à la liste
        var answer = List[Monster]()
        for ( x <- distance_of_case ) {
          answer = ((monsters(x.l)(x.c)).toList):::answer
        }
        answer
  }


    /** Supprime le monstre monster de la 1ere position et l'ajoute sur la deuxieme */
    def move_monster (monster:Monster,p1:Position,p2:Position) : Unit = {
      remove_monster (monster,p1)
      add_monster (monster,p2)
    }

    /** supprime le monstre à la position p de la carte monsters */
    def remove_monster (monster:Monster,p:Position) : Unit = {
      if ( !( (monsters(p.l)(p.c)).remove(monster) ))
      {throw new Exception("monster is not in this box")}
    }

    /** ajoute un monstre à la position p de la carte monsters */
    def add_monster (monster:Monster,p:Position) : Unit = {
      if ( !( (monsters(p.l)(p.c)).add(monster) ))
      {throw new Exception("monster is already in this box")}
    }


    /** Renvoie l'ensemble des monsters situés à la position (l,c) */
    def get_monsters (p:Position) : Set[Tuple2[MonsterType,Int]] = {
      // On parcourt le set "map_set"
      var map_set = monsters(p.l)(p.c) //Set[Monster]
      var monster_set = Set[Tuple2[MonsterType,Int]]()
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

    /** Ajoute un monster sur la carte en position (height/2,0) */
    def new_monster (m:Monster) : Unit = {
      monsters(height/2)(0) = (monsters(height/2)(0)).+(m)
    }

    /** Réinitialise les cartes */
    def initialize():Unit = {
      ground = initialize_matrix_ground(height,width)
      towers = initialize_matrix_towers(height,width)
      monsters = initialize_matrix_monsters(height,width)
    }
}
