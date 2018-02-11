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
        self.gene = nearest_customers
        self.borderline = borderline
        self.vehicles = construct_vehicles(self.gene, customers_params, depots_params)
        self.num_vehicles = num_vehicles


        self.mutation_rate = mutation_rate

        #self.valid = self.is_valid()

        self.path_length = get_path_length(self.vehicles, self.depots_params, self.customers_params, self.num_vehicles)
        self.fitness = self.get_fitness()
        return self

    def init_with_gene(self, customers_params, depots_params, num_vehicles, gene, mutation_rate, nearest_customers, borderline):
      self.customers_params = customers_params
      self.depots_params = depots_params
      #self.nearest_customers, self.borderline = depot_cluster(self.depots_params, self.customers_params)
      self.nearest_customers = nearest_customers
      self.borderline = borderline
      self.num_vehicles = num_vehicles
      self.gene = gene
      self.vehicles = construct_vehicles(self.gene, customers_params, depots_params)

      self.path_length = get_path_length(self.vehicles, self.depots_params, self.customers_params, self.num_vehicles)
      self.fitness = self.get_fitness()

      return self

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
      #if(r > 0.66):
        #self.reverse(depot)
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
        curr_gene = self.copy_gene(gene) #This hopefully works
        curr_depot = depot[:]
        curr_depot.insert(i, cust)
        curr_depot = self.construct_vehicles(curr_depot, vehicle_lengths[self.num_vehicles*rand_depot:(self.num_vehicles*rand_depot)+self.num_vehicles])
        curr_gene[rand_depot] = curr_depot
        path_length = get_path_length(curr_gene, self.depots_params, self.customers_params, self.num_vehicles)

        if(path_length < best_fitness):
          best_fitness = path_length
          best_gene = curr_gene

      best_gene = flatten(best_gene)
      best_gene = flatten(best_gene)
      return best_gene


    def inter_depot_mutation(self, gene):
      #lengths = get_vehicle_lengths(gene)
      selected_depot = randint(0, len(self.borderline)-1)

      if len(self.borderline[selected_depot]) == 0: #Nothing to mutate
          gene = flatten(gene)
          gene = flatten(gene)
          return gene

      selected_customer_index = randint(0, len(self.borderline[selected_depot])-1)
      selected_customer = self.borderline[selected_depot][selected_customer_index]
      del self.borderline[selected_depot][selected_customer_index] #we now have the item to move into our selected_depot

      customer_location = self.find(gene, selected_customer) #e.g. (2, 1, 0) -> depot 2, vehicle 1, index 0

      mutated_gene = self.copy_gene(gene)

      del mutated_gene[customer_location[0]][customer_location[1]][customer_location[2]] #best code thxbye

      self.borderline[customer_location[0]].append(selected_customer)

      vehicle_to_insert = randint(0, len(mutated_gene[selected_depot])-1)
      mutated_gene[selected_depot][vehicle_to_insert].append(selected_customer)

      mutated_gene = flatten(mutated_gene)
      mutated_gene = flatten(mutated_gene)

      return mutated_gene

    def find(self, searchList, elem):
        for i in range(len(searchList)):
            for j in range(len(searchList[i])):
                for k in range(len(searchList[i][j])):
                    if searchList[i][j][k] == elem:
                        return i,j,k

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
        new_depot = []
        for vehicle in depot:
          new_depot.append(vehicle[:])
        new_gene.append(new_depot)
      return new_gene

