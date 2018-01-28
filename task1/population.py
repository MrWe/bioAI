from individual import Individual
from config import *

class Population():
  def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles):
    self.customers_params = customers_params
    self.depots_params = depots_params
    self.vehicle_max_load = vehicle_max_load
    self.vehicle_max_duration = vehicle_max_duration
    self.num_vehicles = num_vehicles
    self.individuals = self.construct_population()

  def construct_population(self):
    population = []
    for i in range(POPULATION_SIZE):
      population.append(Individual(self.customers_params, self.depots_params, self.vehicle_max_load, self.vehicle_max_duration, self.num_vehicles))
    return population

