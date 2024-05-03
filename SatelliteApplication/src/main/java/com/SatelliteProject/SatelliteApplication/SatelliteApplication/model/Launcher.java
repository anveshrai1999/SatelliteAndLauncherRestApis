package com.SatelliteProject.SatelliteApplication.SatelliteApplication.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Launcher {

	@Id
	private String id;
	@Enumerated(EnumType.STRING)
	private LauncherType launchertype;
	private ZonedDateTime registeredOn;

	public enum LauncherType {
		NEW, OLD, DEGRADED
	}

}
