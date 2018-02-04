import java.io._
import scala.io._

object Main
{
	def main(args: Array[String]): Unit =
	{
		//How to write to a file
		val writer = new PrintWriter(new File("test.txt"))

		writer.write("This is a test.")
		writer.close()

		//How to read from the command line
		print("Please enter your input: ")
		val line = StdIn.readLine()

		println("You typed: " + line)

		//How to read from a file
		println("Reading from a file demo:")

		Source.fromFile("Demo.txt").foreach {
			print
		}
	}
}
