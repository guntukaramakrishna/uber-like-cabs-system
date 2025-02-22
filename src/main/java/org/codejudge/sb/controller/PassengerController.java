package org.codejudge.sb.controller;

import org.codejudge.sb.entity.Location;
import org.codejudge.sb.model.*;
import org.codejudge.sb.service.DriverOneService;
import org.codejudge.sb.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/passenger")
public class PassengerController {
    private static final Logger LOG = LoggerFactory.getLogger(PassengerController.class);
    private static final String AVAILABLE_CABS = "/available_cabs";

    @Autowired
    LocationService locationService;

    @Autowired
    DriverOneService driverOneService;

    @PostMapping(value = AVAILABLE_CABS, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> availableCabs(@RequestBody LocationRequest locationRequest) {
        try {
            LOG.info(" ### Start of the PassengerController availableCabs() ####");
            LOG.info("Input --> {}",locationRequest);
            List<String> errorMessages = locationService.validate(locationRequest);
            if ( ! errorMessages.isEmpty()) {
                ResponseModel response = new ResponseModel();
                response.setStatus("failure");
                String reasons = errorMessages.stream()
                        .collect(Collectors.joining(";"));
                response.setReason(reasons);
                return new ResponseEntity<ResponseModel>(response, HttpStatus.BAD_REQUEST);
            }
            List<Location> matchingLocations = locationService.getMatchingLocations(locationRequest);
            if(matchingLocations.isEmpty()) {
                LOG.info("No cabs available for this request - {}",locationRequest);
                NoCabsResponse noCabsResponse = new NoCabsResponse();
                noCabsResponse.setMessage("No cabs available!");
                return new ResponseEntity<NoCabsResponse>(noCabsResponse, HttpStatus.OK);
            }
            List<MinimizedDriver> nearByDriverList = driverOneService.getNearByDrivers(matchingLocations);
            AvailableCabs cabs = new AvailableCabs();
            cabs.setAvailable_cabs(nearByDriverList);
            LOG.info("available cabs for this request - {}",locationRequest);
            LOG.info(cabs.toString());
            LOG.info(" ### End of the PassengerController availableCabs() ####");
            return new ResponseEntity<AvailableCabs>(cabs, HttpStatus.OK);
        }catch (Exception ex) {
            LOG.error("Exception while saving the location");
            return new ResponseEntity<String>("Error while saving location", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
