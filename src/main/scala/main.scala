import packages._
import packages.gui._
import packages.game._
import packages.sugar._
import packages.entities._
import packages.map._
import swing._

/** Main Application :
  *
  * La méthode main est héritée de la classe SimpleSwingApplication.
  * Ici la définition de top sert de fonction main.
  */
object MainApp extends SimpleSwingApplication{
  def top = MainFrameGUI
}
