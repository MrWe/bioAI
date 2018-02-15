# Initialization

In the first iteration of the program all customers when assigned to a random depot. This proved to not be optimal. Instead we introduced clustering.

### Clustering

At the beginning of the program each customer is assigned to the nearest depot. This is done since we can be reasonably sure that an optimal solution will be in the area of each customer being served by the nearest depot. However there are edge cases where this is not the case, this is then fixed with inter-mutation. The customers who are eligible for inter-mutation is given by the formula

```
distance(c,di) − min
---------------------
      min                     ≤ BOUND
```

where _distance(c,di)_ is the distance from customer to depot i.

The bound is set to a range between 0.01 and 0.1 in our program depending on the number of customers which are close to other depots.

# Chromosome representation

### Current
```
[[c1,c2,c3,c4], [c5,c6,c7], [c8,c9]]
```

We decided to represent the gene as an array of depots, where each depot contains customers to visit.
Vehicles are generated from this depot by the following function:

for each depot:
  create car
  add customer to car until a new customer will make either duration or load go over max.

This ensures a minimum number of vehicles. Since each vehicle will add a distance from and to the current depot, a minimal number of vehicles is preferred.

A 2d representation made both inter- and intra-mutation easier and less cpu heavy which was important for running the GA.


### Alternative

```
[[[c1,c2],[c3,c4],[],[]], [[c5,c6,c7],[],[],[]],[[c8],[c9],[],[]]
```

This alternative contains the depots as in the previous example but also contains the maximum number of cars for each depot (here: 3) even if the car is empty. This representation was used in the first iteration of the project but was discarded when mutation and the current crossover was introduced. The reason for discarding this representation was to minimize cpu load when iterating the genome, technical issues when doing crossover and mutation and to guarantee a minimized number of vehicles, which this genome do not.


# Crossover

The crossover function

# Mutation

## Intra-mutation

### Reroute

### Inverse

### Swap

## Inter-mutation

