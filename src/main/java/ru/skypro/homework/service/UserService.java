package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserRecord;
import ru.skypro.homework.repository.UserRepository;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    public UserService(UserRepository userRepository, DtoMapper dtoMapper) {
        this.userRepository = userRepository;
        this.dtoMapper = dtoMapper;
    }

    public NewPassword setPassword(NewPassword newPassword) {
        return null;
    }

    public UserRecord getUser() {
        logger.info("Was invoked method getUser");
        return userRepository.findById(1L)
                .map(dtoMapper::toUserDto)
                .orElse(null);
    }

    public UserRecord updateUser(UserRecord userRecord) {
        return null;
    }

    public void updateUserImage(MultipartFile multipartFile) {
    }
}
