import math
from random import randint, random

def get_best_individual(population):
  path_length = float("Inf")
  individual = None
  for i in range(len(population.individuals)):
    if(population.individuals[i].path_length < path_length):
      path_length = population.individuals[i].path_length
      individual = population.individuals[i]
  return path_length, individual

def depot_cluster(depots, customers, bound=2):
    nearest_customers = [[] for _ in depots]
    borderline = [[] for _ in depots]

    for i in range(len(customers)):
        nearest_depot, nearest_depot_dist = get_nearest_depot(depots, customers[i]) # e.g. (1, 156.2)
        nearest_customers[nearest_depot].append(i)

        borderline_depots = get_borderline_depots(depots, customers[i], nearest_depot, nearest_depot_dist, bound) # e.g. [2, 4, 5]

        for j in borderline_depots:
            borderline[j].append(i)

    return nearest_customers, borderline

def get_nearest_depot(depots, customer):
    nearest = None
    nearest_dist = float("Inf")

    for i in range(len(depots)):
        dist = euclideanDistance(depots[i], (customer[1], customer[2]))
        if dist < nearest_dist:
            nearest = i
            nearest_dist = dist

    return nearest, nearest_dist

def get_borderline_depots(depots, customer, nearest_depot, nearest_depot_dist, bound):
    borderline_depots = []

    for i in range(len(depots)):
        if i != nearest_depot:
            borderline_dist = euclideanDistance(depots[i], (customer[1], customer[2]))

            if (borderline_dist - nearest_depot_dist) / nearest_depot_dist <= bound:
                borderline_depots.append(i)

    return borderline_depots

def euclideanDistance(coordinate1, coordinate2):
  return pow(pow(coordinate1[0] - coordinate2[0], 2) + pow(coordinate1[1] - coordinate2[1], 2), .5)

def flatten(array):
    return [item for sublist in array for item in sublist]


def get_path_length(vehicles, depots_params, customers_params, num_vehicles):


  path_length = 0
  for i, depot in enumerate(vehicles):
    curr_depot_coords = (
    depots_params[i][0], depots_params[i][1])
    path_length += get_depot_path_length(depot, i, customers_params, depots_params, num_vehicles)

  return path_length

def get_depot_path_length(vehicles, depot_index, customers, depots, num_vehicles):
  #vehicles = construct_route(depot, depot_index, customers, depots, num_vehicles)
  curr_depot_coords = (depots[depot_index][0], depots[depot_index][1])
  path_length = 0

  for vehicle in vehicles:
    for k, v in enumerate(vehicle[:-1]):
      v = vehicle[k]
      v_next = vehicle[k+1]
      path_length += euclideanDistance((customers[v][1], customers[v][2]), (customers[v_next][1], customers[v_next][2]))
    path_length += euclideanDistance(curr_depot_coords, (customers[vehicle[0]][1], customers[vehicle[0]][2]))
    path_length += euclideanDistance((customers[vehicle[len(vehicle)-1]][1], customers[vehicle[len(vehicle)-1]][2]), curr_depot_coords)
  return path_length

def get_route_load(vehicle, depots_params, customers_params):
  total_load = 0
  for n in range(len(vehicle)):
    total_load += customers_params[vehicle[n]][4]
  return total_load

# def get_vehicle_lengths(gene):
#   lengths = []
#   for depot in gene:
#       for vehicle in depot:
#           lengths.append(len(vehicle))
#   return lengths  # lengths = [4, 3, 4, 0, 2, 4, 3, 0, ...]


#gene = [[1,2,3,4,5], [6,7,8,9], [10,11]]
def construct_vehicles(gene, customers, depots):
  vehicles = []
  for i, depot in enumerate(gene):
    curr_route = []
    route_load_cost = 0
    depot_max_load = depots[i][3]
    depot_vehicles = []
    for j, customer_index in enumerate(depot):
      if(route_load_cost + customers[customer_index][4] <= depot_max_load):
        curr_route.append(customer_index)
        route_load_cost += customers[customer_index][4]
      else:
        depot_vehicles.append(curr_route)
        curr_route = [customer_index]
        route_load_cost = customers[customer_index][4]
    if(len(curr_route) != 0):
      depot_vehicles.append(curr_route)
    vehicles.append(depot_vehicles)
  return vehicles

