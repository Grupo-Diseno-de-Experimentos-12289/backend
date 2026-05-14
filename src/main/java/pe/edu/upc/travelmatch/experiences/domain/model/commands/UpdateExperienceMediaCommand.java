package pe.edu.upc.travelmatch.experiences.domain.model.commands;

/**
 * Command to update an experience media.
 *
 * @param id the ID
 * @param mediaUrl the media URL
 * @param caption the caption
 */
public record UpdateExperienceMediaCommand(Long id, String mediaUrl, String caption) {}
