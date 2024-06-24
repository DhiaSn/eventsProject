package tn.esprit.eventsproject.services;

import tn.esprit.eventsproject.DTOs.EventDTO;
import tn.esprit.eventsproject.DTOs.LogisticsDTO;
import tn.esprit.eventsproject.DTOs.ParticipantDTO;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;

import java.time.LocalDate;
import java.util.List;

public interface IEventServices {
    ParticipantDTO addParticipant(ParticipantDTO participantDTO);
    EventDTO addAffectEvenParticipant(EventDTO eventDTO, int idParticipant);
    EventDTO addAffectEvenParticipant(EventDTO eventDTO);
    LogisticsDTO addAffectLog(LogisticsDTO logisticsDTO, String descriptionEvent);
    List<LogisticsDTO> getLogisticsDates(LocalDate date_debut, LocalDate date_fin);
    void calculCout();
}
