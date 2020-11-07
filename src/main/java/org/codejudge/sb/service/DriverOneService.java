package org.codejudge.sb.service;

import org.codejudge.sb.entity.Driver;
import org.codejudge.sb.entity.Location;
import org.codejudge.sb.model.MinimizedDriver;
import org.codejudge.sb.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverOneService {

    @Autowired
    private DriverRepository driverRepository;

    public List<String> validate(Driver driver) {
        List<String> errorMessages = new ArrayList<>();
        if(driver == null) {
            errorMessages.add("Bad Driver Input");
        }
        if(driver.getName() == null || driver.getName().isEmpty()) {
            errorMessages.add("Name is Empty");
        }
        if(driver.getEmail() == null || driver.getEmail().isEmpty()) {
            errorMessages.add("Email is Empty");
        }
        if(driver.getCar_number() == null || driver.getCar_number().isEmpty()) {
            errorMessages.add("Car number is empty");
        }
        if(driver.getLicense_number() == null || driver.getLicense_number().isEmpty()) {
            errorMessages.add("License number is empty");
        }
        return errorMessages;
    }

    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver findById(Integer id) {
        return driverRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<MinimizedDriver> getNearByDrivers(List<Location> locations) {
        List<MinimizedDriver> drivers = new ArrayList<>();
        for(Location location: locations) {
            Driver driver = location.getDriver();
            MinimizedDriver minimizedDriver = new MinimizedDriver();
            minimizedDriver.setEmail(driver.getEmail());
            minimizedDriver.setPhone_number(driver.getPhone_number());
            minimizedDriver.setName(driver.getName());
            drivers.add(minimizedDriver);
        }
        return drivers;
    }
}
