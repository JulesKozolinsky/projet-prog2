import GAME._
import GUI._
import swing._

/** Main Application : 
  * 
  * La méthode main est héritée de la classe SimpleSwingApplication.
  * Ici la définition de top sert de fonction main. 
  */
object MainApp extends SimpleSwingApplication{
  def top = new MainFrameGUI
}
