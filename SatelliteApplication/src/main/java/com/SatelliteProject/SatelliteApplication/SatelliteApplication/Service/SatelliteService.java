package com.SatelliteProject.SatelliteApplication.SatelliteApplication.Service;

import java.util.List;
import com.SatelliteProject.SatelliteApplication.SatelliteApplication.model.Satellite;

public interface SatelliteService {

	public List<Satellite> dumpData();

	public Satellite createSatellite(Satellite satellite);

	public Satellite getSatelliteById(String satelliteId);

	public List<Satellite> getAllSatellites();

	public Satellite updateSatellite(String satelliteId, Satellite satellite);

	public void deleteSatelliteId(String satelliteId);

	public List<Satellite> searchSatellites(String id, String launchDate, String country, String launcher, String mass);

}
