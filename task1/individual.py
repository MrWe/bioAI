from random import randint, random, shuffle
#import numpy as np
from config import *
import math
from helpers import *


class Individual():
    def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, mutation_rate): # Initial construction
        self.customers_params = customers_params
        self.depots_params = depots_params
        self.vehicle_max_load = vehicle_max_load
        self.vehicle_max_duration = vehicle_max_duration
        self.num_vehicles = num_vehicles
        self.path_color = [(randint(10, 255), randint(10, 255), randint(
            10, 255)) for x in range(len(self.depots_params) + self.num_vehicles)]
        self.mutation_rate = mutation_rate

        self.nearest_customers, self.borderline = depot_cluster(self.depots_params, self.customers_params)
        self.gene = self.construct_initial_gene(self.nearest_customers)

        self.path_length = self.get_path_length(self.gene)

    def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, mutation_rate, parent1=None, parent2=None, random_individual=True):
        self.customers_params = customers_params
        self.depots_params = depots_params
        self.vehicle_max_load = vehicle_max_load
        self.vehicle_max_duration = vehicle_max_duration
        self.num_vehicles = num_vehicles
        self.path_color = [(randint(10, 255), randint(10, 255), randint(
            10, 255)) for x in range(len(self.depots_params) + self.num_vehicles)]
        self.mutation_rate = mutation_rate

        self.parent1 = parent1.gene
        self.parent2 = parent2.gene
        self.mutation_rate = self.crossover_mutation_rate(parent1.mutation_rate, parent2.mutation_rate)
        self.mutation_rate = self.mutate_mutation_rate(self.mutation_rate)
        self.gene = self.construct_gene(self.parent1, self.parent2)

        self.path_length = self.get_path_length(self.gene)

    def __cmp__(self, other):
        return self.path_length < other.path_length

    '''
    Gene where num_vehicles = 4, num_depots = 4, num_customers = 50:
    [[[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]], [[45, 34, 15, 27], [40, 19, 16], [37, 9, 49], [35, 28, 38]], [[1, 31, 17], [23, 43, 5], [4, 12, 26], [8, 10, 30]], [[11, 41, 25], [6, 21, 18], [2, 46, 24], [33, 22, 0]]]
    Each number is an index of a customer
    Each [36, 44, 14, 39] is the path of one car in a depot
    Each [[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]] is the path of all the cars in one depot
  '''

    def get_vehicle_lengths(self, gene):
      lengths = []
      for depot in gene:
          for vehicle in depot:
              lengths.append(len(vehicle))
      return lengths  # lengths = [4, 3, 4, 0, 2, 4, 3, 0, ...]

    # len1 = [4, 3, 4, 0, 2, 4, 3, 0, ...]
    # Den funker også ikke
    def crossover_vehicle_lengths_deprecated(self, len1, len2):
        total_customers = len(self.customers_params)
        customers = 0
        result = []
        current_array = len1

        pointer = 0
        crossover_point = math.floor(len(len1) * CROSSOVER_RATE)

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

        gene = []
        total_length = 0
        for length in result:
          gene.append(result[total_length:total_length + length])
          total_length += length


        return gene
        #return(result[i:i + self.num_vehicles] for i in range(0, len(result), self.num_vehicles))

