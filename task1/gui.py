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

    for coord in customers:
      pygame.draw.rect(self.screen, CUSTOMER_COLOR , pygame.Rect(coord[0], coord[1], CUSTOMER_SIZE, CUSTOMER_SIZE))

    for coord in depots:
      pygame.draw.rect(self.screen, DEPOT_COLOR , pygame.Rect(coord[0], coord[1], DEPOT_SIZE, DEPOT_SIZE))

    pygame.display.flip()

