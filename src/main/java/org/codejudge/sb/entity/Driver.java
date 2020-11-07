package org.codejudge.sb.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
@Entity
@Table(name="cab_driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private Long phone_number;
    @Column
    private String license_number;
    @Column
    private String car_number;
}
