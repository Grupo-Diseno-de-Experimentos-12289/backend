package pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates;

import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name = "destinations")
@EntityListeners(AuditingEntityListener.class)
public class Destination extends AuditableAbstractAggregateRoot<Destination> {

    @Getter
    @Embedded
    @AttributeOverrides({
                @AttributeOverride(name = "name", column = @Column(name = "destination_name", length = 100, nullable = false))
    })
    private DestinationName name;

    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "destination_address", length = 150, nullable = false))
    })
    private DestinationAddress address;

    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "district", column = @Column(name = "district", length = 50, nullable = false))
    })
    private District district;

    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "city", length = 50, nullable = false))
    })
    private City city;


    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "state", column = @Column(name = "state", length = 50, nullable = false))
    })
    private State state;

    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "country", length = 50, nullable = false))
    })
    private Country country;


    // Constructores
    public Destination() {}

    public Destination(String name, String address, String district, String city, String state, String country) {
        this.name = new DestinationName(name);
        this.address = new DestinationAddress(address);
        this.district = new District(district);
        this.city = new City(city);
        this.state = new State(state);
        this.country = new Country(country);
    }

    // Métodos para actualizar (si quieres agregarlos más adelante)
    public Destination(CreateDestinationCommand command) {
        this.name = new DestinationName(command.name());
        this.address = new DestinationAddress(command.address());
        this.district = new District(command.district());
        this.city = new City(command.city());
        this.state = new State(command.state());
        this.country = new Country(command.country());
    }

    public Destination updateInformation(String name, String address, String district, String city, String state, String country) {
        this.name = new DestinationName(name);
        this.address = new DestinationAddress(address);
        this.district = new District(district);
        this.city = new City(city);
        this.state = new State(state);
        this.country = new Country(country);
        return this;
    }


}

