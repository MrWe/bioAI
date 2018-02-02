import math

def get_best_individual(population):
  path_length = math.inf
  individual = None
  for i in range(len(population.individuals)):
    if(population.individuals[i].path_length < path_length):
      path_length = population.individuals[i].path_length
      individual = population.individuals[i]
  return path_length, individual