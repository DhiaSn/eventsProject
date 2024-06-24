package tn.esprit.eventsproject.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.eventsproject.dtos.EventDTO;
import tn.esprit.eventsproject.dtos.LogisticsDTO;
import tn.esprit.eventsproject.dtos.ParticipantDTO;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicesImpl implements IEventServices {

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final LogisticsRepository logisticsRepository;

    @Override
    public ParticipantDTO addParticipant(ParticipantDTO participantDTO) {
        Participant participant = new Participant();
        BeanUtils.copyProperties(participantDTO, participant);
        participant = participantRepository.save(participant);
        BeanUtils.copyProperties(participant, participantDTO);
        return participantDTO;
    }

    @Override
    public EventDTO addAffectEvenParticipant(EventDTO eventDTO, int idParticipant) {
        Event event = new Event();
        BeanUtils.copyProperties(eventDTO, event);
        Participant participant = participantRepository.findById(idParticipant).orElse(null);
        if (participant != null) {
            if (participant.getEvents() == null) {
                Set<Event> events = new HashSet<>();
                events.add(event);
                participant.setEvents(events);
            } else {
                participant.getEvents().add(event);
            }
            event = eventRepository.save(event);
            BeanUtils.copyProperties(event, eventDTO);
        }
        return eventDTO;
    }

    @Override
    public EventDTO addAffectEvenParticipant(EventDTO eventDTO) {
        Event event = new Event();
        BeanUtils.copyProperties(eventDTO, event);
        Set<ParticipantDTO> participantsDTO = eventDTO.getParticipants();
        Set<Participant> participants = new HashSet<>();
        for (ParticipantDTO participantDTO : participantsDTO) {
            Participant participant = participantRepository.findById(participantDTO.getIdPart()).orElse(null);
            if (participant != null) {
                if (participant.getEvents() == null) {
                    Set<Event> events = new HashSet<>();
                    events.add(event);
                    participant.setEvents(events);
                } else {
                    participant.getEvents().add(event);
                }
                participants.add(participant);
            }
        }
        event.setParticipants(participants);
        event = eventRepository.save(event);
        BeanUtils.copyProperties(event, eventDTO);
        return eventDTO;
    }

    @Override
    public LogisticsDTO addAffectLog(LogisticsDTO logisticsDTO, String descriptionEvent) {
        Event event = eventRepository.findByDescription(descriptionEvent);
        Logistics logistics = new Logistics();
        BeanUtils.copyProperties(logisticsDTO, logistics);
        if (event != null) {
            if (event.getLogistics() == null) {
                Set<Logistics> logisticsSet = new HashSet<>();
                logisticsSet.add(logistics);
                event.setLogistics(logisticsSet);
                eventRepository.save(event);
            } else {
                event.getLogistics().add(logistics);
            }
            logistics = logisticsRepository.save(logistics);
            BeanUtils.copyProperties(logistics, logisticsDTO);
        }
        return logisticsDTO;
    }

    @Override
    public List<LogisticsDTO> getLogisticsDates(LocalDate dateDebut, LocalDate dateFin) {
        List<Event> events = eventRepository.findByDateDebutBetween(dateDebut, dateFin);
        List<LogisticsDTO> logisticsListDTO = new ArrayList<>();
        for (Event event : events) {
            if (event.getLogistics().isEmpty()) {
                return new ArrayList<>();
            } else {
                Set<Logistics> logisticsSet = event.getLogistics();
                for (Logistics logistics : logisticsSet) {
                    if (logistics.isReserve()) {
                        LogisticsDTO logisticsDTO = new LogisticsDTO();
                        BeanUtils.copyProperties(logistics, logisticsDTO);
                        logisticsListDTO.add(logisticsDTO);
                    }
                }
            }
        }
        return logisticsListDTO;
    }

    @Scheduled(cron = "*/60 * * * * *")
    @Override
    public void calculCout() {
        List<Event> events = eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache("Tounsi", "Ahmed", Tache.ORGANISATEUR);
        float somme = 0f;
        for (Event event : events) {
            log.info(event.getDescription());
            Set<Logistics> logisticsSet = event.getLogistics();
            for (Logistics logistics : logisticsSet) {
                if (logistics.isReserve())
                    somme += logistics.getPrixUnit() * logistics.getQuantite();
            }
            event.setCout(somme);
            eventRepository.save(event);
            log.info("Cout de l'Event " + event.getDescription() + " est " + somme);
        }
    }
}