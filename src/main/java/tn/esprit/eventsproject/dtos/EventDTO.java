package tn.esprit.eventsproject.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class EventDTO {
    private int idEvent;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private float cout;
    private Set<ParticipantDTO> participants;
    private Set<LogisticsDTO> logistics;
}
