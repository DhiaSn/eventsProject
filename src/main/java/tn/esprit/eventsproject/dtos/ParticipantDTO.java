package tn.esprit.eventsproject.dtos;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.eventsproject.entities.Tache;

import java.util.Set;

@Getter
@Setter
public class ParticipantDTO {
    private int idPart;
    private String nom;
    private String prenom;
    private Tache tache;
    private Set<EventDTO> events;

}
