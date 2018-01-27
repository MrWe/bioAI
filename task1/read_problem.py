from sklearn.preprocessing import scale
from customer import Customer
from vehicle import Vehicle
from depot import Depot
from config import *


def read(f):
  file_object = open('DataFiles/' + f, 'r')
  lines = file_object.readlines()

  t = lines[0].split(' ')
  num_vehicles, num_customers, num_depots = int(t[0]), int(t[1]), int(t[2])

  vehicle_max_duration = []
  vehicle_max_load = []

  for j in range(1, num_depots+1):
    d, l = lines[j].split(' ')
    vehicle_max_duration.append(int(d))
    vehicle_max_load.append(int(l))


  customers = []
  for j in range(1 + num_depots, num_customers + num_depots + 1):
    i, x, y, d, q = ' '.join(lines[j].split()).split(' ')[:5]
    customers.append(Customer(int(i), int(x), int(y), int(d), int(q)))



  depots = []
  for j in range(num_customers + num_depots + 1, len(lines)):
    i, x, y = ' '.join(lines[j].split()).split(' ')[:3]
    depots.append(Depot(int(x), int(y)))


  max_x, max_y = find_max_coords(customers, depots)

  max_value = max(max_x, max_y)
  scale_coordinates(customers, max_value)
  scale_coordinates(depots, max_value)


  return customers, depots


def find_max_coords(customers, depots):
  max_x = 0
  max_y = 0
  for i in customers:
    if(i.coords[0] > max_x):
      max_x = i.coords[0]
    if(i.coords[1] > max_y):
      max_y = i.coords[1]
  for i in depots:
    if(i.coords[0] > max_x):
      max_x = i.coords[0]
    if(i.coords[1] > max_y):
      max_y = i.coords[1]
  return max_x, max_y


def scale_coordinates(entities_to_scale, max_value):
  for i in entities_to_scale:
    i.scaled_coords[0] = (i.coords[0] / max_value) * SCREEN_SIZE[0]
    i.scaled_coords[1] = (i.coords[1] / max_value) * SCREEN_SIZE[1]

