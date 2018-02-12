from random import randint, random, shuffle
from copy import deepcopy
#import numpy as np
from config import *
import math
from helpers import *
import time


class Individual():
    def __init__(self):
      pass

    def initial_individual(self, customers_params, depots_params, num_vehicles, mutation_rate, nearest_customers, borderline): # Initial construction
        self.customers_params = customers_params
        self.depots_params = depots_params
        #self.gene, self.borderline = depot_cluster(self.depots_params, self.customers_params)
        self.num_vehicles = num_vehicles
        self.gene = nearest_customers
        self.borderline = borderline
        self.mutation_rate = mutation_rate
        self.gene, self.borderline = self.mutate_gene(self.gene, self.borderline)
        self.vehicles = construct_vehicles(self.gene, customers_params, depots_params)

        #self.valid = self.is_valid()

        self.path_length = get_path_length(self.vehicles, self.depots_params, self.customers_params, self.num_vehicles)
        self.fitness = self.get_fitness()
        return self, self.borderline

    def init_with_gene(self, customers_params, depots_params, num_vehicles, gene, mutation_rate, nearest_customers, borderline):
      self.customers_params = customers_params
      self.depots_params = depots_params
      #self.nearest_customers, self.borderline = depot_cluster(self.depots_params, self.customers_params)
      self.nearest_customers = nearest_customers
      self.borderline = borderline
      self.num_vehicles = num_vehicles
      self.gene = gene
      self.mutation_rate = mutation_rate
      self.gene, self.borderline = self.mutate_gene(self.gene, self.borderline)
      self.vehicles = construct_vehicles(self.gene, customers_params, depots_params)

      self.path_length = get_path_length(self.vehicles, self.depots_params, self.customers_params, self.num_vehicles)
      self.fitness = self.get_fitness()

      return self, self.borderline

    def __cmp__(self, other):
        return self.path_length < other.path_length

    def is_valid(self):
      depots_fitness = 0
      for i in range(len(self.gene)):
        depots_fitness += self.get_depot_fitness(self.gene[i], i)
      if(depots_fitness > 0):
        return True
      else:
        return False

    def construct_gene(self, flat_gene, vehicle_lengths):
      vehicles = self.construct_vehicles(flat_gene, vehicle_lengths)
      depots = []
      for i in range(len(self.depots_params)):
          depots.append(self.construct_depot(vehicles, i))
      return depots

    def construct_depot(self, vehicles, index):
      return vehicles[self.num_vehicles*index:(self.num_vehicles*index)+self.num_vehicles]

    def construct_vehicles(self, flat_gene, vehicle_lengths):
      vehicles = []
      total_length = 0
      for length in vehicle_lengths:
        vehicles.append(flat_gene[total_length:total_length + length])
        total_length += length
      return vehicles

    def mutate_gene(self, gene, borderline):
      if random() < 0.1: #Should execute at exactly every 10 generations, men erresånøyea
          return self.inter_depot_mutation(gene, borderline)
      if random() < self.mutation_rate:
         return self.intra_depot_mutation(gene), borderline
      return gene, borderline

    def intra_depot_mutation(self, gene):
      rand_depot = randint(0, len(gene)-1)
      depot = gene[rand_depot]

      if(len(depot) < 2):
        return gene

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
      r1 = randint(0, len(depot)-1)
      r2 = randint(0, len(depot)-1)
      while(r2 != r1):
        r2 = randint(0, len(depot)-1)
      depot[r1], depot[r2] = depot[r2], depot[r1]
      return depot

    def reverse(self, depot):
      if(len(depot) <= 2):
        return reversed(depot)
      r1 = randint(0, len(depot)-2)
      r2 = randint(r1+1, len(depot)-1)
      t = reversed(depot[r1:r2][:])
      depot[r1:r2] = t
      return depot

    def reroute(self, depot, rand_depot, gene):
      depot = depot[:]
      copy_gene = self.copy_gene(gene)
      customer_to_move_index = randint(0, len(depot)-1)
      customer_to_move = depot[customer_to_move_index]
      del depot[customer_to_move_index]
      copy_gene[rand_depot] = depot

      best_location = (0, 0) #(depot, index) e.g. (1, 3)
      best_path_length = float("Inf")
      for i in range(len(copy_gene)):
          for index in range(len(copy_gene[i])):
              copy_gene[i].insert(index, customer_to_move)

              vehicles = construct_vehicles(copy_gene, self.customers_params, self.depots_params)
              curr_path_length = get_path_length(vehicles, self.depots_params, self.customers_params, self.num_vehicles)

              if curr_path_length < best_path_length:
                  best_path_length = curr_path_length
                  best_location = (i, index)
              copy_gene[i].remove(customer_to_move)
      copy_gene[best_location[0]].insert(best_location[1], customer_to_move)
      return copy_gene

    def inter_depot_mutation(self, gene, borderline):
      #lengths = get_vehicle_lengths(gene)
      selected_depot = randint(0, len(borderline)-1) #the depot we want to move something into

      if len(borderline[selected_depot]) == 0: #Nothing to mutate
          return gene, borderline

      selected_customer_index = randint(0, len(borderline[selected_depot])-1)
      selected_customer = borderline[selected_depot][selected_customer_index] #e.g. 5
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
      b = 0.01
      #c = 100

      # depots_fitness = 0
      # for i in range(len(self.gene)):
      #   depots_fitness += self.get_depot_fitness(self.gene[i], i)

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

