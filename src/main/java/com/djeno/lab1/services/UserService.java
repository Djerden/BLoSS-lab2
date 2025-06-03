package com.djeno.lab1.services;

import com.djeno.lab1.exceptions.EmailAlreadyExistsException;
import com.djeno.lab1.exceptions.UserNotFoundException;
import com.djeno.lab1.exceptions.UsernameAlreadyExistsException;
import com.djeno.lab1.persistence.enums.Role;
import com.djeno.lab1.persistence.models.User;
import com.djeno.lab1.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new UsernameAlreadyExistsException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по username или email пользователя
     *
     * @return пользователь
     */
    public User getByUsernameOrEmail(String identifier) {
        return repository.findByUsername(identifier)
                .orElseGet(() -> repository.findByEmail(identifier)
                        .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")));
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsernameOrEmail;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
