class Vehicle():
  def __init__(self, max_duration, max_load):
    self.max_duration = max_duration
    self.max_load = max_load
    #Path contains customers
    self.path = []