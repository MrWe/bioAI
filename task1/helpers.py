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
