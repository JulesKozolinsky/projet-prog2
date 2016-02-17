package packages
package map

import entities._
import sugar._

/** Crée l'object Map, qui gère le déplacement des monstres */
object Map
{
  /** hauteur de la carte */
  val height : Int = 9

  /** largeur de la carte */
  val width : Int = 21

  /** la carte ground représente les obstacles, i.e. ce qui ne bouge a priori pas */
  private var ground = initialize_matrix_ground(height,width)

  /** la carte towers représente les tours (une tour ou liste vide)*/
  private var towers = initialize_matrix_towers(height,width)

  /** la carte monsters représente les montres, il peut y en avoir plusieurs sur une même case,
    * d'où l'ensemble (Set) de monstres par case
    */
  private var monsters = initialize_matrix_monsters(height,width)

  /** le chemin où les monstrers peuvent se déplacer */
  private var path : List[Position] = {compute_path ()}

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
  private def compute_path (p:Position) : List[Position] = {
    List[Position]()
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

  /** Donne la case suivante d'un monster à partir du chemin*/
  def next_case (p:Position) : Position = {
    if (p.c == width-1)
    {throw new Exception("monster will go off the grid")}
    else
    {new Position(p.l,p.c+1)}
  }

  /** renvoie un tableau de monsters susceptibles d'être touchés par une tour donnée*/
  def get_targets (tower:Tower) : List[Monster] = {
    // On calcule les cases à portée de la tour
    var distance_of_case = List[Position]()
    for ( l <- 0 to (height-1) ) {
      for ( c <- 0 to (width-1) ) {
          val coord = new Position(l,c)
          val v = new Vector(tower.pos,coord)
          if (v.norme <= tower.range)
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

  /** Renvoie vrai et crée une nouvelle tour si possible, sinon renvoie faux*/
  def new_tower (t:TowerType,p:Position) : Boolean = {
      if ( (towers(p.l)(p.c)).isEmpty ) {
          if (p.l == height/2) {
            false
          }
          else {
            var tower = t.get_instance(p)
            towers(p.l)(p.c) = tower::(towers(p.l)(p.c))
            true}
       }
      else
        {false}
    }

    /** Renvoie vrai si aucune tour ne se trouve sur la case indiquée */
    def is_tower (p:Position) : Boolean = {
      !(towers(p.l)(p.c)).isEmpty
    }

    /** Renvoie la tour située à la position (l,c) */
    def get_tower (p:Position) : Tower = {
      if (is_tower (p)) {
        towers(p.l)(p.c)(0)
        }
      else {
        throw new IllegalArgumentException("There is no tower here")
        }
    }

}
