from config import *
from helpers import *
from individual import Individual
import random
import math

class Population():

  def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, mutation_rate, parent=None):
    self.customers_params = customers_params
    self.depots_params = depots_params
    self.vehicle_max_load = vehicle_max_load
    self.vehicle_max_duration = vehicle_max_duration
    self.num_vehicles = num_vehicles
    self.mutation_rate = mutation_rate

    if(parent):
      self.individuals = self.construct_population(parent)
    else:
      self.individuals = self.construct_init_population()

    self.surviving_population = self.get_surviving_population()

  def construct_init_population(self):
    population = []

    for i in range(int(POPULATION_SIZE)):
      population.append(Individual().initial_individual(self.customers_params, self.depots_params, self.vehicle_max_load, self.vehicle_max_duration, self.num_vehicles, self.mutation_rate))

    return population

  def construct_population(self, parent):
    population = []

    parent_population = parent.surviving_population
    parent1 = self.tournament_selection(parent_population)
    parent2 = self.tournament_selection(parent_population)

    population.append(parent1)
    population.append(parent2)

    for i in range(int(POPULATION_SIZE - 2)):
      population.append(Individual().child_individual(self.customers_params, self.depots_params, self.vehicle_max_load, self.vehicle_max_duration, self.num_vehicles, self.mutation_rate, parent1, parent2))

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




  def prioritize_population(self, population):
    priority_populaton = []
    percent = math.floor(((len(population) / 100) * 10))
    for i in range(percent):
      priority_populaton.append(population[i])
    return priority_populaton

  def get_surviving_population(self):
    #return sorted(self.individuals, key=lambda x: x.fitness)[:POPULATION_SURVIVORS] #POPULATION_SURVIVORS is how many individuals we keep to perform crossover
    return sorted(self.individuals, key=lambda x: x.fitness)
