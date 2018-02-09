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

def get_path_length(gene, depots_params, customers_params):
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

def get_vehicle_lengths(gene):
  lengths = []
  for depot in gene:
      for vehicle in depot:
          lengths.append(len(vehicle))
  return lengths  # lengths = [4, 3, 4, 0, 2, 4, 3, 0, ...]


#depot = depot index in gene
#vehicle = vehicle index in depot
def enhance_vehicle_path(gene, depot, vehicle, depots_params, customers_params):
    pass
