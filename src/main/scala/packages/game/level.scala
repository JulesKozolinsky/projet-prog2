package packages
package game

import sugar._
import entities._
import map._


/** Crée un nouveau niveau
  * 
  * Un niveau comporte plusieurs vagues de monstres appelées ici des rounds.
  * Déroulement d'un niveau : les rounds se succèdent. Entre chaque rounds, le joueur à la possibilité de créer de nouvelles tours dans la limite de l'argent qu'il possède
  * @param file Fichier JSon qui contient les informations nécessaires à la création du niveau.
  */

class Level(file:String)
{
  /** Argent du joueur */
  var money = 100

  /** Nombre de vies restantes */
  var life = 10

  /** L'identifiant du round dans lequel on se trouve.*/
  var current_round = 0

  /** Array contenant le rounds qui composent le niveau.*/
  val rounds = Array[Round](new Round(""))

  /** Permet de charger depuis un fichier JSon l'ensemble des Rounds dont on a besoin
    * 
    * @param Fichier JSon
    */
  def load_rounds(file:String) = {}

  /** Démarre le round courrant
    * 
    * Cette fonction incrémente current_round afin d'éviter de démarrer plusieurs fois le même round
    */
  def start_round() = {}


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
