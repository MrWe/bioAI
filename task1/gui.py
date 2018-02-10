from __future__ import print_function, division, unicode_literals
from math import pi
from random import randint
import sys
import pygame
from time import sleep
from config import *


class GUI(object):
  def __init__(self):
    pygame.init()
    self.screen = pygame.display.set_mode(SCREEN_SIZE)


  def show_C_D(self, customers, depots):
    for coord in customers:
      pygame.draw.rect(self.screen, CUSTOMER_COLOR , pygame.Rect(coord[0], coord[1], CUSTOMER_SIZE, CUSTOMER_SIZE))

    for coord in depots:
      pygame.draw.rect(self.screen, DEPOT_COLOR , pygame.Rect(coord[0], coord[1], DEPOT_SIZE, DEPOT_SIZE))


  def show_population(self, population, customers, depots):
    for individual in population.individuals:
      for i in range(len(individual.gene)):
        curr_depot_coords = (depots[i][0], depots[i][1])
        for j in range(len(individual.gene[i])):
          curr_vehicle = []
          for n in range(len(individual.gene[i][j])):
            curr_vehicle.append((customers[individual.gene[i][j][n]][0], customers[individual.gene[i][j][n]][1]))
          curr_vehicle.insert(0, curr_depot_coords)
          curr_vehicle.append(curr_depot_coords)
          for k in range(len(curr_vehicle)-1):

            self.draw_line(individual.path_color[i + j], curr_vehicle[k], curr_vehicle[k+1])

  def show_individual(self, individual, customers, depots):
    for i in range(len(individual.vehicles)):
      curr_depot_coords = (depots[i][0], depots[i][1])
      for j in range(len(individual.vehicles[i])):
        curr_vehicle = []
        for n in range(len(individual.vehicles[i][j])):
          curr_vehicle.append((customers[individual.vehicles[i][j][n]][0], customers[individual.vehicles[i][j][n]][1]))
        curr_vehicle.insert(0, curr_depot_coords)
        curr_vehicle.append(curr_depot_coords)
        for k in range(len(curr_vehicle)-1):

          self.draw_line(individual.path_color[i + j], curr_vehicle[k], curr_vehicle[k+1])


  def draw_line(self, path_color, start, stop):
    pygame.draw.line(self.screen, path_color, start, stop, 1)







