package io.getarrays.server.service;

import io.getarrays.server.model.Server;

import java.io.IOException;
import java.util.Collection;

// 6 In questa interfaccia definiamo tutte le funzionalità e caratteristiche che vogliamo che abbia l'app.

// 12 ATTENZIONE!!! Con l'interfaccia non stiamo realmente implementando nulla, ma solo scrivendo funzionalità piatte, che vogliamo tuttavia avere nell'applicazione

// 13 Ora ci tocca trovare un modo per implementare queste funzionalità per poter utilizzare concretamente, ed ecco perchè abbiamo creato la Repository per il Server, perché con questa possiamo creare, listare e aggiornare ed eliminare.

// 14 In un nuovo package (implementation) contenuto all'interno di service, creiamo quindi l'implementazione dell'interfaccia ServerService e, usando la ServerRepository, saremo in grado di creare un'implementazione per tutte le funzionalità definite in ServerService
public interface ServerService {
    // 7 Vogliamo poter salvare un server in un database, quindi creiamo una funzione che restituisca un server
    Server create(Server server);

    // 15 metodo che restituisce il server che stiamo pingando (pingare = contattare un computer o un server mediante l'invio di un pacchetto di dati e la misurazione del tempo di risposta per verificarne la connessione alla rete, individuarne eventuali guasti oppure per segnalare l'apertura o l'aggiornamento di un blog) attraverso l'IP. Il motivo per cui vogliamo che ci venga restituito un server pingato è perchè nel caso in cui lo status del server dovesse cambiare, vogliamo poterne vedere l'aggiornamento nell'interfaccia grafica dell'utente. --> ServerServiceImpl
    Server ping(String ipAddress) throws IOException;

    // 8 Vogliamo poter elencare tutti i server, e per fare ciò creiamo qualcosa che restituisce una collection di oggetti Server, che avrà un limite massimo, inserito come parametro int. Così facendo, vogliamo limitare il numero di server o di righe che restituisce il metodo quando verrà utilizzato per elencare tutti i server
    Collection<Server> list(int limit);

    // 9 metodo che restituisce un server, che però prenderà come parametro l'id del server che vogliamo trovare
    Server get(Long id);

    // 10 metodo che prenderà come parametro un oggetto Server con informazioni da aggiornare e restituisce il server aggiornato
    Server update(Server server);

    // 11 metodo per cancellare un server, che restituirà o vero o falso se un server è stato cancellato o meno, che prenderà come parametro l'id di un server, andrà all'interno di un database, troverà il server attraverso l'id e lo cancellerà
    Boolean delete(Long id);
}
