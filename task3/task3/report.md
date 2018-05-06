# Report
Agnete Djupvik and Erik Wiker

__1: BA Strategy__
The Bees algorithm naturally operates in a continuous search space. This means that, in the objective function, variables are required to be continuous. In our algorithm, the bees that naturally move in a continuous space is simulated in a discrete space. A scout bee naturally moves in a gradual manner from the hive to a random flower patch, but our algorithm places it directly at a discrete point in our search space.
This also applies to performing the _waggle dance_, which would normally have bees swarming around an elite patch. In our algorithm, bees are placed at determined locations surrounding the patch directly, not moving out from the center of the local search space.

There are several other discretization strategies. An example is the notion of __fast marriage in honey bee optimization algorithm__, first described by Karaboga and Basturk (2007)[1]. This algorithm is presented in several versions, but all versions utilize logic operators (AND, OR, XOR) to attempt to improve the fitness of the queen bee (the fittest solution in the hive). This is done by applying these operations from a random _bit_ of a random worker, to see if the queen's fitness can be improved. Then, crossover is applied to the queen and a random drone and the resulting _brood_ is mutated.
These algorithms produce just as good results and studies show, as described by Salim et. al (2007)[2], that they perform _faster_ as the number of variables increases, than the classic Bees algorithm.

__2: Exploration and exploitation in ACO and BA__
With BA, exploitation is performed on a concrete flower patch through the _waggle dance_. The length of the dance, or degree of exploration, is determined by the rating done by the scout bee, and as long as a flower patch still among the highest rated, it will always continue to be visited until better patches turn up elsewhere, or the waggle dance results in finding better maxima surrounding the initially discovered patch. An important parameter to tune was how many resources should be allocated to the exploration of a flower patch for a given rating given by the scout, to be able to escape local maxima while still concluding on global maxima.

With ACO, solutions are constructed as the ant "walks" in a "path" given by its pheromone matrix. When a specific path is walked, all its steps are highlighted in the pheromone matrix. This means that in the first iteration, only one path will be highlighted. This prompts the need for a sufficient _evaporation rate_. If this rate was too low, the first paths would seem overly attractive to the next ants and exploration would be discouraged. If too high, it might never conclude on an optimized solution as no paths would end up being clearly highlighted. This rate was finely tuned in our algorithm to avoid both of these pitfalls.

__3: Variants within ACO__
The variant we selected for our ACO implementation is a hybrid heuristic variant called HACO, presented by Rossi and Boschi (2009)[3]. This combines the classic ACO with GA components, producing a heuristic which better can indicate which areas in the search space to exploit. From there, we perform a local beam search to further explore these areas in a fast and simple manner. However, this search is performed rarely as this method of search has a high risk of not being able to leave a local maxima or a plateau[4]. However, with the HACO algorithm, this allows us to find close-to-optimal results much quicker than without the added local search.

Another possible variant of the ACO algorithm is the Recursive ACO algorithm. The algorithm, described by Gupta et. al. (2012)[5] utilises the divide-and-conquer approach by dividing the search space into several sub-spaces and solves for the objective function at that sub-level. The algorithm introduces the term _depth_ to decide the depth of recursion, and combines the sub-solutions found until a valid and optimal solution.
The efficiency and stability was proved by Gupta et. al. for a problem unrelated to the JSSP – rather for a geophysical problem exploring anomalies in a body of earth. This method proved quick and efficient, but as no research on this method related to the JSSP problem, we decided not to explore this any further due to the shorter time span of the project.


[1] _Basturk B, Karaboga D_ (2006) An artificial bee colony (ABC) algorithm for numeric function optimization. In: Proceedings of the IEEE swarm intelligence symposium 2006. Indianapolis, Indiana, USA

[2] _Salim, M. & Vakil-Baghmisheh_ (2011) M.T. Artif Intell Rev, 35: 73. https://doi.org/10.1007/s10462-010-9184-8

[3] _Andrea Rossi, Elena Boschi_ (2009), A hybrid heuristic to solve the parallel machines job-shop scheduling problem, Advances in Engineering Software, Volume 40, Issue 2, Pages 118-127, https://doi.org/10.1016/j.advengsoft.2008.03.020.

[4] _Norvig, Peter_ (1992). Paradigms of Artificial Intelligence Programming: Case Studies in Common LISP. Pages 189-191. Morgan Kaufmann

[5] _Gupta, D.K.; Arora, Y.; Singh, U.K.; Gupta, J.P.,_ (2012) "Recursive Ant Colony Optimization for estimation of parameters of a function," Recent Advances in Information Technology (RAIT), Pages 448-454