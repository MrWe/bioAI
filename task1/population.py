from config import *
from helpers import *
from individual import Individual


class Population():
  def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, random_population=True):
    self.customers_params = customers_params
    self.depots_params = depots_params
    self.vehicle_max_load = vehicle_max_load
    self.vehicle_max_duration = vehicle_max_duration
    self.num_vehicles = num_vehicles
    self.individuals = self.construct_population(random_population)

  def construct_population(self, random_population):
    population = []
    for i in range(POPULATION_SIZE):
      if(random_population):
        population.append(Individual(self.customers_params, self.depots_params, self.vehicle_max_load, self.vehicle_max_duration, self.num_vehicles))
      else:
        population.append(Individual(self.customers_params, self.depots_params, self.vehicle_max_load, self.vehicle_max_duration, self.num_vehicles))
    return population
