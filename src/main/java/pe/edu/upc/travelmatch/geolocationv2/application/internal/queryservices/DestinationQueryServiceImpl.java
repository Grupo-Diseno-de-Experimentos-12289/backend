package pe.edu.upc.travelmatch.geolocationv2.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetAllDestinationsQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByDestinationNameQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByIdQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationName;
import pe.edu.upc.travelmatch.geolocationv2.domain.services.DestinationQueryService;
import pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories.DestinationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationQueryServiceImpl implements DestinationQueryService {
    private final DestinationRepository destinationRepository;
    public DestinationQueryServiceImpl(DestinationRepository destinationRepository) {
       this.destinationRepository = destinationRepository;
    }

    @Override
    public List<Destination> handle(GetAllDestinationsQuery query) {
        return this.destinationRepository.findAll();
    }

    @Override
    public Optional<Destination> handle(GetDestinationByDestinationNameQuery query) {
        var destinationName = new DestinationName(query.name());
        return this.destinationRepository.findByName(destinationName);
    }

    @Override
    public Optional<Destination> handle(GetDestinationByIdQuery query) {
        return this.destinationRepository.findById(query.destinationId());
    }
}
