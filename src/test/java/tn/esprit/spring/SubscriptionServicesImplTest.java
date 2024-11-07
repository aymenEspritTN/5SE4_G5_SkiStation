package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class SubscriptionServicesImplTest {
    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription subscription;

    @BeforeEach
    public void setUp() {
        // Initialisation d'un abonnement de test
        subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setTypeSub(TypeSubscription.ANNUAL);
    }

    @Test
    public void testAddSubscription() {
        // Simuler le comportement du repository
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription savedSubscription = subscriptionServices.addSubscription(subscription);

        // Vérifier que la date de fin est correcte pour un abonnement annuel
        assertEquals(subscription.getStartDate().plusYears(1), savedSubscription.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testUpdateSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);

        assertEquals(subscription, updatedSubscription);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testRetrieveSubscriptionById() {
        Long subscriptionId = 1L;
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

        Subscription foundSubscription = subscriptionServices.retrieveSubscriptionById(subscriptionId);

        assertNotNull(foundSubscription);
        assertEquals(subscription, foundSubscription);
        verify(subscriptionRepository, times(1)).findById(subscriptionId);
    }

    @Test
    public void testGetSubscriptionByType() {
        TypeSubscription type = TypeSubscription.ANNUAL;
        Set<Subscription> subscriptions = Set.of(subscription);
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(type);

        assertEquals(subscriptions, result);
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(type);
    }

    @Test
    public void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        List<Subscription> subscriptions = List.of(subscription);
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        assertEquals(subscriptions, result);
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    @Test
    public void testRetrieveSubscriptions() {
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
    public void testShowMonthlyRecurringRevenue() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(100f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(600f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(1200f);

        subscriptionServices.showMonthlyRecurringRevenue();

        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }
}
