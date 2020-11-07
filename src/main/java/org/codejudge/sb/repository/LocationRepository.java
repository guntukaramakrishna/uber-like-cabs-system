package org.codejudge.sb.repository;

import org.codejudge.sb.entity.Driver;
import org.codejudge.sb.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {
    public Optional<Location> findByDriver(Driver driver);
}
