package org.codejudge.sb.repository;

import org.codejudge.sb.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByEmail(String email);
    @Query("select d from Driver d where d.phone_number = ?1")
    Optional<Driver> findByPhoneNumber(Long phoneNumber);
    @Query("select d from Driver d where d.license_number = ?1")
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    @Query("select d from Driver d where d.car_number = ?1")
    Optional<Driver> findByCarNumber(String carNumber);
}
