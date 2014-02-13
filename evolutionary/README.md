Evolutionary Algorithms
=================================

#Genetic Algorithm
`src/main/scala/genetic`

sqrt function solution search:

    def oneMin(value: Double): String => Double =
      in => {
        val r = atod(in)
        Math.abs(value - (r * r))
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


#Genetic Programming
`src/main/scala/geneticProgramming`


#Evolution Strategies
`src/main/scala/evolutionStrategies`


#Grammatical Evolution
`src/main/scala/gramaticalEvolution`


#Gene Expression Programming
`src/main/scala/geneExpressionProgramming`

