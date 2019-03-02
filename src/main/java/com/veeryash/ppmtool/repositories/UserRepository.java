package com.veeryash.ppmtool.repositories;

import com.veeryash.ppmtool.domain.User;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User getById(Long id);

}
