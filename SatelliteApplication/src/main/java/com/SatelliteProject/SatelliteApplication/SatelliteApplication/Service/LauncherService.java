package com.SatelliteProject.SatelliteApplication.SatelliteApplication.Service;

import java.util.List;
import com.SatelliteProject.SatelliteApplication.SatelliteApplication.model.Launcher;

public interface LauncherService {

	public List<Launcher> dumpData();

	public Launcher createLauncher(Launcher launcher);

	public Launcher getLauncherById(String launcherId);

	public List<Launcher> getAllLaunchers();

	public Launcher updateLauncher(String launcherId, Launcher launcher);

	public void deleteLauncherId(String launcherId);

}
