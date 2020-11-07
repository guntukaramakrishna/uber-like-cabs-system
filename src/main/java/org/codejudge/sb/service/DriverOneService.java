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
        if(driver.getEmail() != null && !driver.getEmail().contains("@")) {
            errorMessages.add("Invalid email format");
        }
        if(driver.getPhone_number() != null && driver.getPhone_number().toString().length() != 10) {
            errorMessages.add("Invalid Phone Number");
        }
        if(driver.getCar_number() == null || driver.getCar_number().isEmpty()) {
            errorMessages.add("Car number is empty");
        }
        if(driver.getLicense_number() == null || driver.getLicense_number().isEmpty()) {
            errorMessages.add("License number is empty");
        }
        return errorMessages;
    }

    public List<String> uniquenessOfDriver(Driver driver) {
        List<String> errorMessages = new ArrayList<>();
        Driver anyDriver = driverRepository.findByEmail(driver.getEmail()).orElse(null);
        if(anyDriver != null) {
            errorMessages.add("This email exists already");
        }
        anyDriver = driverRepository.findByPhoneNumber(driver.getPhone_number()).orElse(null);
        if(anyDriver != null) {
            errorMessages.add("This phone number exists already");
        }
        anyDriver = driverRepository.findByLicenseNumber(driver.getLicense_number()).orElse(null);
        if(anyDriver != null) {
            errorMessages.add("This License Number exists already");
        }
        anyDriver = driverRepository.findByCarNumber(driver.getCar_number()).orElse(null);
        if(anyDriver != null) {
            errorMessages.add("This Car Number exists already");
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
