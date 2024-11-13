package tn.esprit.spring;

import tn.esprit.spring.services.PisteServicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock  // Crée un mock pour le dépôt
    private IPisteRepository pisteRepository;

    @InjectMocks  // Injecte le mock dans le service à tester
    private PisteServicesImpl pisteServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialise les annotations Mockito
    }

    @Test
    void retrieveAllPistes_ShouldReturnListOfPistes() {
        // Arrange
        Piste piste1 = new Piste(1L, "Piste 1", Color.RED, 1500, 30, null);
        Piste piste2 = new Piste(2L, "Piste 2", Color.BLUE, 1200, 25, null);
        List<Piste> pistes = Arrays.asList(piste1, piste2);
        when(pisteRepository.findAll()).thenReturn(pistes);  // Simule le comportement de findAll()

        // Act
        List<Piste> result = pisteServices.retrieveAllPistes();

        // Assert
        assertEquals(2, result.size());  // Vérifie la taille de la liste
        assertTrue(result.contains(piste1));  // Vérifie si la liste contient piste1
        verify(pisteRepository, times(1)).findAll();  // Vérifie que findAll() a été appelé une fois
    }

    @Test
    void addPiste_ShouldReturnSavedPiste() {
        // Arrange
        Piste piste = new Piste(1L, "Piste 1", Color.GREEN, 1600, 20, null);
        when(pisteRepository.save(piste)).thenReturn(piste);  // Simule le comportement de save()

        // Act
        Piste result = pisteServices.addPiste(piste);

        // Assert
        assertNotNull(result);  // Vérifie que le résultat n'est pas null
        assertEquals("Piste 1", result.getNamePiste());  // Vérifie le nom de la piste sauvegardée
        verify(pisteRepository, times(1)).save(piste);  // Vérifie que save() a été appelé une fois
    }

    @Test
    void removePiste_ShouldCallDeleteByIdOnce() {
        // Arrange
        Long numPiste = 1L;

        // Act
        pisteServices.removePiste(numPiste);

        // Assert
        verify(pisteRepository, times(1)).deleteById(numPiste);  // Vérifie que deleteById() a été appelé une fois
    }

    @Test
    void retrievePiste_ShouldReturnPisteWhenFound() {
        // Arrange
        Long numPiste = 1L;
        Piste piste = new Piste(numPiste, "Piste 1", Color.RED, 1500, 30, null);
        when(pisteRepository.findById(numPiste)).thenReturn(Optional.of(piste));  // Simule findById()

        // Act
        Piste result = pisteServices.retrievePiste(numPiste);

        // Assert
        assertNotNull(result);  // Vérifie que le résultat n'est pas null
        assertEquals("Piste 1", result.getNamePiste());  // Vérifie le nom de la piste récupérée
        verify(pisteRepository, times(1)).findById(numPiste);  // Vérifie que findById() a été appelé une fois
    }

    @Test
    void retrievePiste_ShouldReturnNullWhenNotFound() {
        // Arrange
        Long numPiste = 1L;
        when(pisteRepository.findById(numPiste)).thenReturn(Optional.empty());  // Simule une piste non trouvée

        // Act
        Piste result = pisteServices.retrievePiste(numPiste);

        // Assert
        assertNull(result);  // Vérifie que le résultat est null
        verify(pisteRepository, times(1)).findById(numPiste);  // Vérifie que findById() a été appelé une fois
    }
}
