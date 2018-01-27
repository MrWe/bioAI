import sys
from read_problem import read

def main(f):
  vehicles, customers, depots = read(f)


if __name__ == '__main__':
  f = sys.argv[1] if len(sys.argv) > 1 else 'p01'
  main(f)