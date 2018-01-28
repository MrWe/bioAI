from random import randint, random

class Individual():
  def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles):
    self.customers_params = customers_params
    self.depots_params = depots_params
    self.vehicle_max_load = vehicle_max_load
    self.vehicle_max_duration = vehicle_max_duration
    self.num_vehicles = num_vehicles
    self.path_color = [(randint(10, 255),randint(10, 255),randint(10, 255)) for x in range(len(self.depots_params) + self.num_vehicles)]
    self.construct_random_gene()


  '''
    Gene where num_vehicles = 4, num_depots = 4, num_customers = 50:
    [[[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]], [[45, 34, 15, 27], [40, 19, 16], [37, 9, 49], [35, 28, 38]], [[1, 31, 17], [23, 43, 5], [4, 12, 26], [8, 10, 30]], [[11, 41, 25], [6, 21, 18], [2, 46, 24], [33, 22, 0]]]
    Each number is an idnex of a customer
    Each [36, 44, 14, 39] is the path of one car in a depot
    Each [[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]] is the path of all the cars in one depot
  '''
  def construct_random_gene(self):
    customers_copy = self.customers_params[:]

    self.gene = [[[] for i in range(self.num_vehicles)] for x in range(len(self.depots_params))]

    depot_selector = 0
    vehicle_selector = 0
    while(len(customers_copy) > 0):
      for i in range(len(self.depots_params)):
        if(len(customers_copy) == 0): break
        rand_customer = randint(0, len(customers_copy)-1)
        self.gene[depot_selector % len(self.depots_params)][vehicle_selector % self.num_vehicles].append(self.customers_params.index(customers_copy[rand_customer]))
        del customers_copy[rand_customer]
        depot_selector += 1
      vehicle_selector += 1
    print(self.gene)





