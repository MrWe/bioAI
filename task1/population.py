from config import *
from helpers import *
from individual import Individual
import random
import math

class Population():

  def __init__(self, customers_params, depots_params, num_vehicles, mutation_rate, nearest_customers, borderline, parent=None):
    self.customers_params = customers_params
    self.depots_params = depots_params
    self.num_vehicles = num_vehicles
    self.mutation_rate = mutation_rate
    self.nearest_customers = nearest_customers
    self.borderline = nearest_customers

    if(parent):
      self.individuals = self.construct_population(parent)
    else:
      self.individuals = self.construct_init_population()

    self.surviving_population = self.get_surviving_population()

  def construct_init_population(self):
    population = []

    for i in range(int(POPULATION_SIZE)):
      population.append(Individual().initial_individual(self.customers_params, self.depots_params, self.num_vehicles, self.mutation_rate, self.nearest_customers, self.borderline))

    return population

  def construct_population(self, parent):
    population = []

    parent_population = parent.surviving_population

    for i in range(int(POPULATION_SIZE / 2)):
      parent1 = self.tournament_selection(parent_population)
      parent2 = self.tournament_selection(parent_population)

      c1, c2 = construct_child_gene(parent1.gene, parent2.gene, self.customers_params, self.depots_params, self.num_vehicles)
      population.append(Individual().init_with_gene(self.customers_params, self.depots_params, self.num_vehicles, c1, self.mutation_rate, self.nearest_customers, self.borderline))
      population.append(Individual().init_with_gene(self.customers_params, self.depots_params, self.num_vehicles, c2, self.mutation_rate, self.nearest_customers, self.borderline))

    #TODO: Should create method to copy parent as another individual and add that to population to prevent mutation on that parent
    population.append(parent_population[0])
    population.append(parent_population[1])


    # for i in range(int(POPULATION_SIZE - 2)):
    #   population.append(Individual().child_individual(self.customers_params, self.depots_params, self.num_vehicles, self.mutation_rate, parent1, parent2))

    return population

  def pick_parent(self, population):
    percent = math.floor(((len(population) / 100) * 10))
    best_parent = None
    for i in range(percent):
      curr_option = population[random.randint(0,len(population)-1)]
      if(best_parent == None or curr_option.path_length < best_parent.path_length):
        best_parent = curr_option
    return best_parent

  def tournament_selection(self, population):
    p1 = population[random.randint(0, len(population)-1)]
    p2 = population[random.randint(0, len(population)-1)]
    r = random.random()
    if(r < 0.8):
      selected = p1 if p1.fitness < p2.fitness else p2
    else:
      selected = p1 if random.random() < 0.5 else p2
    return selected

  def get_surviving_population(self):
    #return sorted(self.individuals, key=lambda x: x.fitness)[:POPULATION_SURVIVORS] #POPULATION_SURVIVORS is how many individuals we keep to perform crossover
    return sorted(self.individuals, key=lambda x: x.fitness)

  # def get_best_individual(self):
  #   for individual in self.individuals:
  #     if(individual.valid):
  #       return individual
