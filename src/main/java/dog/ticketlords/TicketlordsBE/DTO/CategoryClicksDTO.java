package dog.ticketlords.TicketlordsBE.DTO;

/**
 * DTO record holding all information relevant to map a user's click per
 * category.
 * Used to map user interests based on their event clicks.
 *
 * @param userId       the id of the user to get interest from.
 * @param categoryId   the id of the category to find amount of clicks on.
 * @param categoryName the category's name.
 * @param clickCount   the amount of clicks a user has done on the specific
 *                     event.
 */
public record CategoryClicksDTO(long userId, long categoryId, String categoryName, long clickCount) {
}
