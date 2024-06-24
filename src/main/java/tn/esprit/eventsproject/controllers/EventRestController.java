package tn.esprit.eventsproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.eventsproject.DTOs.EventDTO;
import tn.esprit.eventsproject.DTOs.LogisticsDTO;
import tn.esprit.eventsproject.DTOs.ParticipantDTO;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("event")
@RequiredArgsConstructor
public class EventRestController {
    private final IEventServices eventServices;

    @PostMapping("/addPart")
    public ParticipantDTO addParticipant(@RequestBody ParticipantDTO participantDTO) {
        return eventServices.addParticipant(participantDTO);
    }

    @PostMapping("/addEvent/{id}")
    public EventDTO addEventPart(@RequestBody EventDTO eventDTO, @PathVariable("id") int idPart) {
        return eventServices.addAffectEvenParticipant(eventDTO, idPart);
    }

    @PostMapping("/addEvent")
    public EventDTO addEvent(@RequestBody EventDTO eventDTO) {
        return eventServices.addAffectEvenParticipant(eventDTO);
    }

    @PutMapping("/addAffectLog/{description}")
    public LogisticsDTO addAffectLog(@RequestBody LogisticsDTO logisticsDTO, @PathVariable("description") String descriptionEvent) {
        return eventServices.addAffectLog(logisticsDTO, descriptionEvent);
    }

    @GetMapping("/getLogs/{d1}/{d2}")
    public List<LogisticsDTO> getLogistiquesDates(@PathVariable("d1") LocalDate date_debut, @PathVariable("d2") LocalDate date_fin) {
        return eventServices.getLogisticsDates(date_debut, date_fin);
    }
}
