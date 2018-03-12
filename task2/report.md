# Task 2

## MOEA Strategy
Several MOEA strategies can be applied to a problem in order to handle multiple objectives.
Our strategy is an implementation of the NSGA-II strategy.
Some key elements include:
- Non-dominated search
  - Creates a ranking of which solutions have parameters which dominate others
- Crowding distance
  - Highly advantageous in order to keep a good spread of solutions
  - Makes solutions suitable for crossover in GA applications
- Elitism
  - Begin with creating a population of size 2N (N is the desired population size) combining both parent and offspring population
  - Perform non-dominated sorting on this combined solution front

In comparison to another mentioned MOEA, SPEA, NSGA-II is often _outperformed_ both in terms of convergence and diversity of fronts. SPEA does this by implementing an _external population_ consisting of non-dominated solutions which are later used to ensure elitism in later generations. SPEA assigns more _strength_ (fitness) to an elite solution which dominates many other solutions. The SPEA strategy also requires less calculating power in total.

## Chromosome Representation
In our solution, the chromosome is represented by a collection of segments consisting of different pixels (referred to as nodes). Each node is assigned to one segment only, and one chromosome contains all segments that constitute an image.

Another representation that could have been used for a chromosome is that _one_ segment is a chromosome, which could crossover with connecting segments. This could be fitting we were more interested GA perspective, but not necessarily useful when we are more reliant on the greedy property of Prim's algorithm to calculate the initial segments. This, as well as the benefit of including the entire image's representation in one picture, makes the aforementioned representation most suitable.

## Parameter Weighting
Our solutions use the following two objectives in the segmentation process:
- Edge value (E)
  - How far a new node lies (in RGB distance) from its neighbouring nodes
- Overall deviation (O)
  - How far a new node deviates from the average color of the segment

Adjusting these priorities have many interesting effects.
- E < O
  - Creates segments that look more _convergent_ instead of exploring in many different directions
  - Is more beneficial to ensure that we don't find unfortunate "bald spots" in out segments unless they really should stand out
- E > O
  - Chooses more greedily in which pixel-to-pixel relationships are the closest. This could mean that a gradient color change, however drastic, may be accepted into the same segment even though they begin and end very different. This significantly damages the results for all images that are continuous in color.
  