package in.thekreml.rentit.data;

import java.util.HashSet;
import java.util.Set;

public class DataModel {
  private Set<Device> devices = new HashSet<>();

  public Set<Device> getDevices() {
    return devices;
  }

  public void setDevices(Set<Device> devices) {
    this.devices = devices;
  }
}
