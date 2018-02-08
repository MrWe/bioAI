from random import randint, random, shuffle
#import numpy as np
from config import *
import math
from helpers import *


class Individual():
    def __init__(self):
      pass

    def initial_individual(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, mutation_rate): # Initial construction
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

        self.path_length = get_path_length(self.gene, self.depots_params, self.customers_params)
        return self


    def child_individual(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, mutation_rate, parent1, parent2):
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
        # self.mutation_rate = self.crossover_mutation_rate(parent1.mutation_rate, parent2.mutation_rate)
        # self.mutation_rate = self.mutate_mutation_rate(self.mutation_rate)
        self.gene = self.construct_gene(self.parent1, self.parent2)

        self.path_length = get_path_length(self.gene, self.depots_params, self.customers_params)

        return self

    def __cmp__(self, other):
        return self.path_length < other.path_length

    '''
    Gene where num_vehicles = 4, num_depots = 4, num_customers = 50:
    [[[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]], [[45, 34, 15, 27], [40, 19, 16], [37, 9, 49], [35, 28, 38]], [[1, 31, 17], [23, 43, 5], [4, 12, 26], [8, 10, 30]], [[11, 41, 25], [6, 21, 18], [2, 46, 24], [33, 22, 0]]]
    Each number is an index of a customer
    Each [36, 44, 14, 39] is the path of one car in a depot
    Each [[36, 44, 14, 39], [20, 7, 3], [13, 47, 29], [32, 48, 42]] is the path of all the cars in one depot
  '''

    def construct_initial_gene(self, nearest_customers):
      customers_copy = nearest_customers[:]

      gene = [[[] for i in range(self.num_vehicles)]
                   for x in range(len(self.depots_params))]

      for i in range(len(nearest_customers)):
        for j in range(len(nearest_customers[i])):
          rand = randint(0, len(nearest_customers[i])-1)
          gene[i][j % self.num_vehicles].append(nearest_customers[i][rand])
          del nearest_customers[i][rand]

      return gene

    def construct_gene(self, parent_gene1, parent_gene2):
        # flat_p1 = np.array(parent_gene1)
        # flat_p2 = np.array(parent_gene2)
        # flat_p1 = flat_p1.flatten()
        # flat_p2 = flat_p2.flatten()

        flat_list1 = [item for sublist in parent_gene1 for item in sublist]
        flat_list2 = [item for sublist in parent_gene2 for item in sublist]

        flat_list1 = [item for sublist in flat_list1 for item in sublist]
        flat_list2 = [item for sublist in flat_list2 for item in sublist]

        flat_child_gene = self.crossover_genes(flat_list1, flat_list2)
        flat_child_gene = self.mutate_gene(flat_child_gene)

        p1_vehicle_lengths = get_vehicle_lengths(parent_gene1)
        p2_vehicle_lengths = get_vehicle_lengths(parent_gene2)

        child_vehicle_lengths = self.crossover_vehicle_lengths(p1_vehicle_lengths, p2_vehicle_lengths)
        child_vehicle_lengths = self.mutate_vehicle_lengths(child_vehicle_lengths)

        child_gene_vehicles = []

        total_length = 0
        for length in p1_vehicle_lengths:
          child_gene_vehicles.append(flat_child_gene[total_length:total_length + length])
          total_length += length

        child_gene_depots = []
        total_length = 0

        for i in range(len(self.depots_params)):
          child_gene_depots.append(child_gene_vehicles[total_length:total_length + self.num_vehicles])
          total_length += self.num_vehicles

        return child_gene_depots

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

    def crossover_genes(self, gene1, gene2):
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

    def crossover_mutation_rate(self, p1, p2):
      return (p1 + p2) / 2

    def mutate_mutation_rate(self, mutation_rate):
      if(random() < self.mutation_rate): #heh
        if(random() < 0.5):
          mutation_rate -= random()*0.001
        else:
          mutation_rate += random()*0.001
      return mutation_rate

    def mutate_gene(self, flat_gene):
      num = 10
      if random() < self.mutation_rate:
         scramble_point = randint(0, len(flat_gene)-(num+1))
         t = flat_gene[scramble_point:scramble_point+num][:]
         shuffle(t)
         flat_gene[scramble_point:scramble_point+num] = t
         return flat_gene

      if(random() < self.mutation_rate):
         reverse_point = randint(0, len(flat_gene)-(num+1))
         t = reversed(flat_gene[reverse_point:reverse_point+num][:])
         flat_gene[reverse_point:reverse_point+num] = t
         return flat_gene

      if random() < self.mutation_rate:
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

