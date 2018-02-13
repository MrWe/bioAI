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


def main(f):
  GUI_customers, GUI_depots, customers_params, depots_params, num_vehicles, optimal_path_length = read(f)
  if(ENABLE_GUI):
    gui = GUI(depots_params, num_vehicles)
  m_rate = MUTATION_RATE

  nearest_customers, borderline = depot_cluster(depots_params, customers_params)

  population = Population(customers_params, depots_params, num_vehicles, m_rate, nearest_customers, borderline)

  current_iteration = 1



  best_path_length = float("Inf")
  best_individual = None

  while(True):
    if(ENABLE_GUI):
      update_GUI(gui, current_iteration, population, GUI_customers, GUI_depots, True,  best_individual, num_vehicles)

    #path_length, individual = get_best_individual(population)
    individual = population.individuals[0]
    if(individual != None):
      path_length = individual.path_length

    if(path_length < best_path_length):
      best_path_length = path_length
      best_individual = individual
      percentile = 1-float(optimal_path_length) / best_path_length
      print("Iteration", current_iteration, "  Path length", round(best_path_length, 6), "  Mutation rate", round(m_rate, 5), "  Percentile:", 100*round(percentile,3), "%")

      if percentile <= 0.05:
          print("Yay found great path wow")
          results = best_individual.get_results()

          print(results)
          return results


    population = Population(customers_params, depots_params, num_vehicles, m_rate, nearest_customers, borderline, population)

    m_rate *= MUTATION_RATE_DECAY
    current_iteration += 1
    #if(current_iteration % 1000 == 0):
      #print(flatten(best_individual.gene))
      #print(len(flatten(best_individual.gene)))



def update_GUI(gui, current_iteration, population, GUI_customers, GUI_depots, show_best_individual, best_individual, num_vehicles):
  if(should_update_gui(current_iteration)):
    gui.screen = pygame.display.set_mode(SCREEN_SIZE)
    gui.screen.fill((255,255,255))
    gui.show_C_D(GUI_customers, GUI_depots)
    if(show_best_individual and best_individual is not None):
      gui.show_individual(best_individual, GUI_customers, GUI_depots, num_vehicles)
    # else:
    #   gui.show_population(population, GUI_customers, GUI_depots)
    pygame.display.update()


def should_update_gui(current_iteration):
  return current_iteration % GUI_UPDATE_INTERVAL == 0





if __name__ == '__main__':
  f = sys.argv[1] if len(sys.argv) > 1 else 'p01'
  main(f)
