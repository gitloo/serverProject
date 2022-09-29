package io.getarrays.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 28 ServerServiceImpl --> Usiamo questa classe per restituire un oggetto Response (risposta) ogni volta che costruiremo un API così che la API possa essere coerente. Questa classe rappresenterà la risposta che verrà inviata all'utente ogni volta che ci sarà una richiesta in entrata nell'applicazione, di successo come di fallimento.
@Data
@SuperBuilder
// 29 Ciò che farà questa @ è che visto che manderemo questa classe a prescindere dal fallimento o meno della richiesta, nel caso in cui ci sia una richiesta eseguita con successo, allora reason e developerMessage saranno null, perché non avremo nessun messaggio da inviare allo sviluppatore e non verranno inclusi nell'oggetto Response. Quindi, quando avremo qualcosa di null nel nostro oggetto Response, con questa @ non verrà completamente mostrato all'utente.
// 30 Dunque useremo questa classe come corpo di risposta che sarà inviata all'utente ogni volta che quest'ultimo invierà una richiesta all'applicazione, quindi non ci resta che inserire un controller e usare questa classe per inviare risposte all'utente quando accederà alla nostra app. Creiamo un nuovo package nel main, chiamato resources e al suo interno creiamo una classe controller chiamata ServerResource --> ServerResource
@JsonInclude(NON_NULL)
public class Response {
    protected LocalDateTime timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    // 28a ragione dell'errore
    protected String reason;
    // 28b messaggio di successo: ogni volta che ci sarà una richiesta in entrata e l'operazione sarà stata eseguita correttamente, restituiremo il messaggio all'utente, che potrà visualizzarlo sul front end come fosse una notifica
    protected String message;
    // 28c messaggio tecnico e dettagliato a disposizione dello sviluppatore
    protected String developerMessage;
    // 28d Map di qualsiasi dato, ci tornerà utile ogni volta che useremo questa classe per inviare info all'user tramite il front end
    protected Map<?, ?> data;
}
