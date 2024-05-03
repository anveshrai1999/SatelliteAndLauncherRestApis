package com.SatelliteProject.SatelliteApplication.SatelliteApplication.Controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SatelliteProject.SatelliteApplication.SatelliteApplication.Service.LauncherService;
import com.SatelliteProject.SatelliteApplication.SatelliteApplication.model.Launcher;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/launchers")
public class LauncherController {

	@Autowired
	private LauncherService launcherService;

	@PostMapping("/dump-data")
	public ResponseEntity<String> dumpData() {
		try {
			log.info("Data dump started for launchers");
			launcherService.dumpData();
			return ResponseEntity.status(HttpStatus.OK).body("Launcher Data Dump Successful");
		} catch (Exception e) {
			log.error("Failed to dump launcher data: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to dump launcher data: " + e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> createLauncher(@RequestBody Launcher launcher) {
		log.info("POST /api/launchers");
		try {
			Launcher createdLauncher = launcherService.createLauncher(launcher);
			return new ResponseEntity<Launcher>(createdLauncher, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Failed to create launcher: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/{launcherId}")
	public ResponseEntity<?> getLauncherById(@PathVariable String launcherId) {
		log.info("GET /api/launchers/{}", launcherId);
		Launcher launcher = launcherService.getLauncherById(launcherId);
		if (Objects.isNull(launcher)) {
			log.warn("Launcher with ID {} not found", launcherId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Launcher with ID " + launcherId + " not found");
		}
		return ResponseEntity.ok(launcher);
	}

	@GetMapping
	public ResponseEntity<?> getAllLaunchers() {
		log.info("GET /api/launchers");
		List<Launcher> launchers = launcherService.getAllLaunchers();
		if (CollectionUtils.isEmpty(launchers)) {
			log.warn("Launchers not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Launchers not found");
		}
		return ResponseEntity.ok(launchers);
	}

	@PutMapping
	public ResponseEntity<?> updateLauncher(@RequestBody Launcher launcher) {
		log.info("PUT /api/launchers");
		String launcherId = launcher.getId();
		if (launcherId == null) {
			log.error("Launcher ID is required for update");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Launcher ID is required for update");
		}
		try {
			launcherService.updateLauncher(launcherId, launcher);
			return ResponseEntity.status(HttpStatus.OK).body("Launcher updated successfully");
		} catch (Exception e) {
			log.error("Failed to update launcher: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update launcher");
		}
	}

	@DeleteMapping("/{launcherId}")
	public ResponseEntity<String> deleteLauncher(@PathVariable String launcherId) {
		log.info("DELETE /api/launchers/{}", launcherId);
		try {
			launcherService.deleteLauncherId(launcherId);
			return ResponseEntity.ok("Launcher deleted successfully");
		} catch (Exception e) {
			log.error("Failed to delete launcher: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete launcher: " + e.getMessage());
		}
	}
}
