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
import java.util.stream.Collectors;

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
            LOG.info(" ### Start of the DriverController register() ####");
            LOG.info("Input --> {} ",driver);
            List<String> errorMessages = driverOneService.validate(driver);
            if ( ! errorMessages.isEmpty()) {
                return getBadRequestResponseEntity(errorMessages);
            }
            errorMessages = driverOneService.uniquenessOfDriver(driver);
            if ( ! errorMessages.isEmpty()) {
                return getBadRequestResponseEntity(errorMessages);
            }
            Driver savedDriver = driverOneService.save(driver);
            LOG.info(" ### End of the DriverController register() ####");
            return new ResponseEntity<Driver>(savedDriver, HttpStatus.CREATED);

        }catch (Exception ex) {
            LOG.error("Exception while registering the driver");
            return new ResponseEntity<String>("Error while registering driver", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> getBadRequestResponseEntity(List<String> errorMessages) {
        ResponseModel response = new ResponseModel();
        response.setStatus("failure");
        String reasons = errorMessages.stream()
                .collect(Collectors.joining(";"));
        response.setReason(reasons);
        return new ResponseEntity<ResponseModel>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = SHARE_LOCATION, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> shareLocation(@RequestBody LocationRequest locationRequest, @PathVariable("id") Integer id) {
        try {
            LOG.info(" ### Start of the DriverController shareLocation() ####");
            List<String> errorMessages = locationService.validate(locationRequest);
            if ( ! errorMessages.isEmpty()) {
                return getBadRequestResponseEntity(errorMessages);
            }
            Driver driver = driverOneService.findById(id);
            if(driver == null) {
                errorMessages.clear();
                errorMessages.add("Invalid Driver Id");
                return getBadRequestResponseEntity(errorMessages);
            }
            LOG.info("Input --> {}",locationRequest);
            Location location = new Location();
            location.setDriver(driver);
            location.setLatitude(locationRequest.getLatitude());
            location.setLongitude(locationRequest.getLongitude());
            locationService.save(location);
            ResponseModel response = new ResponseModel();
            response.setStatus("success");
            LOG.info(" ### End of the DriverController shareLocation() ####");
            return new ResponseEntity<ResponseModel>(response, HttpStatus.ACCEPTED);
        }catch (Exception ex) {
            LOG.error("Exception while saving the location");
            return new ResponseEntity<String>("Error while saving location", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
