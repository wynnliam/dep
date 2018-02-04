//Liam Wynn, 5/14/2017, dep

import java.io._
import scala.io._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object Main
{
	//We will use this to store all the dependencies.
	//The key is the target, and the value is the list of
	//dependencies.
	var targets = Map[String, ListBuffer[String]]()

	//Stores the current target for when we want to add dependencies
	//to.
	var currTarget:String = null

	def main(args: Array[String]): Unit =
	{
		if(args.length < 2)
		{
			println("Insufficient number of arguments")
			return
		}

		parseFile(args(0))
		printDependencyTree(args(1))
	}

	def isBlankLine(line: String): Boolean =
	{
		val whiteSpace:Array[Int] = Array(9, 10, 11, 12, 13)
		var intVal = 0

		for(c <- line)
		{
			intVal = c.toInt

			if(!whiteSpace.contains(intVal) && intVal != 32)
				return false
		}

		true
	}

	def isComment(line: String): Boolean =
	{
		line(0) == '#'
	}

	//Returns if the line is an include line. According to the
	//grammar, we may ignore any white space that may come before
	//the 'include' portion
	def isInclude(line:String): Boolean =
	{
		line.substring(0, 7).equals("include")
	}

	//We will assume that an include line also contains a
	//valid path following the 'include', as the grammar
	//requires that.
	def getPathFromInclude(line:String): String =
	{
		var i:Int = 7
		val whiteSpace:Array[Int] = Array(9, 10, 11, 12, 13, 32)

		while(whiteSpace.contains(line(i).toInt) && i < line.length)
			i += 1

		//The first and last characters are "
		return line.substring(i + 1, line.length - 1)
	}

	//Returns if a given line is a target line. According to the
	//grammar, we may ignore any white psace that could come before
	//the 'target' portion.
	def isTarget(line:String): Boolean =
	{
		line.substring(0, 6).equals("target")
	}

	//Returns the target from a target line. We will assume the line is valid.
	def parseTarget(line:String): String =
	{
		line.substring(7)
	}

	//Returns if a given line is a dependency line.
	def isDependency(line:String): Boolean =
	{
		line.substring(0, 10).equals("depends on")
	}

	def parseDependency(line:String): String = line.substring(11)

	def parseFile(filePath: String): Int = 
	{
		if(filePath == null)
			return 0

		val file = Source.fromFile(filePath)
		val fileString = file.mkString
		val lines = fileString.split("\n")

		for(line <- lines)
		{
			if(line.length > 7 && isInclude(line))
				parseFile("./" + getPathFromInclude(line))

			if(line.length > 6 && isTarget(line))
			{
				currTarget = parseTarget(line)
				targets += (currTarget -> new ListBuffer[String]())
			}

			if(line.length > 10 && isDependency(line))
				targets(currTarget) += parseDependency(line)
		}

		file.close()

		1
	}

	def printDependencyTree(toPrint:String): Int = 
	{
		if(targets.contains(toPrint) == false)
			return 0

		print(toPrint)

		for(dep <- targets(toPrint))
		{
			print('(')
			printDependencyTree(dep)
			print(')')
		}

		1
	}
}

