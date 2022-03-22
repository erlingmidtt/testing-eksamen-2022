package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void hentKundeInfo_loggetInn() {

        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        // act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertEquals(enKunde, resultat);
    }

    @Test
    public void hentKundeInfo_IkkeloggetInn() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentTransaksjoner_LoggetInn() {
        List<Transaksjon> testTransaksjoner = new ArrayList<>();
        Transaksjon testtransaksjon1 = new Transaksjon(0, "41275254977", 3000, "2022-02-18", "overføringMellomKonti", "1", "5428458416");
        Transaksjon testtransaksjon2 = new Transaksjon(1, "58741259425", 4999.99, "2022-02-28", "NetOnNet","1", "5428458416");
        testTransaksjoner.add(testtransaksjon1);
        testTransaksjoner.add(testtransaksjon2);

        Konto testKonto = new Konto("01010110523", "5428458416", 20000.0, "Brukskonto", "NOK", testTransaksjoner);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentTransaksjoner("5428458416", "2022-02.18", "2022-02-28")).thenReturn(testKonto);

        Konto resultat = bankController.hentTransaksjoner("5428458416", "2022-02.18", "2022-02-28");

        assertEquals(testKonto, resultat);
    }

    @Test
    public void hentTransaksjoner_ikkeLoggetInn() {
        when(sjekk.loggetInn()).thenReturn(null);
        Konto resultat = bankController.hentTransaksjoner("5428458416", "2022-02.18", "2022-02-28");
        assertNull(resultat);
    }

    @Test
    public void hentKonti_LoggetInn() {
        // arrange
        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKonti(anyString())).thenReturn(konti);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertEquals(konti, resultat);
    }

    @Test
    public void hentKonti_IkkeLoggetInn() {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentSaldi_loggetInn() {
        //arrage

        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("01010110523", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("01010110523", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        Konto konto3 = new Konto("105010123457", "12345678901",
                1000, "Lønnskonto", "NOK", null);

        konti.add(konto1);
        konti.add(konto2);
        konti.add(konto3);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentSaldi("01010110523")).thenReturn(konti);
        List<Konto> resultat = bankController.hentSaldi();

        assertEquals(konti, resultat);

    }
    @Test
    public void hentSaldi_ikkeLoggetInn(){
        when(sjekk.loggetInn()).thenReturn(null);
        List<Konto> resultat = bankController.hentSaldi();
        assertNull(resultat);
    }

    @Test
    public void registrerBetaling_loggetInn(){
        Transaksjon testTransaksjon = new Transaksjon(0, "41275254977", 3000, "2022-02-18", "overføringMellomKonti", "1", "5428458416");
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.registrerBetaling(testTransaksjon)).thenReturn("testOk");
        String resultat = bankController.registrerBetaling(testTransaksjon);
        assertEquals("testOk", resultat);
    }

    @Test
    public void registrerBetaling_ikkeLoggetInn(){
        Transaksjon testTransaksjon = new Transaksjon(0, "41275254977", 3000, "2022-02-18", "overføringMellomKonti", "1", "5428458416");
        when(sjekk.loggetInn()).thenReturn(null);
        String resultat = bankController.registrerBetaling(testTransaksjon);
        assertNull(resultat);
    }

    @Test
    public void hentBetalinger_LoggetInn() {
        // Arrange
        Transaksjon transaksjon1 = new Transaksjon(20, "01010110523", 800, "2022-01-22", "Strøm", "1", "33310110523");
        Transaksjon transaksjon2 = new Transaksjon(28, "01010110523", 5000, "2020-07-23", "Husleie", "1", "01010110563");

        List<Transaksjon> betalinger = new ArrayList<>();
        betalinger.add(transaksjon1);
        betalinger.add(transaksjon2);

        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.hentBetalinger(anyString())).thenReturn(betalinger);

        List<Transaksjon> resultat = bankController.hentBetalinger();
        assertEquals(betalinger, resultat);
    }

    @Test
    public void hentBetalinger_IkkeLoggetInn() {
        when(sjekk.loggetInn()).thenReturn(null);
        List<Transaksjon> resultat = bankController.hentBetalinger();
        assertNull(resultat);
    }

    @Test
    public void utforBetaling_LoggetInn() {
        // arrange
        Transaksjon transaksjon1 = new Transaksjon(20, "01010110523", 800, "2022-01-22", "Strøm", "1", "33310110523");
        Transaksjon transaksjon2 = new Transaksjon(28, "01010110523", 5000, "2020-07-23", "Husleie", "1", "01010110563");

        List<Transaksjon> betalinger = new ArrayList<>();
        betalinger.add(transaksjon1);
        betalinger.add(transaksjon2);

        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.utforBetaling(20)).thenReturn("OK");
        when(repository.hentBetalinger(anyString())).thenReturn(betalinger);

        List<Transaksjon> resultat = bankController.utforBetaling(20);
        assertEquals(betalinger, resultat);
    }

    @Test
    public void utforBetaling_IkkeLoggetInn() {
        when(sjekk.loggetInn()).thenReturn(null);
        List<Transaksjon> resultat = bankController.utforBetaling(20);
        assertNull(resultat);
    }

    @Test
    public void endreKunde() {
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        Kunde inKunde = new Kunde("01010110524",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        Kunde resultat = bankController.hentKundeInfo();
        assertEquals(enKunde, resultat);

        when(repository.endreKundeInfo(inKunde)).thenReturn("testOk");
        String result = bankController.endre(inKunde);
        assertEquals(result, "testOk");
    }

    @Test
    public void endreKunde_IkkeLoggetInn() {
        when(sjekk.loggetInn()).thenReturn(null);
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");
        String resultat = bankController.endre(enKunde);
        assertNull(resultat);
    }
}

