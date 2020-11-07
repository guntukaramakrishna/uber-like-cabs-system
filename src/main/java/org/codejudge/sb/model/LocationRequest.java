package org.codejudge.sb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString
public class LocationRequest {
    private Double latitude;
    private Double longitude;

}
