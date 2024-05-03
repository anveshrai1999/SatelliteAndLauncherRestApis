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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.SatelliteProject.SatelliteApplication.SatelliteApplication.Repository.SatelliteRepository;
import com.SatelliteProject.SatelliteApplication.SatelliteApplication.Service.SatelliteService;
import com.SatelliteProject.SatelliteApplication.SatelliteApplication.model.Satellite;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/satellites")
public class SatelliteController {

	@Autowired
	SatelliteRepository satelliteRepository;

	@Autowired
	SatelliteService satelliteService;

	@PostMapping("/dump-data")
	public ResponseEntity<String> dumpData() {
		try {
			log.info("Data dump started");
			List<Satellite> satellites = satelliteService.dumpData();
			if (!CollectionUtils.isEmpty(satellites)) {
				return ResponseEntity.status(HttpStatus.OK).body("Satellite Data Dump Successfull");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Satellite Data Not Found,Dump Failed");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to dump data: " + e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> createSatellite(@RequestBody Satellite satellite) {
		log.info("POST /api/satellite");
		try {
			Satellite createdSatellite = satelliteService.createSatellite(satellite);
			return new ResponseEntity<Satellite>(createdSatellite, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Failed to create satellite: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/{satelliteId}")
	public ResponseEntity<?> getSatelliteById(@PathVariable String satelliteId) {
		log.info("GET /api/satellite/{}", satelliteId);
		Satellite satellite = satelliteService.getSatelliteById(satelliteId);
		if (Objects.isNull(satellite)) {
			log.warn("Satellite with ID {} not found", satellite);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Satellite with ID " + satelliteId + " not found");
		}
		return ResponseEntity.ok(satellite);
	}

	@GetMapping
	public ResponseEntity<?> getAllSatellite() {
		log.info("GET /api/satellite");
		List<Satellite> satellites = satelliteService.getAllSatellites();
		if (CollectionUtils.isEmpty(satellites)) {
			log.warn("Satellites not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Satellites not found");
		}
		return ResponseEntity.ok(satellites);
	}

	@PutMapping
	public ResponseEntity<?> updateSatellite(@RequestBody Satellite satellite) {
		log.info("PUT /api/satellite");
		String satelliteId = satellite.getId();
		if (satelliteId == null) {
			log.error("Satellite ID is required for update");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Satellite ID is required for update");
		}
		try {
			satelliteService.updateSatellite(satelliteId, satellite);
			return ResponseEntity.status(HttpStatus.OK).body("Satellite updated successfully");
		} catch (Exception e) {
			log.error("Failed to update satellite: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update satellite");
		}
	}

	@DeleteMapping("/{satelliteId}")
	public ResponseEntity<String> deleteSatellite(@PathVariable String satelliteId) {
		log.info("DELETE /api/sarellite/{}", satelliteId);
		try {
			satelliteService.deleteSatelliteId(satelliteId);
			return ResponseEntity.ok("Satellite deleted successfully");
		} catch (Exception e) {
			log.error("Failed to delete Satellite: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete Satellite: " + e.getMessage());
		}
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchSatellites(@RequestParam(required = true) String id,
			@RequestParam(required = true) String launchDate, @RequestParam(required = true) String country,
			@RequestParam(required = true) String launcher, @RequestParam(required = false) String mass) {
		try {
			log.info("Searching satellites...");
			List<Satellite> result = satelliteService.searchSatellites(id, launchDate, country, launcher, mass);
			if (CollectionUtils.isEmpty(result)) {
				log.info("Satellites not found with search query");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Satellites not found with search query");
			}
			log.info("Found " + result.size() + " satellites");
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			log.error("Failed to search satellites: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search satellites");
		}
	}

}
