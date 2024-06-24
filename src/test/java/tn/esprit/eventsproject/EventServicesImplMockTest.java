package tn.esprit.eventsproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    private Participant participant;
    private Event event;
    private Logistics logistics;

    @BeforeEach
    public void setUp() {
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
        when(participantRepository.save(participant)).thenReturn(participant);

        Participant result = eventServices.addParticipant(participant);

        assertNotNull(result);
        assertEquals(participant, result);
        verify(participantRepository, times(1)).save(participant);
    }

    @Test
    public void testAddAffectEvenParticipantWithId() {
        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(event)).thenReturn(event);

        Event result = eventServices.addAffectEvenParticipant(event, 1);

        assertNotNull(result);
        assertEquals(event, result);
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void testAddAffectEvenParticipantWithEvent() {
        Set<Participant> participants = new HashSet<>();
        participants.add(participant);
        event.setParticipants(participants);

        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(event)).thenReturn(event);

        Event result = eventServices.addAffectEvenParticipant(event);

        assertNotNull(result);
        assertEquals(event, result);
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void testAddAffectLog() {
        when(eventRepository.findByDescription("Sample Event")).thenReturn(event);
        when(logisticsRepository.save(logistics)).thenReturn(logistics);

        Logistics result = eventServices.addAffectLog(logistics, "Sample Event");

        assertNotNull(result);
        assertEquals(logistics, result);
        verify(eventRepository, times(1)).findByDescription("Sample Event");
        verify(logisticsRepository, times(1)).save(logistics);
    }

    @Test
    public void testGetLogisticsDates() {
        List<Event> events = Collections.singletonList(event);
        when(eventRepository.findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(events);

        List<Logistics> result = eventServices.getLogisticsDates(LocalDate.now(), LocalDate.now().plusDays(1));

        assertNotNull(result);
        verify(eventRepository, times(1)).findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    public void testCalculCout() {
        List<Event> events = Collections.singletonList(event);
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache("Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);

        eventServices.calculCout();

        verify(eventRepository, times(1)).findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache("Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, times(1)).save(event);
    }
}
