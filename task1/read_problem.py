from config import *


def read(f):
  file_object = open('DataFiles/' + f, 'r')
  lines = file_object.readlines()

  solution_object = open('SolutionFiles/' + f + '.res', 'r')
  solution_lines = solution_object.readlines()

  t = lines[0].split(' ')
  num_vehicles, num_customers, num_depots = int(t[0]), int(t[1]), int(t[2])


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

  counter = 0
  for j in range(1, num_depots+1):
    d, l = lines[j].split(' ')
    depots_params[counter].append(int(d))
    depots_params[counter].append(int(l))
    counter += 1

  min_x, min_y, max_x, max_y = find_minmax_coords(GUI_customers, GUI_depots)

  scale = max(max_x - min_x,
            max_y - min_y)
  scale_coordinates(GUI_customers, scale, min_x, max_x, min_y, max_y)
  scale_coordinates(GUI_depots, scale, min_x, max_x, min_y, max_y)

  optimal_path_length = solution_lines.pop(0)

  return GUI_customers, GUI_depots, customers_params, depots_params, num_vehicles, optimal_path_length


def find_minmax_coords(customers, depots):
  max_x = 0
  max_y = 0
  min_x = float("Inf")
  min_y = float("Inf")
  for coord in customers:
    if(coord[0] > max_x):
      max_x = coord[0]
    if(coord[1] > max_y):
      max_y = coord[1]
    if(coord[0] < min_x):
      min_x = coord[0]
    if(coord[1] < min_y):
      min_y = coord[1]
  for coord in depots:
    if(coord[0] > max_x):
      max_x = coord[0]
    if(coord[1] > max_y):
      max_y = coord[1]
    if(coord[0] < min_x):
      min_x = coord[0]
    if(coord[1] < min_y):
      min_y = coord[1]
  return min_x, min_y, max_x, max_y


def scale_coordinates(entities_to_scale, scale, min_x, max_x, min_y, max_y):
  for i in range(len(entities_to_scale)):

    entities_to_scale[i][0] -= (max_x + min_x) / 2
    entities_to_scale[i][1] -= (max_y + min_y) / 2

    entities_to_scale[i][0] /= scale
    entities_to_scale[i][1] /= scale

    entities_to_scale[i][0] += 0.51
    entities_to_scale[i][1] += 0.51

    entities_to_scale[i][0] *= SCREEN_SIZE[0] * 0.95
    entities_to_scale[i][1] *= SCREEN_SIZE[1] * 0.95




