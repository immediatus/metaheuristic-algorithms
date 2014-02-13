Evolutionary Algorithms
=================================

###Genetic Algorithm
+ adaptive strategy and a global optimization technique.

`src/main/scala/genetic`

**sqrt** function solution search:

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

**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.genetic.app'`

###Genetic Programming
`src/main/scala/geneticProgramming`

**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.geneticProgramming.app`

###Evolution Strategies
`src/main/scala/evolutionStrategies`

**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.evolutionStrategies.app`

###Grammatical Evolution
`src/main/scala/gramaticalEvolution`

**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.gramaticalEvolution.app`


###Gene Expression Programming
`src/main/scala/geneExpressionProgramming`

**Run:**
`sbt 'project evolutionary' 'run ua.org.scala.geneExpressionProgramming.app`
