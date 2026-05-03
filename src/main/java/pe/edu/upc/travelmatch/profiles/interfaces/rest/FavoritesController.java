package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoriteByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.domain.services.FavoriteCommandService;
import pe.edu.upc.travelmatch.profiles.domain.services.FavoriteQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateFavoriteResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.FavoriteResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.CreateFavoriteCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.FavoriteResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/favorites", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Favorites", description = "Favorites Management Endpoints")
public class FavoritesController {
    private final FavoriteCommandService favoriteCommandService;
    private final FavoriteQueryService favoriteQueryService;

    public FavoritesController(FavoriteCommandService favoriteCommandService, FavoriteQueryService favoriteQueryService) {
        this.favoriteCommandService = favoriteCommandService;
        this.favoriteQueryService = favoriteQueryService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResource> createFavorite(@RequestBody CreateFavoriteResource createFavoriteResource) {
        var createFavoriteCommand = CreateFavoriteCommandFromResourceAssembler.toCommandFromResource(createFavoriteResource);
        var favoriteId = favoriteCommandService.handle(createFavoriteCommand);
        System.out.println("Favorite created with id: " + favoriteId);
        var getFavoriteByUserIdAndExperienceIdQuery = new GetFavoriteByUserIdAndExperienceIdQuery(new UserId(createFavoriteResource.userId()), new ExperienceId(createFavoriteResource.experienceId()));
        var favorite = favoriteQueryService.handle(getFavoriteByUserIdAndExperienceIdQuery);

        if(favorite.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var favoriteResource = FavoriteResourceFromEntityAssembler.toResourceFromEntity(favorite.get());
        return new ResponseEntity<>(favoriteResource, HttpStatus.CREATED);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<FavoriteResource>> getFavoritesByUserId(@PathVariable Long userId) {
        UserId userIdValueObject = new UserId(userId);
        var getFavoritesByUserIdQuery = new GetFavoritesByUserIdQuery(userIdValueObject);
        var favorites = favoriteQueryService.handle(getFavoritesByUserIdQuery);
        var favoriteResource = favorites.stream().map(FavoriteResourceFromEntityAssembler::toResourceFromEntity).toList();
        return new ResponseEntity<>(favoriteResource, HttpStatus.OK);
    }

    @DeleteMapping("users/{userId}/experience/{experienceId}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Long experienceId, @PathVariable Long userId) {
        ExperienceId experienceIdValueObject = new ExperienceId(experienceId);
        UserId userIdValueObject = new UserId(userId);
        var deleteFavoriteCommand = new DeleteFavoriteCommand(userIdValueObject, experienceIdValueObject);
        favoriteCommandService.handle(deleteFavoriteCommand);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
