import sys
from read_problem import read, read_result_file
import pygame
from gui import GUI
from helpers import *
from config import *


DEPOT = 0
VEHICLE_IN_DEPOT = 1
DURATION = 2
LOAD = 3
ROUTE_START = 4

def main(f, optimal):
  GUI_customers, GUI_depots, customers_params, depots_params, num_vehicles, _ = read(f)
  result_lines = read_result_file(f, optimal)

  vehicles = build_gene_from_result(result_lines, customers_params, depots_params, num_vehicles)

  gui = GUI(depots_params, num_vehicles)

  while(True):
    update_GUI(gui, GUI_customers, GUI_depots, vehicles, num_vehicles)

def build_gene_from_result(result_lines, customers_params, depots_params, num_vehicles):
  gene = [[[] for i in range(num_vehicles)]
                     for x in range(len(depots_params))]
  for line in result_lines:
    for customer in line[ROUTE_START+1:-1]:
      gene[line[DEPOT]-1][line[VEHICLE_IN_DEPOT]-1].append(customer-1)

  return gene

  #return construct_vehicles(gene, customers_params, depots_params, num_vehicles)


def update_GUI(gui, GUI_customers, GUI_depots, vehicles, num_vehicles):

  gui.screen = pygame.display.set_mode(SCREEN_SIZE)
  gui.screen.fill((255,255,255))
  gui.show_C_D(GUI_customers, GUI_depots)
  gui.show_gene(vehicles, GUI_customers, GUI_depots, num_vehicles)

  pygame.display.update()




if __name__ == '__main__':
  f = sys.argv[1]
  optimal = sys.argv[2] #True = optimal solution false = our solution
  main(f, optimal)