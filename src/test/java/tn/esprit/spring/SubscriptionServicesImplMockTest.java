package tn.esprit.spring;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import org.junit.jupiter.api.BeforeEach;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)

public class SubscriptionServicesImplMockTest {
    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription subscription;

    @BeforeEach
    public void setUp() {
        // Initialiser un abonnement pour les tests
        subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setTypeSub(TypeSubscription.ANNUAL);
    }

    @Test
    public void testAddSubscriptionWithMock() {
        // Simuler le comportement du repository
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription savedSubscription = subscriptionServices.addSubscription(subscription);

        // Vérifier que la date de fin est correcte pour un abonnement annuel
        assertEquals(subscription.getStartDate().plusYears(1), savedSubscription.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testUpdateSubscriptionWithMock() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);

        assertEquals(subscription, updatedSubscription);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testRetrieveSubscriptionByIdWithMock() {
        Long subscriptionId = 1L;
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

        Subscription foundSubscription = subscriptionServices.retrieveSubscriptionById(subscriptionId);

        assertNotNull(foundSubscription);
        assertEquals(subscription, foundSubscription);
        verify(subscriptionRepository, times(1)).findById(subscriptionId);
    }

    @Test
    public void testGetSubscriptionByTypeWithMock() {
        TypeSubscription type = TypeSubscription.ANNUAL;
        Set<Subscription> subscriptions = Set.of(subscription);
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(type);

        assertEquals(subscriptions, result);
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(type);
    }

    @Test
    public void testRetrieveSubscriptionsByDatesWithMock() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        List<Subscription> subscriptions = List.of(subscription);
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        assertEquals(subscriptions, result);
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    @Test
    public void testRetrieveSubscriptionsWithMock() {
        when(subscriptionRepository.findDistinctOrderByEndDateAsc()).thenReturn(List.of(subscription));
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        when(skierRepository.findBySubscription(subscription)).thenReturn(skier);

        subscriptionServices.retrieveSubscriptions();

        verify(subscriptionRepository, times(1)).findDistinctOrderByEndDateAsc();
        verify(skierRepository, times(1)).findBySubscription(subscription);
    }

    @Test
    public void testShowMonthlyRecurringRevenueWithMock() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(100f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(600f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(1200f);

        subscriptionServices.showMonthlyRecurringRevenue();

        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }
}
