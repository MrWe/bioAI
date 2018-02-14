import math
from random import random
from config import *


def get_best_individual(population):
  path_length = float("Inf")
  individual = None
  for i in range(len(population.individuals)):
    if(population.individuals[i].path_length < path_length):
      path_length = population.individuals[i].path_length
      individual = population.individuals[i]
  return path_length, individual

def depot_cluster(depots, customers, bound=0.1):
    bound = DISTANCE_BOUND
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


def get_path_length(vehicles, depots_params, customers_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers):
  path_length = 0
  lengths = []
  for i, depot in enumerate(vehicles):
    curr_depot_coords = (
    depots_params[i][0], depots_params[i][1])
    depot_path_length, vehicle_path_lengths = get_depot_path_length(depot, i, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
    path_length += depot_path_length
    lengths.append(vehicle_path_lengths)

  return path_length, lengths

def get_depot_path_length(vehicles, depot_index, customers, depots, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers):
  #vehicles = construct_route(depot, depot_index, customers, depots, num_vehicles)
  curr_depot_coords = (depots[depot_index][0], depots[depot_index][1])
  path_length = 0

  vehicle_path_lengths = []

  for vehicle in vehicles:
    vehicle_path_length = 0
    if(len(vehicle) > 1):
      for k, v in enumerate(vehicle[:-1]):
        v_next = vehicle[k+1]
        #path_length += euclideanDistance((customers[v][1], customers[v][2]), (customers[v_next][1], customers[v_next][2]))
        path_length += customer_2_customer[v][v_next]
        #vehicle_path_length += euclideanDistance((customers[v][1], customers[v][2]), (customers[v_next][1], customers[v_next][2]))
        vehicle_path_length += customer_2_customer[v][v_next]
      #From depot to first customer
      #vehicle_path_length += euclideanDistance(curr_depot_coords, (customers[vehicle[0]][1], customers[vehicle[0]][2]))
      vehicle_path_length += depots_2_customers[depot_index][vehicle[0]]
      #path_length += euclideanDistance(curr_depot_coords, (customers[vehicle[0]][1], customers[vehicle[0]][2]))
      path_length += depots_2_customers[depot_index][vehicle[0]]

      #From last customer to depot
      #vehicle_path_length += euclideanDistance((customers[vehicle[len(vehicle)-1]][1], customers[vehicle[len(vehicle)-1]][2]), curr_depot_coords)
      vehicle_path_length += customer_2_depots[vehicle[len(vehicle)-1]][depot_index]
      #path_length += euclideanDistance((customers[vehicle[len(vehicle)-1]][1], customers[vehicle[len(vehicle)-1]][2]), curr_depot_coords)
      path_length += customer_2_depots[vehicle[len(vehicle)-1]][depot_index]
      vehicle_path_lengths.append(vehicle_path_length)
    elif(len(vehicle) == 1):
      path_length += depots_2_customers[depot_index][vehicle[0]] * 2
      vehicle_path_lengths.append(depots_2_customers[depot_index][vehicle[0]] * 2)


  return path_length, vehicle_path_lengths

def get_route_load(vehicle, depots_params, customers_params):
  total_load = 0
  for n in range(len(vehicle)):
    total_load += customers_params[vehicle[n]][4]
  return total_load


def construct_vehicles(gene, customers, depots, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers):
  vehicles = []
  for i, depot in enumerate(gene):
    curr_route = []
    route_load_cost = 0
    depot_max_load = depots[i][3]
    depot_vehicles = []
    vehicles.append(construct_route(depot, i, customers,depots, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers))

  return vehicles

def randint(start, stop):
  return int(start+random()*(stop-start))

def construct_route(depot, depot_index, customers, depots, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers):

  vehicles = []
  depot_max_load = depots[depot_index][3]
  route_load_cost = 0
  route_load_costs = []
  route_max_duration = depots[depot_index][2] if depots[depot_index][2] != 0 else float("Inf")


  curr_route = []

  for customer_index in depot:
    can_carry_load = route_load_cost + customers[customer_index][4] <= depot_max_load

    if(can_carry_load):
      curr_route.append(customer_index)
      route_load_cost += customers[customer_index][4]

      if(get_depot_path_length([curr_route], depot_index, customers, depots, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)[0] > route_max_duration):
        last_customer = curr_route.pop()
        vehicles.append(curr_route)
        curr_route = [last_customer]
        route_load_costs.append(route_load_cost)
        route_load_cost = customers[last_customer][4]

    else:
      vehicles.append(curr_route)
      curr_route = [customer_index]

      route_load_costs.append(route_load_cost)
      route_load_cost = customers[customer_index][4]

  if(len(curr_route) != 0):
    vehicles.append(curr_route)
    route_load_costs.append(route_load_cost)

  # if len(vehicles) > num_vehicles: #Too many vehicles were created
  #   return None
  #print(route_load_costs)
  #print(vehicles)

  length, lengths = get_path_length([vehicles], customers, depots, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
  lenghts = lengths[0]
  #print(lengths)

  for i in range(len(vehicles)-1):
      if len(vehicles[i]) == 0:
          continue
      customer_to_move = vehicles[i].pop()
      vehicles[i+1].insert(0, customer_to_move)

      if route_load_costs[i+1] + customers[customer_to_move][4] > depot_max_load:
          #print("Cannot move:(")
          customer_to_move_back = vehicles[i+1].pop(0)
          vehicles[i].append(customer_to_move_back)
      else:
          new_length, new_lengths = get_path_length([vehicles], customers, depots, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
          if new_length > length:
              #print("Was not shorter buu")
              customer_to_move_back = vehicles[i+1].pop(0)
              vehicles[i].append(customer_to_move_back)
          else:
              #print("Found better path woo")
              length, lengths = new_length, new_lengths[0]
  return vehicles

'''
In Phase 2, the last customer of each route ri,
is relocated to become the first cus- tomer to route ri+1 .
If this removal and insertion maintains feasibility for route ri+1,
and the sum of costs of ri and ri+1 at Phase 2 is less than the sum of costs of ri +ri+1
at Phase 1, the routing configuration at Phase 2 is accepted,
otherwise the route network before Phase 2 (that is, at Phase 1) is maintained.


'''

def construct_child_gene(parent_gene1, parent_gene2, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers):

  copy_p1 = [x[:] for x in parent_gene1]
  copy_p2 = [x[:] for x in parent_gene2]

  rand_depot = randint(0, len(depots_params))

  routes_p1 = construct_route(parent_gene1[rand_depot], rand_depot, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
  routes_p2 = construct_route(parent_gene2[rand_depot], rand_depot, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)

  if(len(routes_p1) == 0):
    return parent_gene2
  if(len(routes_p2) == 0):
    return parent_gene1

  rand_route_p1 = routes_p1[randint(0, len(routes_p1))]
  rand_route_p2 = routes_p2[randint(0, len(routes_p2))]

  for i in rand_route_p1:
    for j in range(len(copy_p2)-1, -1, -1):
      if(i in copy_p2[j]):
        copy_p2[j].remove(i)


  for i in rand_route_p2:
    for j in range(len(copy_p1)-1, -1, -1):
        if(i in copy_p1[j]):
          copy_p1[j].remove(i)


  child1 = crossover(copy_p2, rand_route_p1, rand_depot, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
  child2 = crossover(copy_p1, rand_route_p2, rand_depot, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)

  return child1, child2

def crossover(parent, rand_route, rand_depot, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers):
  done = False
  if(random() < 0.8):
    for i in rand_route:
      for j, _ in enumerate(parent[rand_depot]):
        temp_depot_copy = parent[rand_depot][:]
        temp_depot_copy.insert(j, i)
        if construct_route(temp_depot_copy, rand_depot, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers) != None: #Route with customer i in location j was is_valid
          parent[rand_depot] = temp_depot_copy[:]
          break
  else:
    for i in rand_route:
      valids = []
      for j, _ in enumerate(parent[rand_depot]):
        temp_depot_copy = parent[rand_depot][:]
        temp_depot_copy.insert(j, i)
        r = construct_route(temp_depot_copy, rand_depot, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)
        if r != None: #Route with customer i in location j was is_valid
          valids.append([temp_depot_copy, r])

      best_depot_length = float("Inf")
      best_depot_index = 0
      for i, v in enumerate(valids):
        vehicles = v[1]
        curr_length = get_depot_path_length(vehicles, rand_depot, customers_params, depots_params, num_vehicles, customer_2_customer, customer_2_depots, depots_2_customers)[0]
        if(curr_length < best_depot_length):
          best_depot_length = curr_length
          best_depot_index = i
      # if(len(valids) == 0):
      #   return None #No valid options, returning default
      parent[rand_depot] = valids[best_depot_index][0]

  return parent




#depot = depot index in gene
#vehicle = vehicle index in depot
def enhance_vehicle_path(gene, depot, vehicle, depots_params, customers_params):
    pass
