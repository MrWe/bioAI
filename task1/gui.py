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

  def run_MDVRP_pygame(self, customers, depots):
    self.screen = pygame.display.set_mode(SCREEN_SIZE)

    for i in range(len(customers)):
      c_coords = customers[i].scaled_coords
      pygame.draw.rect(self.screen, CUSTOMER_COLOR , pygame.Rect(c_coords[0], c_coords[1], CUSTOMER_SIZE, CUSTOMER_SIZE))

    for i in range(len(depots)):
      c_coords = depots[i].scaled_coords
      pygame.draw.rect(self.screen, DEPOT_COLOR , pygame.Rect(c_coords[0], c_coords[1], DEPOT_SIZE, DEPOT_SIZE))

    pygame.display.flip()

