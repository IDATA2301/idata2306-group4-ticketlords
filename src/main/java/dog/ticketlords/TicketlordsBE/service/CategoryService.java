package dog.ticketlords.TicketlordsBE.service;

import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.repositories.CategoryRepository;

@Service
public class CategoryService {

  CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

}
