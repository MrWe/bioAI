from config import *
from helpers import *
from individual import Individual
import random

class Population():
  def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, parent=None):
    self.customers_params = customers_params
    self.depots_params = depots_params
    self.vehicle_max_load = vehicle_max_load
    self.vehicle_max_duration = vehicle_max_duration
    self.num_vehicles = num_vehicles
    self.individuals = self.construct_population(parent)
    self.surviving_population = self.get_surviving_population()

  def construct_population(self, parent):
    population = []

    if(parent != None):
        parent_population = parent.surviving_population

    for i in range(POPULATION_SIZE):
      if(parent==None): # Create random population
        population.append(Individual(self.customers_params, self.depots_params, self.vehicle_max_load, self.vehicle_max_duration, self.num_vehicles))

      else: # Create population based on parent population

        parent1 = parent_population[random.randint(0,len(parent_population)-1)]
        parent2 = parent_population[random.randint(0,len(parent_population)-1)]
        population.append(Individual(self.customers_params, self.depots_params, self.vehicle_max_load, self.vehicle_max_duration, self.num_vehicles, parent1.gene, parent2.gene, False))

    return population


  def get_surviving_population(self):
      return sorted(self.individuals, key=lambda x: x.path_length)[:20] #20 is how many individuals we keep to perform crossover

