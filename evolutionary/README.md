Evolutionary Algorithms
=================================

###Genetic Algorithm
+ ia an adaptive strategy and a global optimization technique.

`src/main/scala/genetic`

**sqrt(value) = x** function solution search:

    def oneMin(value: Double): String => Double =
      in => {
        val r = atod(in)               // convert genome to number
        Math.abs(value - (r * r))      // calculate diference
      }

The strategy for the Genetic Algorithm is to repeatedly employ surrogates for the recombination and mutation genetic mechanisms on the population of candidate solutions:

    def search[T](
      crossover:  (T, T) => T,          // - crossover function
      mutation:   T => T,               // - mutation function
      fitness:    T => Double,          // - fitness calculation function
      tournament: List[T] => List[T],   // - tournament function
      maxGens:    Int,                  // - namber of generations
      popSize:    Int                   // - population size
    ): List[T] => T = ...


Crossover function:

    def crossover(rate : => Double): (String, String) => String = ...
    

Mutation function:

    def mutation(rate : => Double): String => String = ...


Fitness function

    def oneMin(value: Double): String => Double = ..



**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.genetic.app'`

###Genetic Programming
+ is an extension of the Genetic Algorithm.
+ inspired by population genetics and evolution at the population level.

`src/main/scala/geneticProgramming`

search for function: **f(x) = 2x² - 3x - 4**

    def targetFunction: Double => Double =
        x => 2 * x * x - 3 * x - 4

algorithm is configured to search for a program with the function set {+, -, *, /}

    // Genetic program operators
    trait Operator
    object Plus extends Operator { override def toString = "+" }
    object Minus extends Operator { override def toString = "-" }
    object Div extends Operator { override def toString = "/" }
    object Mul extends Operator { override def toString = "*" }

and expressin tree defined as:

    trait Program[+T]
    case class Node[T](left : Program[T], right : Program[T], op : Operator) extends Program[T]
    case class Const[T](value: T) extends Program[T]
    case object Var extends Program[Nothing]

implemented type-classes (from **core***) for Programm

    Show[Program[T]]
    Show[Double]
    
and

    Navigable[Program[T]]


Crossover function:

    def crossover[T](
        rate : => Double, 
        maxDepth : => Int
    )(implicit default: Program[T]): (Program[T], Program[T]) => Program[T] = ...

Mutation function:

    def mutation[T](
        rate : => Double,
        maxDepth : => Int,
        random : => Program[T]
    )(implicit default: Program[T]):  Program[T] => Program[T] = ...

Fitness function:

    def segmentError(programMap : => List[(Double, Double)]): Program[Double] => Double =...



**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.geneticProgramming.app'`

###Evolution Strategies
+ inspired by macro-level of evolution (phenotype, hereditary, variation).

`src/main/scala/evolutionStrategies`

**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.evolutionStrategies.app'`

###Grammatical Evolution
`src/main/scala/gramaticalEvolution`

**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.gramaticalEvolution.app'`


###Gene Expression Programming
`src/main/scala/geneExpressionProgramming`

**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.geneExpressionProgramming.app'`



##Core type-classes:

    trait Show[T] { 
        def show: T => String // - show method for any type T
    }


    trait Navigable[T] {
        def length: T => Int            // - get internal structure length of any type T
        def iter:   T => Stream[T]      // - get iterator for internal structure for any T
        def lens:   T => Stream[T => T] // - get iterator of modification functions (lenses) for internal structure of T
        def prune:  T => Int => T       // - prune internal structure on any type T
    }


