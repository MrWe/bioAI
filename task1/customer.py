class Customer():
  def __init__(self, i, x, y, d, q):
    self.c_number = i
    self.coords = (x,y)
    self.service_duration = d
    self.demand = q
    self.scaled_coords = [0,0]
