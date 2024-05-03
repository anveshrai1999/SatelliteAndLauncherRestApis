package com.SatelliteProject.SatelliteApplication.SatelliteApplication.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.SatelliteProject.SatelliteApplication.SatelliteApplication.Repository.LauncherRepository;
import com.SatelliteProject.SatelliteApplication.SatelliteApplication.model.Launcher;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LauncherServiceImpl implements LauncherService {

	@Autowired
	LauncherRepository launcherRepository;

	@Override
	public List<Launcher> dumpData() {
		String apiBaseUrl = "https://isro.vercel.app/api/launchers";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map<String, List<Launcher>>> responseEntity = restTemplate.exchange(apiBaseUrl, HttpMethod.GET,
				null, new ParameterizedTypeReference<Map<String, List<Launcher>>>() {
				});
		List<Launcher> launchers = new ArrayList<>();
		Map<String, List<Launcher>> responseBody = responseEntity.getBody();
		if (responseBody != null && responseBody.containsKey("launchers")) {
			launchers = responseBody.get("launchers");
			launcherRepository.saveAll(launchers);
			return launchers;
		}
		return launchers;
	}

	public Launcher createLauncher(Launcher launcher) {
		log.info("Creating launcher: {}", launcher);
		try {
			return launcherRepository.save(launcher);
		} catch (Exception e) {
			log.error("Failed to create launcher: {}", e.getMessage());
			throw new RuntimeException("Failed to create launcher", e);
		}
	}

	public Launcher getLauncherById(String launcherId) {
		log.info("Fetching launcher with ID: {}", launcherId);
		return launcherRepository.findById(launcherId).orElse(null);
	}

	@Override
	public List<Launcher> getAllLaunchers() {
		log.info("Fetching All launchers: {}");
		return launcherRepository.findAll();
	}

	public Launcher updateLauncher(String launcherId, Launcher launcher) {
		log.info("Updating launcher with ID: {}", launcherId);
		Launcher existingLauncher = getLauncherById(launcherId);
		if (existingLauncher == null) {
			log.warn("Launcher with ID: {} not found", launcherId);
			throw new RuntimeException("Launcher with ID not found" + launcherId);
		}
		BeanUtils.copyProperties(launcher, existingLauncher);
		return launcherRepository.save(existingLauncher);
	}

	public void deleteLauncherId(String launcherId) {
		log.info("Deleting launcher with ID: {}", launcherId);
		if (launcherRepository.existsById(launcherId)) {
			launcherRepository.deleteById(launcherId);
			log.info("Launcher deleted successfully");
		} else {
			log.error("Failed to delete launcher with ID: {}", launcherId);
			throw new RuntimeException("Failed to delete launcher with ID: " + launcherId);
		}
	}

}
