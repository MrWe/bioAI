import sys
from read_problem import read
from gui import GUI
import pygame
from config import *

def main(f):
  customers, depots = read(f)
  gui = GUI()

  current_iteration = 1

  while(True):
    if(should_update_gui(current_iteration)):
      gui.run_MDVRP_pygame(customers, depots)

    current_iteration += 1


def should_update_gui(current_iteration):
  return current_iteration % GUI_UPDATE_INTERVAL == 0


if __name__ == '__main__':
  f = sys.argv[1] if len(sys.argv) > 1 else 'p01'
  main(f)
