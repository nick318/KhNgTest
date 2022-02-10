package idv.imonahhov.khngtest.repositories;

import idv.imonahhov.khngtest.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    public User findUserByUsername(String username);
}
