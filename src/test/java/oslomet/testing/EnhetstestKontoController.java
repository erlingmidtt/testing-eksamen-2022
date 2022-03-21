package oslomet.testing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKontoController;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestKontoController {
    @InjectMocks
    // denne skal testes
    private AdminKontoController AdminController;

    @Mock
    // denne skal Mock'es
    private AdminRepository repository;


    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;


    @Test
    public void hentAlleOk()  {
        // arrange
        List<Konto> konto = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konto.add(konto1);
        konto.add(konto2);

        when(sjekk.loggetInn()).thenReturn("105010123456");

        when(repository.hentAlleKonti()).thenReturn(konto);

        // act
        List<Konto> resultat =AdminController.hentAlleKonti();

        // assert
        assertEquals(konto, resultat);
    }

    @Test
    public void hentAlleFeil()  {
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = AdminController.hentAlleKonti();

        // assert
        assertNull(resultat);
    }

    @Test
    public void registrerOk(){
        List<Konto> konto = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK",
                null);

        when(sjekk.loggetInn()).thenReturn("105010123456");
        when(repository.registrerKonto(konto1)).thenReturn("OK");


        String resultat = AdminController.registrerKonto(konto1);
        assertEquals("OK", resultat);

    }
    @Test
    public void registrerFeil(){
        //List<Konto> konto = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK",
                null);

        when(sjekk.loggetInn()).thenReturn(null);

        String resultat = AdminController.registrerKonto(konto1);

        assertEquals("Ikke innlogget", resultat);

    }
    @Test
    public void test_endrkontoOK() {

        // check if konto1 is inserted
        // update konto1
        when(sjekk.loggetInn()).thenReturn("105010121012");

        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);

        when(repository.endreKonto(any(Konto.class))).thenReturn("OK");

        /*Konto konto2 = new Konto("105010123456", "01010110524",
                800, "Lønnskonto", "NOK", null);*/

        //when(repository.endreKonto(any(Konto .class))).thenReturn("Ikke innlogget");

        String restulat = AdminController.endreKonto(konto1);
        assertEquals("OK", restulat);
    }

    @Test
    public void test_endreKontoFeil() {

        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);

        String restulat = AdminController.endreKonto(konto1);
        assertEquals("Ikke innlogget", restulat);
    }

    @Test
    public void test_slettKontoOk () {
        when(sjekk.loggetInn()).thenReturn("Admin");

        when(repository.slettKonto(anyString())).thenReturn("OK");
        String resultat = AdminController.slettKonto("01010110523");
        assertEquals("OK", resultat);
    }

    @Test
    public void test_slettKontoFeil() {
        when(sjekk.loggetInn()).thenReturn(null);

        String resultat = AdminController.slettKonto("01010110523");
        assertEquals("Ikke innlogget", resultat);
    }

}
