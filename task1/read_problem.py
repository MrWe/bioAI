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

  GUI_customers = []
  customers_params = []
  for j in range(1 + num_depots, num_customers + num_depots + 1):
    i, x, y, d, q = ' '.join(lines[j].split()).split(' ')[:5]
    GUI_customers.append([int(x), int(y)])
    customers_params.append([int(i), int(x), int(y), int(d), int(q)])


  GUI_depots = []
  depots_params = []
  for j in range(num_customers + num_depots + 1, len(lines)):
    i, x, y = ' '.join(lines[j].split()).split(' ')[:3]
    GUI_depots.append([int(x), int(y)])
    depots_params.append([int(x), int(y)])


  max_x, max_y = find_max_coords(GUI_customers, GUI_depots)

  max_value = max(max_x, max_y)
  scale_coordinates(GUI_customers, max_value)
  scale_coordinates(GUI_depots, max_value)


  return GUI_customers, GUI_depots, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles


def find_max_coords(customers, depots):
  max_x = 0
  max_y = 0
  for coord in customers:
    if(coord[0] > max_x):
      max_x = coord[0]
    if(coord[1] > max_y):
      max_y = coord[1]
  for i in depots:
    if(coord[0] > max_x):
      max_x = coord[0]
    if(coord[1] > max_y):
      max_y = coord[1]
  return max_x, max_y


def scale_coordinates(entities_to_scale, max_value):
  for i in range(len(entities_to_scale)):
    entities_to_scale[i][0] = (entities_to_scale[i][0] / max_value) * (SCREEN_SIZE[0] - (SCREEN_SIZE[0] / 90))
    entities_to_scale[i][1] = (entities_to_scale[i][1] / max_value) * (SCREEN_SIZE[1] - (SCREEN_SIZE[1] / 90))

