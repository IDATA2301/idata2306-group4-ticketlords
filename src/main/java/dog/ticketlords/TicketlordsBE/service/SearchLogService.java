package dog.ticketlords.TicketlordsBE.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import dog.ticketlords.TicketlordsBE.dbentity.SearchLog;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.repositories.SearchLogRepository;

@Service
public class SearchLogService {

  private SearchLogRepository searchLogRepository;

  public SearchLogService(SearchLogRepository searchLogRepository) {
    this.searchLogRepository = searchLogRepository;
  }

  /**
   * Saves a search entry to the database.
   *
   * @param user       any user registered or not's searches are saved.
   * @param query      the query they entered into the search field.
   * @param searcherAt the date and time the search happened.
   */
  public void saveSearchLog(UnregisteredUser user, String query, LocalDateTime searcherAt) {
    SearchLog log = new SearchLog(user, query, searcherAt);
    this.searchLogRepository.save(log);
  }

  /**
   * Finds all search entries done by a user's id.
   *
   * @param user any user, registered or not.
   * @return List of all search log entries.
   */
  public List<SearchLog> getAllUsersSearches(UnregisteredUser user) {
    return this.searchLogRepository.findAllById(Collections.singleton(user.getUId()));
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
