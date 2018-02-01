from random import randint, random

class Individual():
  def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, parent1=None, parent2=None, random=False):
    self.customers_params = customers_params
    self.depots_params = depots_params
    self.vehicle_max_load = vehicle_max_load
    self.vehicle_max_duration = vehicle_max_duration
    self.num_vehicles = num_vehicles
    self.path_color = [(randint(10, 255),randint(10, 255),randint(10, 255)) for x in range(len(self.depots_params) + self.num_vehicles)]
    self.parent1 = parent1
    self.parent2 = parent2
    if random:
        self.gene = self.construct_random_gene()
    else:
        self.gene = self.construct_gene(self.parent1, self.parent2)
    self.path_length = self.get_path_length(self.gene)



  '''
    Gene where num_vehicles = 4, num_depots = 4, num_customers = 50:
    [[[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]], [[45, 34, 15, 27], [40, 19, 16], [37, 9, 49], [35, 28, 38]], [[1, 31, 17], [23, 43, 5], [4, 12, 26], [8, 10, 30]], [[11, 41, 25], [6, 21, 18], [2, 46, 24], [33, 22, 0]]]
    Each number is an index of a customer
    Each [36, 44, 14, 39] is the path of one car in a depot
    Each [[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]] is the path of all the cars in one depot
  '''

  def crossover_vehicles(len1, len2): #len1 = [4, 3, 4, 0, 2, 4, 3, 0, ...]
    total_customers = len(self.customers_params)
    customers = 0
    result = []

    pointer = 0
    crossover_point = floor(len(len1) * self.crossover_rate)

    while pointer < len(len1):
        if pointer % crossover_point == 0:
            if current_array == len1:
                current_array = len2
            else:
                current_array = len1

        result.append(current_array[pointer])
        customers += current_array[pointer]
        pointer += 1

    while customers > total_customers:
        mutation_point = random.randint(0, len(result))
        result[mutation_point] -= 1
        customers -= 1

    while customers < total_customers:
        mutation_point = random.randint(0, len(result))
        result[mutation_point] += 1
        customers += 1

    return(result[i:i+num_vehicles] for i in xrange(0, len(result), num_vehicles))

  def construct_gene(parent1, parent2):
    customers_copy = self.customers_params[:]
    self.gene = [[[] for i in range(self.num_vehicles)] for x in range(len(self.depots_params))]




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
    return self.gene

  def get_path_length(self, gene):
    path_length = 0
    for i in range(len(gene)):
      curr_depot_coords = (self.depots_params[i][0], self.depots_params[i][1])
      for j in range(len(gene[i])):
        for n in range(len(gene[i][j])-1):
          curr_cust = self.customers_params[gene[i][j][n]]
          next_cust = self.customers_params[gene[i][j][n+1]]

          curr_customer_coords = (curr_cust[1], curr_cust[2])
          next_customer_coords = (next_cust[1], next_cust[2])

          if(n == 0):
            path_length += self.euclideanDistance(curr_depot_coords, curr_customer_coords)
          elif(n == len(gene[i][j])-2):
            path_length += self.euclideanDistance(next_customer_coords, curr_depot_coords)
          path_length += self.euclideanDistance(curr_customer_coords, next_customer_coords)
    return path_length

  def euclideanDistance(self, coordinate1, coordinate2):
    return pow(pow(coordinate1[0] - coordinate2[0], 2) + pow(coordinate1[1] - coordinate2[1], 2), .5)





