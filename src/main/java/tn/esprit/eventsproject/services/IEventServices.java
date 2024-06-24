package tn.esprit.eventsproject.services;

import tn.esprit.eventsproject.dtos.EventDTO;
import tn.esprit.eventsproject.dtos.LogisticsDTO;
import tn.esprit.eventsproject.dtos.ParticipantDTO;

import java.time.LocalDate;
import java.util.List;

public interface IEventServices {
    ParticipantDTO addParticipant(ParticipantDTO participantDTO);
    EventDTO addAffectEvenParticipant(EventDTO eventDTO, int idParticipant);
    EventDTO addAffectEvenParticipant(EventDTO eventDTO);
    LogisticsDTO addAffectLog(LogisticsDTO logisticsDTO, String descriptionEvent);
    List<LogisticsDTO> getLogisticsDates(LocalDate dateDebut, LocalDate dateFin);
    void calculCout();
}
