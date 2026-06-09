package com.crud_app.security;

import com.crud_app.exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserAssignsUserRole() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User user = userService.registerUser("alice", "secret");

        assertEquals("alice", user.getUsername());
        assertEquals("encoded", user.getPassword());
        assertTrue(user.getRoles().contains(Role.ROLE_USER));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(Set.of(Role.ROLE_USER), captor.getValue().getRoles());
    }

    @Test
    void registerUserThrowsWhenUsernameExists() {
        when(userRepository.existsByUsername("bob")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class,
                () -> userService.registerUser("bob", "pass"));
        verify(userRepository, never()).save(any());
    }
}
