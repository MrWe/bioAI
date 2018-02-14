from random import random, randint, uniform
import main

'''
SCREEN_SIZE = (800, 800)

CUSTOMER_COLOR = (255,0,0)
DEPOT_COLOR = (0, 0, 255)

CUSTOMER_SIZE = 5
DEPOT_SIZE = 10

GUI_UPDATE_INTERVAL = 20

ENABLE_GUI = False

POPULATION_SIZE = 30


MUTATION_RATE = 0.03
MUTATION_RATE_DECAY = 0.999
DISTANCE_BOUND = 0.03

'''


best_pop_size = 0
best_m_rate = 0
best_m_rate_decay = 0
best_dist_bound = 0

best_path_length = float("Inf")


while(True):

  pop_size = randint(10, 60)
  m_rate = uniform(0.0001, 0.1)
  m_rate_decay = uniform(0.9, 0.9999)
  dist_bound = uniform(0.001, 0.1)



  with open('config.py', 'w') as f:
    f.write("SCREEN_SIZE = (800, 800)"+ '\n')
    f.write("CUSTOMER_COLOR = (255,0,0)"+ '\n')
    f.write("DEPOT_COLOR = (0, 0, 255)"+ '\n')
    f.write("CUSTOMER_SIZE = 5"+ '\n')
    f.write("DEPOT_SIZE = 10"+ '\n')
    f.write("GUI_UPDATE_INTERVAL = 20"+ '\n')
    f.write("ENABLE_GUI = False"+ '\n')

    f.write("POPULATION_SIZE = " + str(pop_size) + '\n')

    f.write("MUTATION_RATE = "+ str(m_rate)+ '\n')
    f.write("MUTATION_RATE_DECAY = " + str(m_rate_decay)+ '\n')
    f.write("DISTANCE_BOUND = " + str(dist_bound)+ '\n')
    curr_length = main.main("p05")
  if(curr_length < best_path_length):
    best_path_length = curr_length
    print(curr_length)
    print("POPULATION_SIZE = " + str(pop_size) + '\n')

    print("MUTATION_RATE = "+ str(m_rate)+ '\n')
    print("MUTATION_RATE_DECAY = " + str(m_rate_decay)+ '\n')
    print("DISTANCE_BOUND = " + str(dist_bound)+ '\n')
