from customer import Customer
from vehicle import Vehicle
from depot import Depot
from config import *


def read(f):
  file_object = open('DataFiles/' + f, 'r')
  lines = file_object.readlines()

  t = lines[0].split(' ')
  num_vehicles, num_customers, num_depots = int(t[0]), int(t[1]), int(t[2])

  vehicles = []
  for j in range(1, num_vehicles+1):
    d, l = lines[j].split(' ')
    vehicles.append(Vehicle(int(d), int(l)))

  customers = []
  for j in range(1 + num_vehicles, num_customers + num_vehicles + 1):
    i, x, y, d, q = ' '.join(lines[j].split()).split(' ')[:5]
    customers.append(Customer(int(i), int(x), int(y), int(d), int(q)))

  scale_coordinates(customers)

  depots = []
  for j in range(num_customers + num_vehicles + 1, len(lines)):
    i, x, y = ' '.join(lines[j].split()).split(' ')[:3]
    depots.append(Depot(int(x), int(y)))

  scale_coordinates(depots)

  return vehicles, customers, depots


def scale_coordinates(entities_to_scale):
  max_x = 0
  max_y = 0
  for i in entities_to_scale:
    if(i.coords[0] > max_x):
      max_x = i.coords[0]
    if(i.coords[1] > max_y):
      max_y = i.coords[1]

  for i in entities_to_scale:
    i.scaled_coords[0] = (i.coords[0] / max_x) * SCREEN_SIZE[0]
    i.scaled_coords[1] = (i.coords[1] / max_y) * SCREEN_SIZE[1]