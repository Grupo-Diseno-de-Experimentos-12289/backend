package pe.edu.upc.travelmatch.geolocationv2.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.DeleteDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationName;
import pe.edu.upc.travelmatch.geolocationv2.domain.services.DestinationCommandService;
import pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories.DestinationRepository;

import java.util.Optional;

@Service
public class DestinationCommandServiceImpl implements DestinationCommandService {
    private final DestinationRepository destinationRepository;

    public DestinationCommandServiceImpl(DestinationRepository destinationRepository){
        this.destinationRepository = destinationRepository;
    }
    @Override
    public Long handle(CreateDestinationCommand command) {
        var name= new DestinationName(command.name()) ;
        if (this.destinationRepository.existsByName(name)) {
            throw new IllegalArgumentException("Destination with name " + name + " already exists");
        }
        var destination = new Destination(command);
        try {
            this.destinationRepository.save(destination);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error while saving profile: " + e.getMessage());
        }
        return destination.getId();

    }
    @Override
    public Optional<Destination> handle(UpdateDestinationCommand command) {
        var destinationId = command.destinationId();
        var name= new DestinationName(command.name()) ;
        if (this.destinationRepository.existsByNameAndIdIsNot(name, destinationId)) {
            throw new IllegalArgumentException("Destination with name " + name + " already exists");
        }
        if (!this.destinationRepository.existsById(destinationId)) {
            throw new IllegalArgumentException("Destination with id " + destinationId + " does not exist");
        }
        var destinationToUpdate = this.destinationRepository.findById(destinationId).get();
        destinationToUpdate.updateInformation(command.name(), command.address(), command.district(), command.city(), command.state(), command.country());

        try {
            var updatedDestination = this.destinationRepository.save(destinationToUpdate);
            return Optional.of(updatedDestination);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating destination: " + e.getMessage());
        }
    }

    @Override
    public void handle (DeleteDestinationCommand command) {
        if (!this.destinationRepository.existsById(command.destinationId())) {
            throw new IllegalArgumentException("Destination with id " + command.destinationId() + " does not exist");
        }
        try {
            this.destinationRepository.deleteById(command.destinationId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting destination: " + e.getMessage());
        }
    }


}
