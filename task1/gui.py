from __future__ import print_function, division, unicode_literals
from math import pi
from random import randint
import sys
import pygame
from time import sleep



pygame.init()
screen = pygame.display.set_mode((400, 300))
done = False

while not done:
  for event in pygame.event.get():
    if event.type == pygame.QUIT:
        done = True

  pygame.draw.rect(screen, (0, 128, 255), pygame.Rect(30, 30, 60, 60))

  pygame.display.flip()
