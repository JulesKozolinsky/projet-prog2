package packages
package game

import sugar._
import entities._
import map._
import parser._


/** Crée un nouveau niveau
  *
  * Un niveau comporte plusieurs vagues de monstres appelées ici des rounds.
  * Déroulement d'un niveau : les rounds se succèdent. Entre chaque rounds, le joueur à la possibilité de créer de nouvelles tours dans la limite de l'argent qu'il possède
  * @param file Fichier XML qui contient les informations nécessaires à la création du niveau.
  */

class Level(file:String)
{

  /** si has_win vaut vrai, alors le joueur a gagné */
  var has_won : Boolean = false

  /** si has_lost vaut vrai, alors le joueur a perdu */
  var has_lost : Boolean = false

  /** Booléen indiquant si on se trouve en cours de round ou non */
  var in_a_round = false

  /** compteur indiquant le numéro du round dans lequel on se trouve */
  var round_counter = 1

  /** Array contenant les rounds qui composent le niveau.*/
  private var rounds : List[Round] = Parser.parse(file)

  Map.initialize

  /** Démarre le round courrant
    *
    * Cette fonction incrémente current_round afin d'éviter de démarrer plusieurs fois le même round
    */
  def start_round():Unit =
  {
    if (!rounds.isEmpty) {in_a_round = true }
    else {throw new Exception ("There are no more rounds")}
  }

  /** Arrête le round courant
    * Cette fonction met la variable in_a_round à faux
    */
  private def stop_round():Unit =
  {
    if (!rounds.isEmpty)
    {
      in_a_round = false
      rounds = rounds.tail
      round_counter = round_counter + 1
    }
    else  {throw new Exception ("Round should be started before stopped")}
  }


  /** Crée une nouvelle tour
    *
    * Renvoie un booléen, le même que Map.new_tower
    * Devra prendre en paramètre un TowerType et une position
    */
  def create_new_tower(t:TowerType,p:Position) : Boolean =
  {
    if (in_a_round) {false}
    else
    {
      var cost = t.price
      if (cost > money) {false}
      else
      {
        if (Map.new_tower(t,p))
        {
          money = money - cost
          true
        }
        else {false}
      }
    }
  }

  def actualize_unlocked(n:Int) : Unit =
  {
    all_towers.foreach {(t:TowerType) => if (t.round_to_unlock == round_counter) {unlocked_towers = t::unlocked_towers}  }
  }


  /** Transmet à level l'état du round en cours
    *
    * Doit être lancée à chaque tick
    * @return faux quand le round est terminé pour indiquer à la gui qu'elle a un timer à stopper
    */
  def actualize () : Boolean =
  {
    if (! in_a_round) {true}
    else
    {
      if (! (rounds.head).actualize) {true}
      else
      {
        stop_round()
        if (life == 0) {has_lost = true}
        else
        {
          if (rounds.isEmpty) {has_won = true}
          else {actualize_unlocked(round_counter)}   //le round est terminé, mais il reste des rounds à jouer : on actualise les tours disponibles pour la suite
        }
        false
      }
    }
  }




}
