package tn.esprit.eventsproject.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogisticsDTO {
    private int idLog;
    private String description;
    private boolean reserve;
    private float prixUnit;
    private int quantite;
}
