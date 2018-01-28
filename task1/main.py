import sys
from read_problem import read
from gui import GUI
import pygame
from config import *
from population import Population

def main(f):
  GUI_customers, GUI_depots, customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles = read(f)
  gui = GUI()
  initial_population = Population(customers_params, depots_params, vehicle_max_load, vehicle_max_duration, num_vehicles)

  current_iteration = 1

  while(True):
    update_GUI(gui, current_iteration, initial_population, GUI_customers, GUI_depots)
    current_iteration += 1


def update_GUI(gui, current_iteration, population, GUI_customers, GUI_depots):
  if(should_update_gui(current_iteration)):
    gui.screen = pygame.display.set_mode(SCREEN_SIZE)
    gui.show_C_D(GUI_customers, GUI_depots)
    gui.show_population(population, GUI_customers, GUI_depots)
    pygame.display.update()


def should_update_gui(current_iteration):
  return current_iteration % GUI_UPDATE_INTERVAL == 0


if __name__ == '__main__':
  f = sys.argv[1] if len(sys.argv) > 1 else 'p01'
  main(f)
