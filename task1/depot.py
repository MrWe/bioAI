class Depot():
  def __init__(self, x, y):
    self.coords = (x, y)
    self.scaled_coords = [0,0]
    self.vehicles = []