package dog.ticketlords.TicketlordsBE.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import dog.ticketlords.TicketlordsBE.dbentity.SearchLog;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.repositories.UnregisteredUserRepository;
import dog.ticketlords.TicketlordsBE.repositories.SearchLogRepository;

@Service
public class SearchLogService {

  private SearchLogRepository searchLogRepository;
  private UnregisteredUserRepository unregisteredUserRepository;

  public SearchLogService(SearchLogRepository searchLogRepository,
      UnregisteredUserRepository unregisteredUserRepository) {
    this.searchLogRepository = searchLogRepository;
    this.unregisteredUserRepository = unregisteredUserRepository;
  }

  /**
   * Saves a search entry to the database.
   *
   * @param userId the id of the unregisteredUser who is doing the search.
   * @param query  the query they entered into the search field.
   * @return true if successful, false otherwise
   */
  public boolean saveSearchLog(long userId, String query) {
    Optional<UnregisteredUser> unregUser = this.unregisteredUserRepository.findById(userId);
    if (unregUser.isPresent()) {
      SearchLog log = new SearchLog(unregUser.get(), query);
      this.searchLogRepository.save(log);
      return true;
    }
    return false;
  }

  /**
   * Finds all search entries done by a user's id.
   *
   * @param user any user, registered or not.
   * @return List of all search log entries.
   */
  public List<SearchLog> getAllUsersSearches(long userId) {
    return this.searchLogRepository.findAllByUser_UId(userId);
  }

  /**
   * Finds the most recent searches a user has done.
   *
   * @param userId the user to see searches for.
   * @param limit  the amount of searches to find.
   *
   * @return List of all found searches.
   */
  public List<SearchLog> getRecentSearchLogsByUserId(long userId, int limit) {
    Pageable pageable = PageRequest.of(0, limit);
    return this.searchLogRepository.findTopNByUser_UIdOrderBySearchedAtDesc(userId, pageable);
  }
}
