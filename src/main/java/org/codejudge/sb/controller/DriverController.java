package org.codejudge.sb.controller;

import org.codejudge.sb.entity.Driver;
import org.codejudge.sb.entity.Location;
import org.codejudge.sb.model.LocationRequest;
import org.codejudge.sb.model.ResponseModel;
import org.codejudge.sb.service.DriverOneService;
import org.codejudge.sb.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/driver")
public class DriverController {
    private static final Logger LOG = LoggerFactory.getLogger(DriverController.class);
    private static final String URL_CREATE = "/register";
    private static final String SHARE_LOCATION = "/{id}/sendLocation/";

    @Autowired
    DriverOneService driverOneService;
    @Autowired
    LocationService locationService;

    @PostMapping(value = URL_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody Driver driver) {
        try {
            List<String> errorMessages = driverOneService.validate(driver);
            if ( ! errorMessages.isEmpty()) {
                ResponseModel response = new ResponseModel();
                response.setStatus("failure");
                StringBuilder reasons = new StringBuilder();
                errorMessages.forEach(value -> reasons.append(value+" ; "));
                response.setReason(reasons.toString());
                return new ResponseEntity<ResponseModel>(response, HttpStatus.BAD_REQUEST);
            } else {
                driver = driverOneService.save(driver);
                return new ResponseEntity<Driver>(driver, HttpStatus.CREATED);
            }
        }catch (Exception ex) {
            LOG.error("Exception while registering the driver");
            return new ResponseEntity<String>("Error while registering driver", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = SHARE_LOCATION, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> shareLocation(@RequestBody LocationRequest locationRequest, @PathVariable("id") Integer id) {
        try {
            List<String> errorMessages = locationService.validate(locationRequest);
            if ( ! errorMessages.isEmpty()) {
                ResponseModel response = new ResponseModel();
                response.setStatus("failure");
                StringBuilder reasons = new StringBuilder();
                errorMessages.forEach(value -> reasons.append(value+" ; "));
                response.setReason(reasons.toString());
                return new ResponseEntity<ResponseModel>(response, HttpStatus.BAD_REQUEST);
            }
            Driver driver = driverOneService.findById(id);
            Location location = new Location();
            location.setDriver(driver);
            location.setLatitude(locationRequest.getLatitude());
            location.setLongitude(locationRequest.getLongitude());
            location = locationService.save(location);
            ResponseModel response = new ResponseModel();
            response.setStatus("success");
            return new ResponseEntity<ResponseModel>(response, HttpStatus.ACCEPTED);
        }catch (Exception ex) {
            LOG.error("Exception while saving the location");
            return new ResponseEntity<String>("Error while saving location", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
