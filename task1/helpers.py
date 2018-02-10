import math

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

#TODO: Combine both for-loops n and k into one
def get_path_length(gene, depots_params, customers_params):
  gene = construct_vehicles(gene, customers_params, depots_params)
  path_length = 0
  for i in range(len(gene)):
    curr_depot_coords = (
    depots_params[i][0], depots_params[i][1])
    for j in range(len(gene[i])):
      curr_vehicle = []
      for n in range(len(gene[i][j])):
        curr_vehicle.append((customers_params[gene[i][j][n]][1], customers_params[gene[i][j][n]][2]))
      curr_vehicle.insert(0, curr_depot_coords)
      curr_vehicle.append(curr_depot_coords)
      for k in range(len(curr_vehicle)-1):
        path_length += euclideanDistance(curr_vehicle[k], curr_vehicle[k+1])

  return path_length

def get_depot_path_length(depot, depot_index, customers, depots, num_vehicles):
  vehicles = construct_route(depot, depot_index, customers, depots, num_vehicles)
  curr_depot_coords = (depots[depot_index][0], depots[depot_index][1])
  path_length = 0

  for j in range(len(vehicles)):
    for k in range(len(vehicles[j])-1):
      v = vehicles[j][k]
      v_next = vehicles[j][k+1]
      path_length += euclideanDistance((customers[v][1], customers[v][2]), (customers[v_next][1], customers[v_next][2]))
    path_length += euclideanDistance(curr_depot_coords, (customers[vehicles[j][0]][1], customers[vehicles[j][0]][2]))
    path_length += euclideanDistance((customers[vehicles[j][len(vehicles[j])-1]][1], customers[vehicles[j][len(vehicles[j])-1]][2]), curr_depot_coords)
  return path_length

def get_route_load(vehicle, depots_params, customers_params):
  total_load = 0
  for n in range(len(vehicle)):
    total_load += customers_params[vehicle[n]][4]
  return total_load

def get_vehicle_lengths(gene):
  lengths = []
  for depot in gene:
      for vehicle in depot:
          lengths.append(len(vehicle))
  return lengths  # lengths = [4, 3, 4, 0, 2, 4, 3, 0, ...]


#gene = [[1,2,3,4,5], [6,7,8,9], [10,11]]
def construct_vehicles(gene, customers, depots):
  vehicles = []
  for i in range(len(gene)):
    curr_route = []
    route_load_cost = 0
    depot_max_load = depots[i][3]
    depot_vehicles = []
    for j in range(len(gene[i])):
      customer_index = gene[i][j]

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
  for i in range(len(depot)):
    if(route_load_cost + customers[depot[i]][4] <= depot_max_load):
      curr_route.append(depot[i])
      route_load_cost += customers[depot[i]][4]
    else:
      vehicles.append(curr_route)
      curr_route = [depot[i]]
      route_load_cost = customers[depot[i]][4]
  if(len(curr_route) != 0):
    vehicles.append(curr_route)

  # if len(vehicles) > num_vehicles: #Too many vehicles were created
  #   return None

  return vehicles







#depot = depot index in gene
#vehicle = vehicle index in depot
def enhance_vehicle_path(gene, depot, vehicle, depots_params, customers_params):
    pass
