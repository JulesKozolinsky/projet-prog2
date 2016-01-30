package packages

package game


/** Permet de créer un Timer
  * 
  * Usage : Timer(100){println("hey")} crée un timer qui affichera "hey" toutes les 100 millisecondes.
  */
object Timer {
  /** 
    * 
    * @param interval Interval entre les ticks
    * @param repeats true si le Timer doit se répéter à l'infini, false sinon. La valeur par défaut est true
    * @param op La fonction à appliquer à chaque tick
    */
  def apply(interval: Int, repeats: Boolean = true)(op: => Unit) {
    val timeOut = new javax.swing.AbstractAction() {
      def actionPerformed(e : java.awt.event.ActionEvent) = op
    }
    /** Timer de java*/
    val t = new javax.swing.Timer(interval, timeOut)
    t.setRepeats(repeats)
    t.start()
  }
}



/** Gestion d'un round (vague d'attaquant).
  * 
  * Pendant le round, le joueur ne peut pas ajouter de tour.
  * Il regarde les tours défendre le passage contre une vague de monstres
  * @param file Chaîne de caractères définissant le nom du fichier Json correspondant à la vague de monstres que l'on veut créer
  */
class Round(file:String){

  /** Nombre de vies perdues pendant le round */
  var lives_lost = 0

  /** Ensemble des monstres en vie */
  var monsters = Set()

  /** Actualise l'état de l'objet. 
    * 
    *  Cette méthode doit être appelée à chaque tick
    */
  def actualize (): Unit = {}

  /** Permet de savoir quand le round est terminé
    * 
    *  @return true si le round est fini, false sinon.
    */
  def is_finished () : Boolean = {true}
}
