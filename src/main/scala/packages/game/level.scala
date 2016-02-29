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

  /** Booléen indiquant si on se trouve en cours de round ou non */
  var in_a_round = false

  /** L'identifiant du round dans lequel on se trouve.*/
  var current_round = 0

  /** Array contenant le rounds qui composent le niveau.*/
  var rounds = Parser.parse(file) //List[Round] ()


  /** Démarre le round courrant
    *
    * Cette fonction incrémente current_round afin d'éviter de démarrer plusieurs fois le même round
    */
  def start_round():Unit = {
    if (!rounds.isEmpty) {
    current_round = current_round + 1
    rounds = rounds.tail
    }
  }

  /**
  */
  def stop_round():Unit = {}


  /** Crée une nouvelle tour
    *
    * Renvoie un booléen, le même que Map.new_tower
    * Devra prendre en paramètre un TowerType et une position
    */
  def create_new_tower(t:TowerType,p:Position) : Boolean = {
    //if (in_a_round) {false}
    //else {
    var tower_try = t.get_instance(new Position(-1,-1))
    var cost = tower_try.price
    if (!(cost > money))
      {
        if (Map.new_tower(t,p))
         {
            money = money - cost
            true
         }
        else {false}
      }
    else {false}
  //}
  }

  /** Transmet à level l'état du round en cours
     *
     * Doit être lancée à chaque tick
     * @return true tant que le  round n'est pas terminé, false quand il est temps de se préparer pour un nouveau round
     */
  def actualize () : Boolean = {
    //in_a_round = true
    if (rounds.isEmpty) {throw new Exception ("You already finished this level")}
    if ((rounds.head).actualize)
      {
      if (life == 0) {throw new Exception("You failed : monsters killed you \n Game Over")}
      else
        {if ((rounds.tails).isEmpty) {throw new Exception ("You killed everyone ! Congratulation !!")}
         else {start_round() ; /*in_a_round = false ; */false}
        }
      }
    else {true}
  }


}
