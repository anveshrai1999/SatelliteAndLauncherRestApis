package com.SatelliteProject.SatelliteApplication.SatelliteApplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Satellite {
	@Id
	private String id;
	private String country;
	private String launch_date;
	private String mass;
	private String launcher;
}
