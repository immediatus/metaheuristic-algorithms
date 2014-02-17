Evolutionary Algorithms
=================================

####Genaral view:

    t := 0;
    initialize(P (t));
    evaluate(P (t));
    while not terminate(P (t)) do
        P (t + 1) := select(P (t));
        recombine(P (t + 1));
        mutate(P (t + 1));
        evaluate(P (t + 1));
        t := t + 1;
    od

+ **Individuals** -  is a sequence of chromosomes, the genome. Size and structure of the genome is determined by the number of chromosomes and their types.

+ **Populations** - represents a collection of individuals where they may be sorted in a population either in ascending or descending order with respect to their fitness.

**Genegic operators:**

+ **Selection** - define a strategy of selection which usually base the chance of selection of particular individuals on their fitness values or their rank in the population, respectively.

   * Extinctiveness
   * Elitism
   * (μ,λ)-Selection
   * (μ+λ)-Selection
   * Stochastic Selection
   * Proportional Selection
   * Ranking Selection
   * Tournament Selection

where:
   **μ** - size of the parent population;
   **λ** - size of the offspring population from which individuals are selected.


+ **Replacement** - a number of candidates are generated from the current population, e. g. by recombination and mutation, and may replace some or all of the parents top produce the next generation.

   * Generational Replacement
   * Steady-State Replacement
   * Recombination
   * Crossover of Strings
   * Discrete Recombination
   * Intermediate Recombination
   * Panmictic Recombination

+ **Mutation** - define a mutation stategy for GAs & ESs

   * Discrete Mutations
   * Continuous Mutations
   * Inversion

+ **Self-adaptation in Evolution Strategies**

   * Standard adaptation
   * Rotation matrix adaptation

The strategy for the Genetic Algorithm is to repeatedly employ surrogates for the recombination and mutation genetic mechanisms on the population of candidate solutions:
Strategy implemented in `src/main/scala/core/genetic.scala`:

    def search[T](
      replacement: (T, T) => T,          // - replacement function
      mutation:    T => T,               // - mutation function
      fitness:     T => Double,          // - fitness calculation function
      selection:   List[T] => List[T],   // - selection function
      maxGens:     Int,                  // - namber of generations
      popSize:     Int                   // - population size
    ): List[T] => T = ...



###Genetic Algorithm
+ is an adaptive strategy and a global optimization technique.
+ Details: [wiki](http://en.wikipedia.org/wiki/Genetic_algorithm "wikipedia")
+ Sources: `src/main/scala/genetic`
+ Run: `sbt 'evolutionary/run-main ua.org.scala.genetic.app'`


**sqrt(value) = x** function solution search:

    def oneMin(value: Double): Individual => Double =
      in => { // for sqrt function
        val r = itod(in)
        Math.abs(value - (r * r))
      }

where:

    type Individual = Array[Boolean]

Crossover function:

    def crossover(rate : => Double): (Individual, Individual) => Individual = ...


Mutation function:

    def mutation(rate : => Double): Individual => Individual = ...


Fitness function

    def oneMin(value: Double): Individual => Double = ..


###Genetic Programming
+ is an extension of the Genetic Algorithm.
+ inspired by population genetics and evolution at the population level.
+ Details: [wiki](http://en.wikipedia.org/wiki/Genetic_programming "wikipedia")
+ Sources: `src/main/scala/geneticProgramming`
+ Run: `sbt 'evolutionary/run-main ua.org.scala.geneticProgramming.app'`


approximation for function: **f(x) = 2x² - 3x - 4**

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


Replacement function:

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


###Evolution Strategies
+ inspired by macro-level of evolution (phenotype, hereditary, variation).
+ Details: [wiki](http://en.wikipedia.org/wiki/Evolution_strategy "wikipedia")
+ Sources: `src/main/scala/evolutionStrategies`
+ Run: `sbt 'evolutionary/run-main ua.org.scala.evolutionStrategies.app'`

###Grammatical Evolution
+ Sources: `src/main/scala/gramaticalEvolution`
+ **Run:** `sbt 'evolutionary/run-main ua.org.scala.gramaticalEvolution.app'`


###Gene Expression Programming
+ Details: [wiki](http://en.wikipedia.org/wiki/Gene_expression_programming "wikipedia")
+ Sources: `src/main/scala/geneExpressionProgramming`
+ Run: `sbt 'evolutionary/run-main ua.org.scala.geneExpressionProgramming.app'`



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


