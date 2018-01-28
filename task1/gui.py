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
          path_color = individual.path_color[i + j]
          for n in range(len(individual.gene[i][j])-1):

            curr_cust = customers[individual.gene[i][j][n]]
            next_cust = customers[individual.gene[i][j][n+1]]


            curr_customer_coords = (curr_cust[0], curr_cust[1])
            next_customer_coords = (next_cust[0], next_cust[1])


            if(n == 0):
              self.draw_line(path_color, curr_depot_coords, curr_customer_coords)
            elif(n == len(individual.gene[i][j])-2):
              self.draw_line(path_color, next_customer_coords, curr_depot_coords)
            self.draw_line(path_color, curr_customer_coords, next_customer_coords)



  def draw_line(self, path_color, start, stop):
    pygame.draw.line(self.screen, path_color, start, stop, 1)







