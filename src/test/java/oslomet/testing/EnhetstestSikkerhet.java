package oslomet.testing;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpSession;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestSikkerhet {

    @InjectMocks
    //denne skal testes
    private Sikkerhet Sikkerhet;

    @Mock
    //denne skal mockes
    private BankRepository repository;

    @Mock
    //denne skal mockes
    MockHttpSession session;

    @Before
    public void initSession(){
        Map<String,Object> attributes = new HashMap<String,Object>();

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                return attributes.get(key);
            }
        }).when(session).getAttribute(anyString());

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                Object value = invocation.getArguments()[1];
                attributes.put(key, value);
                return null;
            }
        }).when(session).setAttribute(anyString(), any());
    }
    @Test
    public void test_SjekkLoggInn(){
        // arrange
        when(repository.sjekkLoggInn(anyString(),anyString())).thenReturn("OK");

        // setningen under setter ikke attributten, dvs. at det ikke er mulig å sette en attributt i dette oppsettet
        session.setAttribute("Innlogget", "12345678901");

        // act
        String resultat = Sikkerhet.sjekkLoggInn("12345678901","HeiHeiHei");
        // assert
        assertEquals("OK", resultat);
    }
    @Test
    public void test_SjekkLoggInnFeilPassord() {
        String Resultat = Sikkerhet.sjekkLoggInn("12345567878", "");
        assertEquals("Feil i passord", Resultat);
    }
    @Test
    public void test_SjekkLoggInnFeilPersNr() {
        String Resultat = Sikkerhet.sjekkLoggInn("", "passord");
        assertEquals("Feil i personnummer", Resultat);
    }
    @Test
    public void test_SjekkLoggInnFeil() {
        when(repository.sjekkLoggInn(anyString(),anyString())).thenReturn("Feil i personnummer eller passord");
        String Resultat = Sikkerhet.sjekkLoggInn("12345567878", "passord");
        assertEquals("Feil i personnummer eller passord", Resultat);
    }
    @Test
    public void test_LoggUt(){
        session.setAttribute("Innlogget", "1234567889");
        Sikkerhet.loggUt();
        assertNull(session.getAttribute("Innlogget"));
    }

    @Test
    public void test_loggInnAdmin(){
        String Resultat = Sikkerhet.loggInnAdmin("Admin", "Admin");
        assertEquals("Logget inn", Resultat);
    }

    @Test
    public void test_loggInnAdminFeil(){
        String Resultat = Sikkerhet.loggInnAdmin("admin", "");
        assertEquals("Ikke logget inn", Resultat);
    }
    @Test
    public void test_loggetInn(){
        session.setAttribute("Innlogget", "1234567330");
        String Resultat = Sikkerhet.loggetInn();
        assertEquals("1234567330", Resultat);
    }
    @Test
    public void test_ikkeLoggetInn(){
        String Resultat = Sikkerhet.loggetInn();
        assertNull(Resultat);
    }

}
