package com.SatelliteProject.SatelliteApplication.SatelliteApplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SatelliteProject.SatelliteApplication.SatelliteApplication.model.Launcher;

@Repository
public interface LauncherRepository extends JpaRepository<Launcher, String> {

}