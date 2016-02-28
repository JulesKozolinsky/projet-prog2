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

  /** L'identifiant du round dans lequel on se trouve.*/
  var current_round = 0

  /** Array contenant le rounds qui composent le niveau.*/
  var rounds = List[Round] ()

  /** Permet de charger depuis le fichier XML l'ensemble des Rounds dont on a besoin, à l'aide du parser */
  def load_rounds() : Unit = {
    rounds = Parser.parse(file)
  }

  /** Démarre le round courrant
    *
    * Cette fonction incrémente current_round afin d'éviter de démarrer plusieurs fois le même round
    */
  def start_round():Round = {
    current_round = current_round + 1
    var r = rounds.head
    rounds = rounds.tail
    r
  }


  /** Crée une nouvelle tour
    *
    * Renvoie un booléen, le même que Map.new_tower
    * Devra prendre en paramètre un TowerType et une position
    */
  def create_new_tower(t:TowerType,p:Position) : Boolean = {
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
  }
}
