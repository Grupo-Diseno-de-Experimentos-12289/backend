package pe.edu.upc.travelmatch.experiences.domain.model.commands;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
/**
 * Command to create an experience media.
 *
 * @param experience the experience
 * @param mediaUrl   the media url
 * @param caption    the caption
 */
public record CreateExperienceMediaCommand(
    Experience experience,
    String mediaUrl,
    String caption) {
}
