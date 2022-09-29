package io.getarrays.server.model;

import io.getarrays.server.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.GenerationType.AUTO;

// 1 mappiamo la classe per il DB
// 2 Definiamo un modo per manipolare i dati inseriti nel DB, e questo sarà possibile con JPA. Creiamo un nuovo package repository, dove creiamo un'interfaccia ServerRepo, che estenderà la classe JpaRepository, e nel fare ciò, dobbiamo passare due informazioni: il dominio o la classe che vogliamo gestire (Server) e il tipo di dato della chiave primaria della classe che vogliamo gestire con l'interfaccia ServerRepo -->
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {
    @Id
    @GeneratedValue(strategy = AUTO) // come il programma dovrà generare l'ID. Se AUTO compare rosso, tasto dx su AUTO, More actions e Import static
    private Long id; //l'id sarà la chiave primaria nel tabella Server, nel database
    @Column(unique = true) // in questo modo creiamo un vincolo su ipAddress di modo che i dati inseriti in questa colonna dovranno essere univoci. Nel caso in cui dovessimo provare ad inserire un ip già inserito, questa annotation lancerà una Exception di vincolo in mysql
    @NotEmpty(message = "IP Address cannot be empty or null") // questa annot assicurerà che quando verrà inviata una richiesta di salvataggio sul database di un oggetto Server, dovrà essere presente un IP
    private String ipAddress;
    private String name;
    private String memory;
    private String type;
    private String imageUrl;
    private Status Status; // dato che definirà se il server è acceso o spento
}
