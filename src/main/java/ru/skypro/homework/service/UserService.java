package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserRecord;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final AvatarService avatarService;

    public UserService(UserRepository userRepository, DtoMapper dtoMapper, AvatarService avatarService) {
        this.userRepository = userRepository;
        this.dtoMapper = dtoMapper;
        this.avatarService = avatarService;
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
        logger.info("Was invoked method getUser");
        User newUser = userRepository.findById(1L).get();
        newUser.setFirstName(userRecord.getFirstName());
        newUser.setLastName(userRecord.getLastName());
        newUser.setPhone(userRecord.getPhone());
        return dtoMapper.toUserDto(userRepository.save(newUser));
    }

    public void updateUserImage(MultipartFile multipartFile) throws IOException {
        logger.info("Was invoked method updateUser");
        User newUser = userRepository.findById(1L).get();
        newUser.setAvatar(avatarService.uploadAvatar(multipartFile));
        userRepository.save(newUser);
    }
}
