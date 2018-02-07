import math
import sys

#import pygame

from config import *
#from gui import GUI
from helpers import *
from population import Population
from read_problem import read

if(ENABLE_GUI):
  import pygame
  from gui import GUI
  gui = GUI()

def main(f):
  GUI_customers, GUI_depots, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles = read(f)

  m_rate = MUTATION_RATE

  nearest_customers, borderline = depot_cluster(depots_params, customers_params)

  population = Population(customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, m_rate)



  current_iteration = 1

  best_path_length = float("Inf")
  best_individual = None

  while(True):
    if(ENABLE_GUI):
      update_GUI(gui, current_iteration, population, GUI_customers, GUI_depots, True,  best_individual, m_rate)

    #path_length, individual = get_best_individual(population)
    individual = population.surviving_population[0]
    path_length = individual.path_length

    if(path_length < best_path_length):
      best_path_length = path_length
      print(current_iteration, best_path_length, m_rate)
      best_individual = individual

    population = Population(customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles, m_rate, population)

    m_rate *= MUTATION_RATE_DECAY
    current_iteration += 1


def update_GUI(gui, current_iteration, population, GUI_customers, GUI_depots, show_best_individual, best_individual, m_rate):
  if(should_update_gui(current_iteration)):
    print(current_iteration, m_rate)
    gui.screen = pygame.display.set_mode(SCREEN_SIZE)
    gui.screen.fill((255,255,255))
    gui.show_C_D(GUI_customers, GUI_depots)
    if(show_best_individual and best_individual is not None):
      gui.show_individual(best_individual, GUI_customers, GUI_depots)
    else:
      gui.show_population(population, GUI_customers, GUI_depots)
    pygame.display.update()


def should_update_gui(current_iteration):
  return current_iteration % GUI_UPDATE_INTERVAL == 0





if __name__ == '__main__':
  f = sys.argv[1] if len(sys.argv) > 1 else 'p01'
  main(f)
