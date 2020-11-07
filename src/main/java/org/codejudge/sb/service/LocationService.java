package org.codejudge.sb.service;

import org.codejudge.sb.component.Haversine;
import org.codejudge.sb.entity.Location;
import org.codejudge.sb.model.LocationRequest;
import org.codejudge.sb.repository.DriverRepository;
import org.codejudge.sb.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
public class LocationService {
    @Autowired private LocationRepository locationRepository;
    @Autowired private DriverRepository driverRepository;
    @Autowired private Haversine haversine;

    private static final Logger LOG = LoggerFactory.getLogger(LocationService.class);

    public List<String> validate(LocationRequest location) {
        List<String> errorMessages = new ArrayList<>();
        if(location == null) {
            errorMessages.add("Bad Location Input");
            return errorMessages;
        }
        if(location.getLatitude() == null) {
            errorMessages.add("Latitude is empty");
        }
        if(location.getLongitude() == null) {
            errorMessages.add("Longitude is empty");
        }
        return errorMessages;
    }
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> getMatchingLocations(LocationRequest locationRequest) {
        List<Location> matchingLocations = new ArrayList<>();
        List<Location> dbLocations = locationRepository.findAll();

        BiPredicate<Location, LocationRequest> distanceFiler = (x, y) -> {
            double distance = haversine.haversineDistance(x.getLatitude(),x.getLongitude(), y.getLatitude(), y.getLongitude());
            LOG.info("Distance between two locations - "+x.toString()+ " &&fcv "+y.toString()+" is == "+distance);
            return distance <= 4.0;
        };

        if(dbLocations != null && dbLocations.size() != 0) {
            matchingLocations = dbLocations.stream().filter(eachLocation -> distanceFiler.test(eachLocation, locationRequest)).collect(Collectors.toList());
        }
        return matchingLocations;
    }

}
