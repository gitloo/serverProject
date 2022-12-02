package io.getarrays.server.service.implementation;

import io.getarrays.server.enumeration.Status;
import io.getarrays.server.model.Server;
import io.getarrays.server.repository.ServerRepo;
import io.getarrays.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Random;

import static java.lang.Boolean.TRUE;

// 16 implementiamo i metodi contenuti in ServerService

// 17 questa @ è necessaria perché comunicherà a Lombok di creare un costruttore con al suo interno il campo ServerRepo e costituirà la dependency injection. Quindi invece di creare un costruttore manualmente, Lombok lo farà automaticamente con questa annotation
@RequiredArgsConstructor
@Service
@Transactional
// 18 questa @ ci permetterà di avere qualcosa stampato nella console, così da poter vedere esattamente cosa sta accadendo
@Slf4j
public class ServerServiceImpl implements ServerService {
    // 16a definiamo una proprietà ServerRepo
    private final ServerRepo serverRepo;

    // 19 implementiamo i metodi definiti
    // create() --> ogni qualvolta utilizziamo questo metodo, verrà registrato il salvataggio di un nuovo server nella console, imposterà l'immagine del server, perché verrà richiesto all'user di impostarla, infine salverà il server nel db e restituirà quel server.
    @Override
    public Server create(Server server) {
        // 20 questo metodo restituirà un registro nella console che dirà "Saving new server" e passerà il nome del server
        log.info("Saving new server: {}", server.getName());
        // 21 creiamo un'immagine per il server con il metodo setImageUrl e, al suo interno, passiamo il metodo setServerImageUrl che definiamo immediatamente (tasto DX, implement method... in ServerServiceImpl) lasciando momentaneamente return null; come unica implementazione.
        server.setImageUrl(setServerImageUrl());
        // 22 per salvare un server, grazie alla JpaRepository, abbiamo già a disposizione il metodo save, che permette di salvare qualcosa sul database e quindi come parametro del metodo passiamo un oggetto server, che sarà quello che vogliamo salvare dopo averlo creato.
        return serverRepo.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
/*
                // 23 Ogni volta che questo metodo verrà eseguito, nel registro della console comparirà il messaggio sottostante (l'indirizzo IP del server)
            log.info("Pinging server IP: {}", ipAddress);
                // 23a a questo punto però non abbiamo il server che lo user sta cercando di pingare, ma solo il suo IP. Con questa funzione andiamo nel db e selezioniamo il server attraverso l'indirizzo IP
            Server server = serverRepo.findByIpAddress(ipAddress);
                // 23b Pinghiamo il server con questa funzione (presa dal package java.net), che ci darà l'indirizzo Inet e...
            InetAddress address = InetAddress.getByName(ipAddress);
                // 23c ...una volta che avremo questo indirizzo, potremo controllare se il server è raggiungibile, aggiungendo un timeout, e se dopo questo timeout non potremo raggiungere il server il codice continuerà la sua esecuzione.
                // 23d Cosa facciamo concretamente? Dopo aver recuperato l'oggetto address relativo al specifico indirizzo IP che stavamo cercando, andiamo a settare lo Status del server e controlliamo se riusciamo a raggiungere il server nel tempo limite inserito: aggiungendo il ? dopo il timeout, possiamo direzionare il codice a seconda del risultato di ricerca del Server: se lo trova prima del timeout, allora lo Status del server sarà settato su SERVER_UP, altrimenti sarà settato su SERVER_DOWN.
            server.setStatus(address.isReachable(30000) ? Status.SERVER_UP : Status.SERVER_DOWN); // 23e per risolvere l'errore su isReachable, implementare "Add exception...", così facendo, risolviamo anche l'errore su
                // 23f salviamo quindi il server nel db con il nuovo status
            serverRepo.save(server);
            return server;

SOTTO HO IMPLEMENTATO (e adattato) UN'ALTERNATIVA, IN QUANTO LEGGENDO LA CLASSE InetAddress.java SEMBRANO ESSERCI PROBLEMI CON FIREWALL QUANDO SI TENTA isReachable()

 */
        Server server = serverRepo.findByIpAddress(ipAddress);
        server.setStatus(server.getIpAddress().length() <= 13 ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepo.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepo.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}", id);
        // 24 returniamo un oggetto server attraverso la funzione findById la Repository del Server, aggiungendo anche la chiamata al metodo get()
        return serverRepo.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        // 25 questo metodo restituirà un registro nella console che dirà "Saving new server" e passerà il nome del server
        log.info("Updating server: {}", server.getName());
        // 25a Rimuoviamo l'immagine, perchè quando aggiorniamo un server non vogliamo aggiornare anche l'immagine
        // server.setImageUrl(setServerImageUrl());
        // 25b Rimane la funzione save, ma la differenza, rispetto al metodo create, è che quando passiamo un id, Jpa è abbastanza intelligente da sapere che è un id esistente e quindi
        return serverRepo.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server by id: {}", id);
        serverRepo.deleteById(id);
        return TRUE;
    }

    private String setServerImageUrl() {
        // 26 per settare l'immagine del server, definiamo un array di String, che avrà come unici valori i 4 che impostiamo
        String[] imageNames = {"server1.png", "server2.png", "server3.png", "server4.png"};
        // 26A vogliamo poter prendere uno di questi 4 valori disponibili in maniera randomica e assegnarli come URL per lo specifico server che scegliamo. Quando verrà restituito questo URL sarà localhost:8080/server/image/server_.png
        // 26b Il 4 contenuto in nextInt(4) serve a specificare che il numero random non dovrà andare oltre il 3, in quanto si tratta di scegliere un numero a caso nell'indice di un array (0-1-2-3). Infine convertiamo il tutto in String in quanto è il tipo di dato richiesto dal metodo.
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }

    // 27 prima di creare il controller vogliamo far sì che sia possibile mandare una risposta all'utente, così che ogni volta che vorremo mandare la stessa risposta (sia nel caso di errore che di successo), potremo farlo. Definiamo quindi nel package model una nuova classe, chiamata Response --> Response
}
