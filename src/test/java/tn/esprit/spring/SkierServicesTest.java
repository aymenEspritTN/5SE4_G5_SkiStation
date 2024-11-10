package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.time.LocalDate;
import java.util.*;
import tn.esprit.spring.services.SkierServicesImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkierServicesTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllSkiers() {
        List<Skier> skiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findAll()).thenReturn(skiers);

        List<Skier> result = skierServices.retrieveAllSkiers();

        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findAll();
    }

    @Test
    void testAddSkier_AnnualSubscription() {
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.of(2023, 1, 1));
        Skier skier = new Skier();
        skier.setSubscription(subscription);

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.addSkier(skier);

        assertEquals(LocalDate.of(2024, 1, 1), result.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testAssignSkierToSubscription() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(2L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.assignSkierToSubscription(1L, 2L);

        assertEquals(subscription, result.getSubscription());
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testAddSkierAndAssignToCourse() {
        Skier skier = new Skier();
        Course course = new Course();
        Registration registration = new Registration();
        skier.setRegistrations(new HashSet<>(Collections.singletonList(registration)));

        when(skierRepository.save(skier)).thenReturn(skier);
        when(courseRepository.getById(3L)).thenReturn(course);

        Skier result = skierServices.addSkierAndAssignToCourse(skier, 3L);

        assertEquals(course, result.getRegistrations().iterator().next().getCourse());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testRemoveSkier() {
        skierServices.removeSkier(1L);

        verify(skierRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveSkier() {
        Skier skier = new Skier();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        Skier result = skierServices.retrieveSkier(1L);

        assertEquals(skier, result);
        verify(skierRepository, times(1)).findById(1L);
    }

    @Test
    void testAssignSkierToPiste() {
        Skier skier = new Skier();
        skier.setPistes(new HashSet<>());
        Piste piste = new Piste();

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(2L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.assignSkierToPiste(1L, 2L);

        assertTrue(result.getPistes().contains(piste));
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() {
        List<Skier> skiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(skiers);

        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
    }
}
