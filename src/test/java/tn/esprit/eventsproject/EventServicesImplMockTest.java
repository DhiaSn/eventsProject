package tn.esprit.eventsproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import tn.esprit.eventsproject.services.EventServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServicesImplMockTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private LogisticsRepository logisticsRepository;

    @InjectMocks
    private EventServicesImpl eventServices;

    private ParticipantDTO participantDTO;
    private EventDTO eventDTO;
    private LogisticsDTO logisticsDTO;
    private Participant participant;
    private Event event;
    private Logistics logistics;

    @BeforeEach
    public void setUp() {
        participantDTO = new ParticipantDTO();
        participantDTO.setIdPart(1);
        participantDTO.setNom("John");
        participantDTO.setPrenom("Doe");

        eventDTO = new EventDTO();
        eventDTO.setIdEvent(1);
        eventDTO.setDescription("Sample Event");

        logisticsDTO = new LogisticsDTO();
        logisticsDTO.setIdLog(1);
        logisticsDTO.setDescription("Sample Logistics");

        participant = new Participant();
        participant.setIdPart(1);
        participant.setNom("John");
        participant.setPrenom("Doe");

        event = new Event();
        event.setIdEvent(1);
        event.setDescription("Sample Event");

        logistics = new Logistics();
        logistics.setIdLog(1);
        logistics.setDescription("Sample Logistics");
    }

    @Test
    public void testAddParticipant() {
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        ParticipantDTO result = eventServices.addParticipant(participantDTO);

        assertNotNull(result);
        assertEquals(participantDTO.getIdPart(), result.getIdPart());
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    public void testAddAffectEvenParticipantWithId() {
        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDTO result = eventServices.addAffectEvenParticipant(eventDTO, 1);

        assertNotNull(result);
        assertEquals(eventDTO.getIdEvent(), result.getIdEvent());
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testAddAffectEvenParticipantWithEvent() {
        Set<ParticipantDTO> participantsDTO = new HashSet<>();
        participantsDTO.add(participantDTO);
        eventDTO.setParticipants(participantsDTO);

        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDTO result = eventServices.addAffectEvenParticipant(eventDTO);

        assertNotNull(result);
        assertEquals(eventDTO.getIdEvent(), result.getIdEvent());
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testAddAffectLog() {
        when(eventRepository.findByDescription("Sample Event")).thenReturn(event);
        when(logisticsRepository.save(any(Logistics.class))).thenReturn(logistics);

        LogisticsDTO result = eventServices.addAffectLog(logisticsDTO, "Sample Event");

        assertNotNull(result);
        assertEquals(logisticsDTO.getIdLog(), result.getIdLog());
        verify(eventRepository, times(1)).findByDescription("Sample Event");
        verify(logisticsRepository, times(1)).save(any(Logistics.class));
    }

    @Test
    public void testGetLogisticsDates() {
        List<Event> events = Collections.singletonList(event);
        when(eventRepository.findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(events);

        List<LogisticsDTO> result = eventServices.getLogisticsDates(LocalDate.now(), LocalDate.now().plusDays(1));

        assertNotNull(result);
        verify(eventRepository, times(1)).findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    public void testCalculCout() {
        List<Event> events = Collections.singletonList(event);
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache("Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);

        eventServices.calculCout();

        verify(eventRepository, times(1)).findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache("Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

}
