object MyProgram {
	def parcourir(v:Vehicule,d:Int) = {
		while(v.compteur < d) v.bouger
	}

	val f = Add(Const(1),Mul(Const(2),Abs(Const(-3))))

	def evalue(f:Aexpr):Int = { f match { 
		case Const(n) => n 
		case Add(g,d) => evalue(g) + evalue(d) 
		case Mul(g,d) => evalue(g) * evalue(d) 
		case Abs(g) => Math.abs(evalue(g))
		}
	}

	def derive(f:Aexpr):Aexpr = { f match { 
		case Const(n) => Const(0)
		case X() => Const(1)
		case Add(g,d) => Add(derive(g), derive(d))
		case Mul(g,d) => Add(Mul(g,derive(d)),Mul(derive(g),d))
		case Abs(g) => Const(-5) 
		}
	} 
		

	def main(args:Array[String]):Unit = {
		println("Hello!")
		val v = new VeloRoute("Moulinette")
		val w = new VeloRouille("Marinette")
//		parcourir(v,15)
//		v.freiner
//		parcourir(w,10)
		w.freiner
<<<<<<< HEAD
		A.print()
		A.incr()
		A.print()
		/* println(evalue(f)) */
		
		val a = Array[Int](0)
		val b = Array[Int](0)
		val c = 2
		
		var f=a 
		var g=b 
		println(f==g)		
		println(g)
		
=======
		val a = Array(5,2,8,1)
		println(a)
		wa.exists(_ >= 7)
		a.find(_ >= 7)
		a.count(_ <= 7)
		a.foldLeft(0)((a,b) => a+b)


>>>>>>> 30dc92565fe1bab82e8b4fdd4ad07fd2123220d5
	}
}

class Velo (nom:String) extends Vehicule {
	def bouger {
		compteur += 1
		println(nom+ ": *roule*")
	}
	override def freiner { println(nom+ "j'arrête")}
}

class VeloRoute(nom:String) extends Velo(nom) {
	override def bouger {
		compteur += 1.5
		println(nom+ ": *roule*")
	}
}

class VeloRouille(nom:String) extends Velo(nom) {
	override def freiner { println(nom+ ": eek")}
}

abstract class Vehicule {
	var compteur:Double = 0
	def bouger : Unit
	def freiner : Unit
}

object A {
	var n=0
	def incr () { n = n+1 }
	def print () { println(n) }
}

abstract class Aexpr
case class Const(n:Int) extends Aexpr
case class X extends Aexpr
case class Add(g:Aexpr,d:Aexpr) extends Aexpr
case class Mul(g:Aexpr,d:Aexpr) extends Aexpr
case class Abs(f:Aexpr) extends Aexpr

