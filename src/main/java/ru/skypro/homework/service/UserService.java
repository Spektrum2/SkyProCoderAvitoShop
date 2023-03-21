package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserRecord;
import ru.skypro.homework.exception.UserForbiddenException;
import ru.skypro.homework.exception.UserNameNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final AvatarService avatarService;
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository,
                       DtoMapper dtoMapper,
                       AvatarService avatarService,
                       UserDetailsManager manager) {
        this.userRepository = userRepository;
        this.dtoMapper = dtoMapper;
        this.avatarService = avatarService;
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    public NewPassword setPassword(NewPassword newPassword, Authentication authentication) {
        logger.info("Was invoked method setPassword");
        User user = userRepository.findByUserName(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        if (user.getPassword().equals(newPassword.getCurrentPassword())) {
            manager.updateUser(
                    org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                            .password(newPassword.getNewPassword())
                            .build());
            return newPassword;
        } else {
            logger.error("The current password is incorrect");
            throw new UserForbiddenException();
        }
    }

    public UserRecord getUser(Authentication authentication) {
        logger.info("Was invoked method getUser");
        User user = userRepository.findByUserName(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        return dtoMapper.toUserDto(user);
    }

    public UserRecord updateUser(UserRecord userRecord) {
        logger.info("Was invoked method getUser");
        User user = userRepository.findById(1L).orElseThrow(() -> {
            logger.error("There is not user with id = {}", 1L);
            return new UserNotFoundException(1L);
        });
        user.setFirstName(userRecord.getFirstName());
        user.setLastName(userRecord.getLastName());
        user.setPhone(userRecord.getPhone());
        return dtoMapper.toUserDto(userRepository.save(user));
    }

    public void updateUserImage(MultipartFile multipartFile) throws IOException {
        logger.info("Was invoked method updateUserImage");
        User user = userRepository.findById(1L).orElseThrow(() -> {
            logger.error("There is not user with id = {}", 1L);
            return new UserNotFoundException(1L);
        });
        user.setAvatar(avatarService.uploadAvatar(multipartFile));
        userRepository.save(user);
    }
}
