from random import randint, random, shuffle
from copy import deepcopy
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
        self.nearest_customers, self.borderline = depot_cluster(self.depots_params, self.customers_params)
        self.vehicle_max_load = vehicle_max_load
        self.vehicle_max_duration = vehicle_max_duration
        self.num_vehicles = num_vehicles
        self.path_color = [(randint(10, 255), randint(10, 255), randint(
            10, 255)) for x in range(len(self.depots_params) + self.num_vehicles)]
        self.mutation_rate = mutation_rate

        self.gene = self.construct_initial_gene(self.nearest_customers)

        self.path_length = get_path_length(self.gene, self.depots_params, self.customers_params)
        self.fitness = self.get_fitness()
        return self


    def child_individual(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, mutation_rate, parent1, parent2):
        self.customers_params = customers_params
        self.depots_params = depots_params
        self.nearest_customers, self.borderline = depot_cluster(self.depots_params, self.customers_params)
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
        self.gene = self.construct_flat_gene(self.parent1, self.parent2)

        #print("GENE", self.gene)
        #print("DEPOTS", self.depots_params)
        #print("CUSTOMERS", self.customers_params)
        self.path_length = get_path_length(self.gene, self.depots_params, self.customers_params)
        self.fitness = self.get_fitness()
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

    def construct_flat_gene(self, parent_gene1, parent_gene2):
        flat_list1 = flatten(parent_gene1)
        flat_list2 = flatten(parent_gene2)

        flat_list1 = flatten(flat_list1)
        flat_list2 = flatten(flat_list2)

        flat_child_gene = self.crossover_genes(flat_list1, flat_list2)
        flat_child_gene = self.mutate_gene(parent_gene1, flat_child_gene)

        p1_vehicle_lengths = get_vehicle_lengths(parent_gene1)
        p2_vehicle_lengths = get_vehicle_lengths(parent_gene2)

        child_vehicle_lengths = self.crossover_vehicle_lengths(p1_vehicle_lengths, p2_vehicle_lengths)
        #child_vehicle_lengths = self.mutate_vehicle_lengths(child_vehicle_lengths)

        child_gene = self.construct_gene(flat_child_gene, child_vehicle_lengths)

        return child_gene

    def construct_gene(self, flat_gene, vehicle_lengths):
      vehicles = self.construct_vehicles(flat_gene, vehicle_lengths)
      depots = []
      for i in range(len(self.depots_params)):
          depots.append(self.construct_depot(vehicles, i))
      return depots

    def construct_depot(self, vehicles, index):
      # total_length = 0
      # depots.append(vehicles[total_length:total_length + self.num_vehicles])
      # total_length += self.num_vehicles
      return vehicles[self.num_vehicles*index:(self.num_vehicles*index)+self.num_vehicles]

    def construct_vehicles(self, flat_gene, vehicle_lengths):
      vehicles = []
      total_length = 0
      for length in vehicle_lengths:
        vehicles.append(flat_gene[total_length:total_length + length])
        total_length += length
      return vehicles

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

    def intra_depot_mutation(self, gene):
      rand_depot = randint(0, len(gene)-1)
      depot = [item for sublist in gene[rand_depot] for item in sublist]
      vehicle_lengths = get_vehicle_lengths(gene)

      if(len(depot) < 2):
        gene = flatten(gene)
        gene = flatten(gene)
        return gene
      r = random()
      if(r < 0.5):
        self.swap(depot)
        pass
      # elif(r > 0.66):
      #
      #   #self.reverse(depot)
      #   pass
      else:
        return self.reroute(depot, rand_depot, gene, vehicle_lengths)

      new_depot = self.construct_vehicles(depot, vehicle_lengths[self.num_vehicles*rand_depot:(self.num_vehicles*rand_depot)+self.num_vehicles])
      gene[rand_depot] = new_depot

      gene = flatten(gene)
      gene = flatten(gene)

      return gene

    def swap(self, depot):
      r1 = randint(0, len(depot)-1)
      r2 = randint(0, len(depot)-1)
      while(r2 != r1):
        r2 = randint(0, len(depot)-1)
      depot[r1], depot[r2] = depot[r2], depot[r1]

    def reverse(self, depot):
      if(len(depot) <= 2):
        return reversed(depot)
      r1 = randint(0, len(depot)-2)
      r2 = randint(r1+1, len(depot)-1)
      t = reversed(depot[r1:r2][:])
      depot[r1:r2] = t

    def reroute(self, depot, rand_depot, gene, vehicle_lengths):
      depot = depot[:]
      r = randint(0, len(depot)-1)
      best_fitness = float("Inf")
      best_gene = []
      cust = depot[r]
      del depot[r]

      for i in range(len(depot)):
        #curr_gene = deepcopy(gene) #TODO: Find alternative to deepcopy
        curr_gene = self.copy_gene(gene) #This hopefully works
        curr_depot = depot[:]
        curr_depot.insert(i, cust)
        curr_depot = self.construct_vehicles(curr_depot, vehicle_lengths[self.num_vehicles*rand_depot:(self.num_vehicles*rand_depot)+self.num_vehicles])
        curr_gene[rand_depot] = curr_depot
        path_length = get_path_length(curr_gene, self.depots_params, self.customers_params)

        if(path_length < best_fitness):
          best_fitness = path_length
          best_gene = curr_gene

      best_gene = flatten(best_gene)
      best_gene = flatten(best_gene)
      return best_gene

    def crossover_mutation_rate(self, p1, p2):
      return (p1 + p2) / 2

    def inter_depot_mutation(self, gene):
      lengths = get_vehicle_lengths(gene)
      selected_depot = randint(0, len(self.borderline)-1)
      selected_customer = randint(0, len(self.borderline[selected_depot])-1)
      customer_to_move = self.borderline[selected_depot][selected_customer]

      mutated_gene = gene[:]
      mutated_gene = flatten(mutated_gene)
      mutated_gene = flatten(mutated_gene)
      borderline_customer = mutated_gene.index(customer_to_move)

      mutated_gene[borderline_customer], self.borderline[selected_depot][selected_customer] = customer_to_move, borderline_customer

      return mutated_gene

    def mutate_mutation_rate(self, mutation_rate):
      if(random() < self.mutation_rate): #heh
        if(random() < 0.5):
          mutation_rate -= random()*0.001
        else:
          mutation_rate += random()*0.001
      return mutation_rate

    def mutate_gene(self, gene, flat_gene):
      if random() < 0.01: #Should execute at exactly every 10 generations, men erresånøyea
          return self.inter_depot_mutation(gene)
          pass

      if random() < self.mutation_rate:
          return self.intra_depot_mutation(gene)
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

    def get_fitness(self):
      a = 100
      b = 0.001

      return a * (self.num_vehicles * len(self.depots_params)) + (b * self.path_length)

    def copy_gene(self, gene):
      new_gene = []
      for depot in gene:
        new_depot = []
        for vehicle in depot:
          new_depot.append(vehicle[:])
        new_gene.append(new_depot)
      return new_gene

