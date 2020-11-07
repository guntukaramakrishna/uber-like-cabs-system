package org.codejudge.sb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString
public class AvailableCabs {
    private List<MinimizedDriver> available_cabs;
}
