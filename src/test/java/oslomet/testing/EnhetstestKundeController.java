package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKundeController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EnhetstestKundeController {

    @InjectMocks
    //dette skal testes
    private AdminKundeController AdminKundeController;

    @Mock
    //dette skal Mockes
    private AdminRepository repository;

    @Mock
    //dette skal Mockes
    private Sikkerhet sjekk;

    @Test
    public void hentAlleOK(){

        List<Kunde> kunder = new ArrayList<>();
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        //må lage ny kunde object
        Kunde kundeTo = new Kunde("02020201134", "Kiya", "Nezif","Akerbrygge 40", "1212","Aker", "11223344","velkommen");

        //legger begge kunde-objektene inn i arrayet
        kunder.add(enKunde);
        kunder.add(kundeTo);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentAlleKunder()).thenReturn(kunder);

        // act
        List<Kunde> resultat = AdminKundeController.hentAlle();

        // assert
        assertEquals(kunder, resultat);
    }

    @Test
    public void hentAlleFeil(){
        // Vi sjekker om personen er innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Kunde> resultat = AdminKundeController.hentAlle();

        // assert
        assertNull(resultat);


    }

    @Test
    public void lagreKundeOK(){
        //lager en kunde objekt
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.registrerKunde((any(Kunde.class)))).thenReturn("OK");

        //act
        String resultat = AdminKundeController.lagreKunde(enKunde);

        //assert
        assertEquals("OK", resultat);


    }

    @Test
    public void lagreKundeFeil(){
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = AdminKundeController.lagreKunde(enKunde);

        //assert
        assertEquals("Ikke logget inn", resultat);

    }


    @Test
    public void endreOK(){
        Kunde enKunde = new Kunde("11223345678",
                "Jostein", "Eriksen", "Osloveien 43", "3270",
                "Oslo", "21213131", "Jostein123");

        when(sjekk.loggetInn()).thenReturn("98798711111");

        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn("OK");

        //act
        String resultat = AdminKundeController.endre(enKunde);

        //assert
        assertEquals("OK", resultat);

    }

    @Test
    public void endreFeil(){
        Kunde enKunde = new Kunde("11223345678",
                "Jostein", "Eriksen", "Osloveien 43", "3270",
                "Oslo", "21213131", "Jostein123");


        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = AdminKundeController.endre(enKunde);

        //assert
        assertEquals("Ikke logget inn", resultat);

    }


    @Test
    public void slettOK(){
        when(sjekk.loggetInn()).thenReturn("Admin");

        when(repository.slettKunde(anyString())).thenReturn("OK");
        String resultat = AdminKundeController.slett("01010110523");
        assertEquals("OK", resultat);

    }

    @Test
    public void slettFeil(){
        when(sjekk.loggetInn()).thenReturn(null);

        String resultat = AdminKundeController.slett ("01010110523");
        assertEquals("Ikke logget inn", resultat);
    }










}

