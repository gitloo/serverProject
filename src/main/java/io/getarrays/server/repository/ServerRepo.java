package io.getarrays.server.repository;

import io.getarrays.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

// 2 Definiamo un modo per manipolare i dati inseriti nel DB, e questo sarà possibile con JPA. Creiamo un nuovo package repository, dove creiamo un'interfaccia ServerRepo, che estenderà la classe JpaRepository, e nel fare ciò, dobbiamo passare due informazioni: il dominio o la classe che vogliamo gestire (Server) e il tipo di dato della chiave primaria della classe (id = Long) che vogliamo gestire con l'interfaccia ServerRepo.
// 3 Estendendo la classe JpaRepository, abbiamo accesso a una serie di metodi che possiamo usare per manipolare i dati all'interno del server Mysql, così da non dover definirli tutti manualmente. L'unico metodo che aggiungiamo manualmente è quello che trova un server partendo dall'ip --> 4
public interface ServerRepo extends JpaRepository<Server, Long> {
    // 4 Il metodo restituirà un oggetto Server: il nome in questo caso è importante, perchè quando scriviamo Find By comunichiamo alla classe Jpa che stiamo cercando di fare una SELECT utilizzando come filtro il parametro del metodo, che dovrà essere un campo della classe oggetto che vogliamo trovare
    Server findByIpAddress(String ipAddress);
}

// 5 ora che abbiamo il model (Server) che vogliamo manipolare e abbiamo un modo per manipolarlo (ServerRepo, che estende JpaRepository), dobbiamo definire le differenti funzionalità che vogliamo avere nell'applicazione. Per fare ciò, definiamo un servizio / service e in seguito definiamo i metodi che andranno a rappresentare le caratteristiche che vogliamo avere nell'app. Creiamo quindi un nuovo package, chiamato service, al cui interno creeremo un'interfaccia ServerService --> 6