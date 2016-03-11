package packages
package gui

import entities._
import map._
import game._
import sugar._

import swing._
import scala.swing.event._

import javax.swing._
import java.awt.Dimension
import java.awt.Font

class InfoPanel extends BoxPanel(Orientation.Vertical) {
  border = BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0,0,0,100))
  val textArea = new TextArea("cecie cqjdkcldqskvclqdjsbqclqiufdvliusdhclsduhcliqsduvchlqidu<fhvoqdilufvhquoidfvhoqdfiuvholdqifuvh odfqiuhvoiqdfuhvoqfdiuhvouidqfhv\nvqfdvfvhqbdvjhbkqdfvhbqd,kfbhvkqdfhvbkqjdvbkjdfhvbkqjvbkjbdqkjbqhqdfvkjvbfkjqbvkjqb",1000,1){
    border = BorderFactory.createMatteBorder(5, 5, 20, 5, new Color(0,0,0,0))
    
  editable = false;
  lineWrap = true;
  opaque = false;
  border = BorderFactory.createEmptyBorder();
  }
  val attribute = new Attribute("blub"," Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc diam felis, facilisis ut ligula fringilla, ultrices eleifend ligula. Vivamus in elit dui. Suspendisse convallis nisi quis aliquet aliquet. Morbi non lectus quam. Phasellus aliquet quam nec felis tempus dapibus. Donec lacus quam, tempor a dapibus eget, aliquam in sapien. Curabitur accumsan massa velit, feugiat sodales ligula porttitor at. Sed eros felis, rhoncus semper commodo ut, mattis a ligula. Nulla facilisi. Ut a arcu ac erat sodales malesuada. Ut in est vehicula, ullamcorper eros a, laoreet orci.\nAenean maximus egestas laoreet. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum tincidunt mi facilisis, porta dui ut, scelerisque purus. In hac habitasse platea dictumst ","truc")
  preferredSize = new Dimension(200,preferredSize.height)
  contents += attribute
/*add(textArea, BorderLayout.CENTER);
  contents += new Label {
    text = "cecie cqjdkcldqskvclqdjsbqclqiufdvliusdhclsduhcliqsduvchlqidu<fhvoqdilufvhquoidfvhoqdfiuvholdqifuvh odfqiuhvoiqdfuhvoqfdiuhvouidqfhv\nvqfdvfvhqbdvjhbkqdfvhbqd,kfbhvkqdfhvbkqjdvbkjdfhvbkqjvbkjbdqkjbqhqdfvkjvbfkjqbvkjqb"
  }*/

}

class Attribute(title : String, description : String, icon_file : String) extends BoxPanel(Orientation.Vertical)
{
  val font_title = new Font("Courier New", Font.BOLD, 13)
  val font_descr = new Font("Courier New", Font.ITALIC, 12)
  val title_label = new Label (title) {
    font = font_title
  }
  val descr_label = new TextArea(description, 10,10){
    border = BorderFactory.createMatteBorder(5, 5, 20, 5, new Color(0,0,0,200))
    editable = false;
    lineWrap = true;
    opaque = false;
    font = font_descr
  }
  contents += title_label
  contents += descr_label
}
