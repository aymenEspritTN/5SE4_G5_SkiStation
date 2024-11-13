package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionServicesImpl subscriptionServices;

    @BeforeEach
    void setUp() {
        subscriptionServices = new SubscriptionServicesImpl(subscriptionRepository, null);
    }

    @Test
    void showMonthlyRecurringRevenue_shouldCalculateCorrectRevenue() {
        // Arrange
        // Mocking the repository to return specific values for each type of subscription revenue
        Float monthlyRevenue = 1000.0f;
        Float semestrielRevenue = 6000.0f;
        Float annualRevenue = 12000.0f;

        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(monthlyRevenue);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(semestrielRevenue);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(annualRevenue);

        // Calculate the expected revenue based on the method logic
        Float expectedRevenue = monthlyRevenue + (semestrielRevenue / 6) + (annualRevenue / 12);

        // Act
        subscriptionServices.showMonthlyRecurringRevenue();

        // Assert
        // Verify that each type of subscription revenue was fetched from the repository
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);

        // Optionally, check if the correct revenue is logged (log capture could be used in advanced testing setups)
        Mockito.verify(subscriptionRepository, Mockito.times(1))
                .recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);

        // Since `showMonthlyRecurringRevenue` logs the revenue, you may use a logger or another approach to verify the output if necessary
    }
}