#    [[[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]], [[45, 34, 15, 27], [40, 19, 16], [37, 9, 49], [35, 28, 38]], [[1, 31, 17], [23, 43, 5], [4, 12, 26], [8, 10, 30]], [[11, 41, 25], [6, 21, 18], [2, 46, 24], [33, 22, 0]]]

    def construct_gene(self, parent_gene1, parent_gene2):
        # flat_p1 = np.array(parent_gene1)
        # flat_p2 = np.array(parent_gene2)
        # flat_p1 = flat_p1.flatten()
        # flat_p2 = flat_p2.flatten()

        flat_list1 = [item for sublist in parent_gene1 for item in sublist]
        flat_list2 = [item for sublist in parent_gene2 for item in sublist]

        flat_list1 = [item for sublist in flat_list1 for item in sublist]
        flat_list2 = [item for sublist in flat_list2 for item in sublist]

        flat_child_gene = self.crossover(flat_list1, flat_list2)


        flat_child_gene = self.mutate(flat_child_gene)

        p1_vehicle_lengths = self.get_vehicle_lengths(parent_gene1)
        p2_vehicle_lengths = self.get_vehicle_lengths(parent_gene2)

        child_vehicle_lengths = self.crossover_vehicle_lengths(p1_vehicle_lengths, p2_vehicle_lengths)
        child_vehicle_lengths = self.mutate_vehicle_lengths(child_vehicle_lengths)



        child_gene_vehicles = []


        total_length = 0
        for length in p1_vehicle_lengths:
          child_gene_vehicles.append(flat_child_gene[total_length:total_length + length])
          total_length += length

        child_gene_depots = []
        total_length = 0

        #print(child_gene_vehicles)

        for i in range(len(self.depots_params)):
          child_gene_depots.append(child_gene_vehicles[total_length:total_length + self.num_vehicles])
          total_length += self.num_vehicles

        # if(random() < 0.001):
        #   print(flat_list1)
        #   print(p1_vehicle_lengths)
        #   print(child_vehicle_lengths)
        #   print(sum(child_vehicle_lengths))
        #   print(sum(p1_vehicle_lengths))
        #   print(child_gene_depots)


        return child_gene_depots

    def crossover_mutation_rate(self, p1, p2):
      return (p1 + p2) / 2

    def crossover_vehicle_lengths(self, gene1, gene2):
      #crossover_point = math.floor(len(gene1) * CROSSOVER_RATE)
      crossover_point = math.floor(randint(1, len(gene1)-1))
      child_gene = []
      gene1_copy = gene1[:]
      current_array = gene1[:]
      not_current_array = gene2[:]
      pointer = 0

      while(len(child_gene) != len(gene1)):

        if pointer % crossover_point == 0:
              temp = current_array[:]
              current_array = not_current_array[:]
              not_current_array = temp[:]

        if(len(current_array) != 0):

          child_gene.append(current_array[0])
          del current_array[0]

        pointer += 1

      #Kind of mutation. This should change TODO

      high_low = sum(gene1)-sum(child_gene)
      direction = -1 if high_low < 0 else 1
      while(sum(child_gene) != sum(gene1)):
        rand = randint(0, len(child_gene)-1)
        if(child_gene[rand] + direction >= 0): # and child_gene[rand] + direction <= self.num_vehicles <---- This should check max load TODO
          child_gene[rand] += direction

      return child_gene

    def crossover(self, gene1, gene2):
        crossover_point = math.floor(len(gene1) * CROSSOVER_RATE)
        #crossover_point = math.floor(randint(1, len(gene1)-1))
        child_gene = []
        gene1_copy = gene1[:]
        current_array = gene1[:]
        not_current_array = gene2[:]
        pointer = 0

        while(len(child_gene) != len(gene1)):

          if pointer % crossover_point == 0:
                temp = current_array[:]
                current_array = not_current_array[:]
                not_current_array = temp[:]

          if(len(current_array) != 0):
            if(current_array[0] not in child_gene):
              child_gene.append(current_array[0])
            else:
              pointer -= 1
            del current_array[0]
          pointer += 1

        return child_gene

    def mutate_deprecated(self, flat_gene):
      result = flat_gene[:]
      for i in range(NUM_MUTATION_TRIES):
        if(random() < self.mutation_rate):
          a = randint(0, len(flat_gene)-1)
          b = randint(0, len(flat_gene)-1)
          result[b], result[a] = result[a], result[b]
      return result

    def mutate_mutation_rate(self, mutation_rate):
      if(random() < self.mutation_rate): #heh
        if(random() < 0.5):
          mutation_rate -= random()*0.001
        else:
          mutation_rate += random()*0.001
      return mutation_rate

    def mutate(self, flat_gene):
      num = 10
      if random() < self.mutation_rate:
      #   scramble_point = randint(0, len(flat_gene)-(num+1))
      #   t = flat_gene[scramble_point:scramble_point+num][:]
      #   shuffle(t)
      #   flat_gene[scramble_point:scramble_point+num] = t
      #   return flat_gene
      #
      # #if(random() < self.mutation_rate):
      #   reverse_point = randint(0, len(flat_gene)-(num+1))
      #   t = reversed(flat_gene[reverse_point:reverse_point+num][:])
      #   flat_gene[reverse_point:reverse_point+num] = t
      #   return flat_gene

      #if random() < self.mutation_rate:
        for i in range(len(flat_gene)):
          if(random() < self.mutation_rate):
            a = randint(0, len(flat_gene)-1)
            flat_gene[i], flat_gene[a] = flat_gene[a], flat_gene[i]
        return flat_gene
      return flat_gene


    def mutate_vehicle_lengths(self, lengths):
      result = lengths[:]
      for i in range(NUM_MUTATION_TRIES):
        if(random() < self.mutation_rate):
          a = randint(0, len(lengths)-1)
          b = randint(0, len(lengths)-1)
          if(result[a] > 0):
            result[b], result[a] = result[b] + 1, result[a] - 1
      return result

    def construct_initial_gene(self, nearest_customers):
        self.gene = [[[] for i in range(self.num_vehicles)]
                     for x in range(len(self.depots_params))]




    def construct_random_gene(self):
        customers_copy = self.customers_params[:]

        self.gene = [[[] for i in range(self.num_vehicles)]
                     for x in range(len(self.depots_params))]

        depot_selector = 0
        vehicle_selector = 0
        while(len(customers_copy) > 0):
            for i in range(len(self.depots_params)):
                if(len(customers_copy) == 0):
                    break
                rand_customer = randint(0, len(customers_copy) - 1)
                self.gene[depot_selector % len(self.depots_params)][vehicle_selector % self.num_vehicles].append(
                    self.customers_params.index(customers_copy[rand_customer]))
                del customers_copy[rand_customer]
                depot_selector += 1
            vehicle_selector += 1
        return self.gene

    def get_path_length(self, gene):
      path_length = 0
      for i in range(len(gene)):
        curr_depot_coords = (
        self.depots_params[i][0], self.depots_params[i][1])
        for j in range(len(gene[i])):
          curr_vehicle = []
          for n in range(len(gene[i][j])):
            curr_vehicle.append((self.customers_params[gene[i][j][n]][1], self.customers_params[gene[i][j][n]][2]))
          curr_vehicle.insert(0, curr_depot_coords)
          curr_vehicle.append(curr_depot_coords)
          for k in range(len(curr_vehicle)-1):
            path_length += euclideanDistance(curr_vehicle[k], curr_vehicle[k+1])

      return path_length

