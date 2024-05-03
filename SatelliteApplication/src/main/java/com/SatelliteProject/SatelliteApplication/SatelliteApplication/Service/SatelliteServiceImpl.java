package com.SatelliteProject.SatelliteApplication.SatelliteApplication.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.SatelliteProject.SatelliteApplication.SatelliteApplication.Repository.SatelliteRepository;
import com.SatelliteProject.SatelliteApplication.SatelliteApplication.model.Satellite;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SatelliteServiceImpl implements SatelliteService {

	@Autowired
	SatelliteRepository satelliteRepository;

	@Override
	public List<Satellite> dumpData() {
		String apiBaseUrl = "https://isro.vercel.app/api/customer_satellites";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map<String, List<Satellite>>> responseEntity = restTemplate.exchange(apiBaseUrl, HttpMethod.GET,
				null, new ParameterizedTypeReference<Map<String, List<Satellite>>>() {
				});
		List<Satellite> satellites = new ArrayList<>();
		Map<String, List<Satellite>> responseBody = responseEntity.getBody();
		if (responseBody != null && responseBody.containsKey("customer_satellites")) {
			satellites = responseBody.get("customer_satellites");
			satelliteRepository.saveAll(satellites);
			return satellites;
		}
		return satellites;
	}

	public Satellite createSatellite(Satellite satellite) {
		log.info("Creating satellite: {}", satellite);
		try {
			return satelliteRepository.save(satellite);
		} catch (Exception e) {
			log.error("Failed to create satellite: {}", e.getMessage());
			throw new RuntimeException("Failed to create satellite", e);
		}
	}

	public Satellite getSatelliteById(String satelliteId) {
		log.info("Fetching satellite with ID: {}", satelliteId);
		return satelliteRepository.findById(satelliteId).orElse(null);
	}

	@Override
	public List<Satellite> getAllSatellites() {
		log.info("Fetching All satellites: {}");
		return satelliteRepository.findAll();
	}

	public Satellite updateSatellite(String satelliteId, Satellite satellite) {
		log.info("Updating satellite with ID: {}", satelliteId);
		Satellite existingSatellite = getSatelliteById(satelliteId);
		if (existingSatellite == null) {
			log.warn("Satellite with ID: {} not found", satelliteId);
			throw new RuntimeException("Satellite with ID not found" + satelliteId);
		}
		BeanUtils.copyProperties(satellite, existingSatellite);
		return satelliteRepository.save(existingSatellite);
	}

	public void deleteSatelliteId(String satelliteId) {
		log.info("Deleting satellite with ID: {}", satelliteId);
		if (satelliteRepository.existsById(satelliteId)) {
			satelliteRepository.deleteById(satelliteId);
			log.info("Satellite deleted successfully");
		} else {
			log.error("Failed to delete satellite with ID: {}", satelliteId);
			throw new RuntimeException("Failed to delete satellite with ID: " + satelliteId);
		}
	}

	@Override
	public List<Satellite> searchSatellites(String id, String launchDate, String country, String launcher, String mass) {
	    log.info("Searching satellites...");
	    try {
	        Satellite searchSatellite = new Satellite();
	        searchSatellite.setId(id);
	        searchSatellite.setLaunch_date(launchDate);
	        searchSatellite.setCountry(country);
	        searchSatellite.setLauncher(launcher);
	        searchSatellite.setMass(mass);
	        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
	                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase()
	                .withMatcher("launchDate", ExampleMatcher.GenericPropertyMatchers.exact());
	        Example<Satellite> example = Example.of(searchSatellite, matcher);
	        List<Satellite> satellites = satelliteRepository.findAll(example);
	        log.info("Found " + satellites.size() + " satellites.");
	        return satellites;
	    } catch (Exception e) {
	        log.error("Error occurred while searching satellites: {}", e.getMessage());
	        throw new RuntimeException("Error occurred while searching satellites", e);
	    }
	}


}
