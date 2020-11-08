package org.codejudge.sb.service;

import org.codejudge.sb.entity.Driver;
import org.codejudge.sb.entity.Location;
import org.codejudge.sb.model.MinimizedDriver;
import org.codejudge.sb.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverOneService {

    @Autowired
    private DriverRepository driverRepository;

    public List<String> validate(Driver driver) {
        List<String> errorMessages = new ArrayList<>();
        if(driver == null) {
            errorMessages.add("Bad Driver Input");
            return errorMessages;
        }
        if(driver.getName() == null || driver.getName().isEmpty()) {
            errorMessages.add("Name is Empty");
        }
        if(driver.getEmail() == null || driver.getEmail().isEmpty()) {
            errorMessages.add("Email is Empty");
        }
        if(driver.getPhone_number() == null) {
            errorMessages.add("Phone number is empty");
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

    public List<MinimizedDriver> getNearByDrivers(Collection<Location> locations) {
        return locations.stream().map(location-> {
            Driver driver = location.getDriver();
            return new MinimizedDriver(driver.getName(),driver.getEmail(),driver.getPhone_number());
        }).collect(Collectors.toList());
    }
}
