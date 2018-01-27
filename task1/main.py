import sys
from read_problem import read
from gui import GUI
import pygame

def main(f):
  vehicles, customers, depots = read(f)
  gui = GUI()
  while(True):
    gui.run_MDVRP_pygame(vehicles, customers, depots)
    pygame.display.flip()


if __name__ == '__main__':
  f = sys.argv[1] if len(sys.argv) > 1 else 'p01'
  main(f)