def construct_route(depot, depot_index, customers, depots, num_vehicles):

  vehicles = []
  depot_max_load = depots[depot_index][3]
  route_load_cost = 0
  curr_route = []
  for customer_index in depot:
    if(route_load_cost + customers[customer_index][4] <= depot_max_load):
      curr_route.append(customer_index)
      route_load_cost += customers[customer_index][4]
    else:
      vehicles.append(curr_route)
      curr_route = [customer_index]
      route_load_cost = customers[customer_index][4]
  if(len(curr_route) != 0):
    vehicles.append(curr_route)

  # if len(vehicles) > num_vehicles: #Too many vehicles were created
  #   return None

  return vehicles


def construct_child_gene(parent_gene1, parent_gene2, customers_params, depots_params, num_vehicles):

  copy_p1 = [x[:] for x in parent_gene1]
  copy_p2 = [x[:] for x in parent_gene2]

  rand_depot = randint(0, len(depots_params)-1)

  routes_p1 = construct_route(parent_gene1[rand_depot], rand_depot, customers_params, depots_params, num_vehicles)
  routes_p2 = construct_route(parent_gene2[rand_depot], rand_depot, customers_params, depots_params, num_vehicles)

  if(len(routes_p1) == 0):
    return parent_gene2
  if(len(routes_p2) == 0):
    return parent_gene1

  rand_route_p1 = routes_p1[randint(0, len(routes_p1)-1)]
  rand_route_p2 = routes_p2[randint(0, len(routes_p2)-1)]

  for i in rand_route_p1:
    for j in range(len(copy_p2)-1, -1, -1):
      for k in range(len(copy_p2[j])-1, -1, -1):
        if(copy_p2[j][k] == i):
          del copy_p2[j][k]

  for i in rand_route_p2:
    for j in range(len(copy_p1)-1, -1, -1):
      for k in range(len(copy_p1[j])-1, -1, -1):
        if(copy_p1[j][k] == i):
          del copy_p1[j][k]


  child1 = crossover(copy_p2, rand_route_p1, rand_depot, customers_params, depots_params, num_vehicles)
  child2 = crossover(copy_p1, rand_route_p2, rand_depot, customers_params, depots_params, num_vehicles)

  return child1, child2

def crossover(parent, rand_route, rand_depot, customers_params, depots_params, num_vehicles):
  if(random() < 0.8):
    for i in rand_route:
      for j, _ in enumerate(parent[rand_depot]):
        temp_depot_copy = parent[rand_depot][:]
        temp_depot_copy.insert(j, i)
        if construct_route(temp_depot_copy, rand_depot, customers_params, depots_params, num_vehicles) != None: #Route with customer i in location j was is_valid
          parent[rand_depot] = temp_depot_copy[:]
          break
  else:
    for i in rand_route:
      valids = []
      for j, _ in enumerate(parent[rand_depot]):
        temp_depot_copy = parent[rand_depot][:]
        temp_depot_copy.insert(j, i)
        if construct_route(temp_depot_copy, rand_depot, customers_params, depots_params, num_vehicles) != None: #Route with customer i in location j was is_valid
          valids.append(temp_depot_copy)

      best_depot_length = float("Inf")
      best_depot_index = 0
      for i, v in enumerate(valids):
        vehicles = construct_route(v, rand_depot, customers_params, depots_params, num_vehicles)
        curr_length = get_depot_path_length(vehicles, rand_depot, customers_params, depots_params, num_vehicles)
        if(curr_length < best_depot_length):
          best_depot_length = curr_length
          best_depot_index = i
      if(len(valids) == 0):
        return None #No valid options, returning default
      parent[rand_depot] = valids[best_depot_index]

  return parent




#depot = depot index in gene
#vehicle = vehicle index in depot
def enhance_vehicle_path(gene, depot, vehicle, depots_params, customers_params):
    pass
