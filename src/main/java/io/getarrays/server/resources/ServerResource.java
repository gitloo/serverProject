package io.getarrays.server.resources;

import io.getarrays.server.enumeration.Status;
import io.getarrays.server.model.Response;
import io.getarrays.server.model.Server;
import io.getarrays.server.service.implementation.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

// 31 Response --> Avremmo potuto chiamare questa classe ServerController ma tipicamente, quando si tratta di un REST
// controller come una REST API, viene chiamata Resource, anche se tecnicamente è un CONTROLLER.
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {
    // 32 importiamo la ServiceImplementation perché è ciò che chiameremo ogni volta che avremo bisogno di fare qualcosa nel backend

    // 32a L'errore che ci appare qui sotto su serverService è dovuto al fatto che già nella classe ServerServiceImpl abbiamo dovuto definire ServerRepo come un campo final e abbiamo utilizzato @RequiredArgsConstructor, che creava il costruttore e passava al suo interno un'istanza di ServerRepo e ciò rappresentava la Dependency Injection di cui avevamo bisogno. Aggiungiamo anche qui la stessa @ e l'errore si risolve.
    private final ServerServiceImpl serverService;

    // 32b definiamo un metodo pubblico che ci restituirà una ResponseEntity di tipo Response, quindi il corpo della ResponseEntity sarà la Response che abbiamo creato nel model, lo chiamiamo getServers() e quindi restituirà una lista di tutti i server che abbiamo nell'applicazione.
    // 32c All'interno del metodo vogliamo returnare la ResponseEntity. Chiamiamo il metodo ok di ResponseEntity, che richiede un body, cioè l'oggetto(o istanza) della classe Response che dovremo passare. Al posto che passarle il costruttore però, utilizziamo il builder pattern per passare i dati che ci interessano, dato che la classe Response ha molti dati e si rischia di confondersi nella costruzione.
    // 33 Quando avvieremo l'app e andremo all'indirizzo http:localhost:8080/server/list il risultato sarà la Response che avremo creato con il metodo getServers(), e ci verranno restituiti i primi 30 elementi della tabella in cui abbiamo salvato i server
    @GetMapping("/list")
    public ResponseEntity<Response> getServers() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        // 32d per la proprietà 'data', il tipo di dato richiesto è Map, quindi passiamo le informazioni richieste, cioè la combinazione di chiave ("servers") e valore, cioè tutti i server che vorremo vedere restituiti, con il metodo list definito in serverService (istanza di ServerServiceImpl, a rigo 26)
                        .data(Map.of("servers", serverService.list(30)))
                        .message("Servers retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
// 34 Creiamo il metodo per pingare un server, e per farlo dobbiamo trovare un modo per passare l'indirizzo IP del server che cerchiamo di pingare al metodo, e per farlo aggiungiamo un {ipAddress} all'URL
    @GetMapping("/ping/{ipAddress}")
    // 34a Nel definire il metodo pingServer(), passiamo al suo interno la Path variable che stiamo cercando (cioè l'indirizzo IP, contenuto nel percorso URL (il path)) stabiliamo l'esatto risultato che cerchiamo("ipAddress") e definiamo il tipo di dato (String) e il nome della variabile (ipAddress)
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        // 34b Verifichiamo se riusciamo a pingare il server, per poter modificare lo status del server cercato. Per risolvere il problema, aggiungiamo la method signature
        Server server = serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
        // 34c cambiamo la chiave in "server"
                        .data(Map.of("server", server))
                        // 34d cambiamo il messaggio da restituire all'utente. Se lo status del server, ricavato tramite server.getStatus(), è uguale a SERVER_UP il messaggio da restituire sarà "Ping success" altrimenti comunicheremo "Ping failed".
                        .message(server.getStatus() == Status.SERVER_UP ? "Ping success" : "Ping failed")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    // 35 definiamo una Richiesta POST, perchè qui è dove cercheremo di salvare un server e come URL impostiamo /save
    @PostMapping("/save")
    // 35a Nel definire il metodo saveServer(), passiamo al suo interno l'annotation @RequestBody, perchè manderemo il body e la richiesta e l'annotation @Valid (la dipendenza che aggiungiamo)
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
       // 35b Catturiamo il body della richiesta, che conterrà il server. Se la richiesta sarà eseguita con successo, non avremo una OK Response, quindi modifichiamo status e statusCode da OK a CREATED
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        // 35e come chiave per 'data' possiamo lasciare server perchè returneremo il server che avremo creato e come valore il metodo per creare un server della classe ServerServiceImpl, implementato dalla sua istanza serverService, definita a rigo 29
                        .data(Map.of("server", serverService.create(server)))
                        .message("Server create")
                        .status(CREATED)
                        // 35c il valore che verrà restituito è 201, lo status che indica che la richiesta è stata soddisfatta e ha comportato la creazione di una o più nuove risorse. La risorsa primaria creata dalla richiesta viene identificata da un campo header Location nella risposta o, se il campo header viene ricevuto, dall'URI di richiesta effettivo.
                        // 35d ATTENZIONE !!! Avremmo potuto utilizzare il metodo created() a rigo 71, dopo ResponseEntity, ma richiede un URI e noi invece vogliamo mandare una Response all'utente quindi se utilizzassimo il metodo created() non potremmo poi returnare una Response. Per simulare questa cosa potremmo returnare una nuova ResponseEntity e poi riempire tutte le informazioni di classe (le proprietà della classe Response) per il costruttore, ma il metodo ok() è comunque una situazione accettabile per quando qualcosa viene creato nel server
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server", serverService.get(id)))
                        .message("Server retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        // il risultato di serverService.delete(id) sarà un boolean T o F
                        .data(Map.of("deleted", serverService.delete(id)))
                        .message("Server deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    // 36 Per la @ GetMapping, il parametro predefinito è path, che corrisponde al percorso che il controller andrà ad intercettare per restituire qualcosa all'utente. In questo caso dobbiamo aggiungere un altro parametro e quindi occorre specificare tutti i parametri utilizzati: il primo è path, il secondo è produces, che esplicita la ricerca che stiamo facendo, cioè un'immagine png, non un JSON come per tutte le funzioni precedenti
    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        // 36a rimuoviamo tutto il builder pattern, perchè non dovremo returnare nulla come Response in questo caso, bensì dovremo returnare i bytes che possiamo leggere dal file che stiamoc cercando. Cambiamo quindi il tipo di dato da returnare nel metodo (byte[]) e ci dirigiamo verso la posizione all'interno del PC dove dovremmo trovare i byte dell'immagine che vogliamo leggere e returniamoli.
        // 36b importiamo Paths per poter effettuare ricerche nei file del nostro PC, e all'interno del metodo get inseriamo System.getProperty, che ci permetterà di indirizzare il percorso esatto dove cercare l'immagine. Aggiungiamo poi Add signature method a readAllBytes per risolvere l'errore che compare.
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
        // 36c una volta settato l'URL dell'immagine sul server, quando lo caricheremo sul browser, di default il browser manderà una richiesta GET, che sarà intercettata dal metodo getServerImage(), e avrà il fileName nella richiesta --> http:localhost:8080/image/server1.png. Dunque, una volta raccolta l'immagine (ad es. server1.png), il metodo ci porterà all'interno della cartella specificata e cercheà di leggere i bites dal file, che corrisponderanno a ciò che verrà returnato
    }

    // 37 Aggiungiamo sul database alcuni servers per verificare che tutto funzioni, ad esempio quando cerchiamo di elencare i server, perchè in assenza di dati sul database, se provassimo ad andare all'indirizzo http:localhost:8080/server/list, come risposta avremmo un array vuoto.
    // --> ServerApplication
}
