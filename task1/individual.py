from random import random, shuffle

#import numpy as np
from config import *
import math
from helpers import *
import time


class Individual():
    def __init__(self):
      pass

    def initial_individual(self, customers_params, depots_params, num_vehicles, mutation_rate, nearest_customers, borderline, mem_keys, mem_vals, customer_2_customer, customer_2_depots, depots_2_customers): # Initial construction
      self.customers_params = customers_params
      self.depots_params = depots_params
      self.mem_keys = mem_keys
      self.mem_vals = mem_vals
      self.num_vehicles = num_vehicles
      self.customer_2_customer = customer_2_customer
      self.customer_2_depots = customer_2_depots
      self.depots_2_customers = depots_2_customers
      self.gene = nearest_customers
      for i in range(len(self.gene)):
        shuffle(self.gene[i])

      self.borderline = borderline
      self.mutation_rate = mutation_rate
      self.gene, self.borderline = self.mutate_gene(self.gene, self.borderline)
      self.hash = self.get_hash(self.gene)
      if(self.hash in self.mem_keys):
        self.vehicles = self.mem_vals[self.hash][0]
        self.path_length = self.mem_vals[self.hash][1]
        self.fitness = self.mem_vals[self.hash][2]
        self.lengths = self.mem_vals[self.hash][3]
      else:
        self.vehicles = construct_vehicles(self.gene, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
        self.path_length, self.lengths = get_path_length(self.vehicles, self.depots_params, self.customers_params, self.num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
        self.fitness = self.get_fitness()
        self.mem_keys.add(self.hash)
        self.mem_vals[self.hash] = []
        self.mem_vals[self.hash].append(self.vehicles)
        self.mem_vals[self.hash].append(self.path_length)
        self.mem_vals[self.hash].append(self.fitness)
        self.mem_vals[self.hash].append(self.lengths)

      return self, self.borderline, self.mem_keys, self.mem_vals

    def init_with_gene(self, customers_params, depots_params, num_vehicles, gene, mutation_rate, nearest_customers, borderline, mem_keys, mem_vals, customer_2_customer, customer_2_depots, depots_2_customers):
      self.customers_params = customers_params
      self.depots_params = depots_params
      self.mem_keys = mem_keys
      self.mem_vals = mem_vals
      self.customer_2_customer = customer_2_customer
      self.customer_2_depots = customer_2_depots
      self.depots_2_customers = depots_2_customers
      #self.nearest_customers, self.borderline = depot_cluster(self.depots_params, self.customers_params)
      self.nearest_customers = nearest_customers
      self.borderline = borderline
      self.num_vehicles = num_vehicles
      self.gene = gene
      self.mutation_rate = mutation_rate
      # print(gene)
      # print("\n")
      self.gene, self.borderline = self.mutate_gene(self.gene, self.borderline)
      self.hash = self.get_hash(self.gene)
      if(self.hash in self.mem_keys):
        self.vehicles = self.mem_vals[self.hash][0]
        self.path_length = self.mem_vals[self.hash][1]
        self.fitness = self.mem_vals[self.hash][2]
        self.lengths = self.mem_vals[self.hash][3]
      else:
        self.vehicles = construct_vehicles(self.gene, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
        self.path_length, self.lengths = get_path_length(self.vehicles, self.depots_params, self.customers_params, self.num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
        self.fitness = self.get_fitness()
        self.mem_keys.add(self.hash)
        self.mem_vals[self.hash] = []
        self.mem_vals[self.hash].append(self.vehicles)
        self.mem_vals[self.hash].append(self.path_length)
        self.mem_vals[self.hash].append(self.fitness)
        self.mem_vals[self.hash].append(self.lengths)

      return self, self.borderline, self.mem_keys, self.mem_vals


    def __cmp__(self, other):
        return self.path_length < other.path_length

    def get_hash(self, gene):
        return str(gene)

    def is_valid(self):
      depots_fitness = 0
      for i in range(len(self.gene)):
        depots_fitness += self.get_depot_fitness(self.gene[i], i)
      if(depots_fitness > 0):
        return True
      else:
        return False

    def get_results(self):
        for i in range(len(self.vehicles)-1,-1,-1):
          for j in range(len(self.vehicles[i])-1,-1,-1):
            if(len(self.vehicles[i][j]) == 0):
              del self.vehicles[i][j]

        results_array = ""
        results_array += str(self.path_length) + "\n"
        for i in range(len(self.vehicles)):
            for j in range(len(self.vehicles[i])):
                results_array += str(i+1) + " "

                results_array += str(j+1) + " "


                results_array += str(round(self.lengths[i][j],3)) + " "

                results_array += str(get_route_load(self.vehicles[i][j], self.depots_params, self.customers_params)) + " "

                results_array += "0 "

                for v in range(len(self.vehicles[i][j])):
                    results_array += str(self.vehicles[i][j][v]+1) + " "

                results_array += "0"
                results_array += "\n"
        return results_array

    '''
        1   1   60.06   71   0 44 45 33 15 37 17 0
        1   2   66.55   79   0 42 19 40 41 13 0
        1   3   47.00   78   0 25 18 4 0
        2   1   53.44   73   0 6 27 1 32 11 46 0
        2   2   79.47   80   0 48 8 26 31 28 22 0
        2   3   81.40   77   0 23 7 43 24 14 0
        2   4   23.50   54   0 12 47 0
        3   1   50.41   75   0 9 34 30 39 10 0
        3   2   25.22   54   0 49 5 38 0
        4   1   47.67   67   0 35 36 3 20 0
        4   2   42.14   69   0 21 50 16 2 29 0
    '''





    def mutate_gene(self, gene, borderline):
      if random() < 0.1: #Should execute at exactly every 10 generations, men erresånøyea
          return self.inter_depot_mutation(gene, borderline)

      if random() < self.mutation_rate:
         return self.intra_depot_mutation(gene), borderline
      return gene, borderline

    def intra_depot_mutation(self, gene):
      rand_depot = randint(0, len(gene))
      depot = gene[rand_depot]

      # if(len(depot) < 2):
      #   return gene

      r = random()
      if(r < 0.33):
        mutated_depot = self.swap(depot)
      if(r > 0.66):
        mutated_depot = self.reverse(depot)
      else:
        return self.reroute(depot, rand_depot, gene)

      gene[rand_depot] = mutated_depot

      return gene

    def swap(self, depot):
      r1 = randint(0, len(depot))
      r2 = randint(0, len(depot))
      while(r2 != r1):
        r2 = randint(0, len(depot))
      depot[r1], depot[r2] = depot[r2], depot[r1]
      return depot

    def reverse(self, depot):
      if(len(depot) <= 2):
        return reversed(depot)
      r1 = randint(0, len(depot)-1)
      r2 = randint(r1+1, len(depot))
      t = reversed(depot[r1:r2][:])
      depot[r1:r2] = t
      return depot

    def reroute(self, depot, rand_depot, gene):

      depot = depot[:]
      copy_gene = self.copy_gene(gene)
      customer_to_move_index = randint(0, len(depot))
      customer_to_move = depot[customer_to_move_index]
      del depot[customer_to_move_index]
      copy_gene[rand_depot] = depot

      best_location = (0, 0) #(depot, index) e.g. (1, 3)
      best_path_length = float("Inf")
      for i in range(len(copy_gene)):
          for index in range(len(copy_gene[i])):
              copy_gene[i].insert(index, customer_to_move)

              str_hash = self.get_hash(copy_gene)
              if(str_hash in self.mem_keys):
                vehicles = self.mem_vals[str_hash][0]
                curr_path_length = self.mem_vals[str_hash][1]
              else:
                vehicles = construct_vehicles(copy_gene, self.customers_params, self.depots_params, self.num_vehicles, self.customer_2_customer, self.customer_2_depots, self.depots_2_customers)
                curr_path_length = get_path_length(vehicles, self.depots_params, self.customers_params, self.num_vehicles, self.customer_2_customer, self.customer_2_depots, self.depots_2_customers)[0]

              if curr_path_length < best_path_length:
                  best_path_length = curr_path_length
                  best_location = (i, index)
              copy_gene[i].remove(customer_to_move)
      copy_gene[best_location[0]].insert(best_location[1], customer_to_move)
      return copy_gene

    def inter_depot_mutation(self, gene, borderline):
      #lengths = get_vehicle_lengths(gene)
      selected_depot = randint(0, len(borderline)) #the depot we want to move something into

      if len(borderline[selected_depot]) == 0: #Nothing to mutate
          return gene, borderline

      selected_customer_index = randint(0, len(borderline[selected_depot]))

      if(len(borderline[selected_depot]) == 0):
        return gene, borderline
      # print(borderline[selected_depot])

      selected_customer = borderline[selected_depot][selected_customer_index] #e.g. 5
      # print(selected_customer)
      # print(gene)
      # print("\n")

      del borderline[selected_depot][selected_customer_index] #we now have the item to move into our selected_depot

      customer_location = self.find(gene, selected_customer) # returns depot index

      gene[customer_location].remove(selected_customer)
      borderline[customer_location].append(selected_customer)

      gene[selected_depot].append(selected_customer)

      return gene, borderline

    def find(self, searchList, elem):
        for depot in range(len(searchList)):
            if elem in searchList[depot]:
                return depot

    def get_fitness(self):
      a = 100
      b = 0.001
      num_cars = 0
      for depot in self.vehicles:
        for car in depot:
          if(len(car) != 0):
            num_cars += 1


      return (a * num_cars) + (b * self.path_length)

      #return a * (self.num_vehicles * len(self.depots_params)) + (b * self.path_length)

    def get_depot_fitness(self, depot, i):
      load_over_max = 0
      for car in depot:
        car_load = get_route_load(car, self.depots_params, self.customers_params)
        load_over_max += car_load-self.depots_params[i][3] if car_load-self.depots_params[i][3] > 0 else 0
      return load_over_max


    def copy_gene(self, gene):
      new_gene = []
      for depot in gene:
          new_gene.append(depot[:])
      return new_gene

