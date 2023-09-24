package de.hs.da.hskleinanzeigen.service;

import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repository.UserRepository;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @CachePut(value = "users", key = "#user.id")
  public User insertUser(User user){
    return userRepository.save(user);
  }

  public boolean isEmailTaken(String email){
    return userRepository.findByEmail(email).isPresent();
  }

  @Cacheable(value = "users") //parameter value is by default used as key. In our case id.
  public User findById(Integer id){
    return userRepository.findById(id).orElse(null);
  }

  public List<User> findAllSortedAscending(Integer pageStart, Integer pageSize){
    return this.userRepository.findAll(PageRequest.of(pageStart, pageSize, Sort.by("created").ascending())).getContent();
  }

  public boolean validateUser(User user){
    EmailValidator emailValidator = EmailValidator.getInstance();
    boolean isEmailValid = emailValidator.isValid(user.getEmail());
    boolean notNull = isNotNull(user);

    boolean isPasswordLengthValid = user.getPassword().length() >= 6;
    boolean isFirstNameLengthValid = user.getFirstName().length() <= 255;
    boolean isLastNameLengthValid = user.getLastName().length() <= 255;

    return validateLengths(isEmailValid, notNull, isPasswordLengthValid, isFirstNameLengthValid, isLastNameLengthValid);
  }

  private boolean isNotNull(User user) {
    return user.getEmail() != null &&
            user.getPassword() != null &&
            user.getFirstName() != null &&
            user.getLastName() != null;
  }

  private boolean validateLengths(boolean isEmailValid, boolean notNull, boolean isPasswordLengthValid, boolean isFirstNameLengthValid, boolean isLastNameLengthValid) {
    return isEmailValid &&
            notNull &&
            isPasswordLengthValid &&
            isFirstNameLengthValid &&
            isLastNameLengthValid;
  }

  public boolean isNotComplete(User user){
    return user.getEmail() == null ||
            user.getFirstName() == null ||
            user.getLastName() == null ||
            user.getPassword() == null ||
            user.getPhone() == null ||
            user.getLocation() == null;
  }
}
