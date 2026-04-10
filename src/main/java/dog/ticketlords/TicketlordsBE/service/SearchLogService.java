package dog.ticketlords.TicketlordsBE.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import dog.ticketlords.TicketlordsBE.dbentity.SearchLog;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.repositories.SearchLogRepository;

@Service
public class SearchLogService {

  private SearchLogRepository searchLogRepository;

  public SearchLogService(SearchLogRepository searchLogRepository) {
    this.searchLogRepository = searchLogRepository;
  }

  public void saveSearchLog(UnregisteredUser user, String query, LocalDateTime searcherAt) {
    SearchLog log = new SearchLog(user, query, searcherAt);
    this.searchLogRepository.save(log);

  }

  public List<SearchLog> getAllUsersSearches(UnregisteredUser user) {
    return this.searchLogRepository.findAllById(Collections.singleton(user.getUId()));
  }

}
