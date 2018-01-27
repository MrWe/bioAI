class Population():
  def __init__(self, customers_params, depots_params, vehicle_max_load, vehicle_max_duration):
    self.customers_params = customers_params
    self.depots_params = depots_params
    self.vehicle_max_load = vehicle_max_load
    self.vehicle_max_duration = vehicle_max_duration
    self.construct_population()

  def construct_population(self):
    pass

