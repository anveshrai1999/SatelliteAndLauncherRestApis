package com.SatelliteProject.SatelliteApplication.SatelliteApplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SatelliteProject.SatelliteApplication.SatelliteApplication.model.Satellite;

@Repository
public interface SatelliteRepository extends JpaRepository<Satellite, String> {

}
