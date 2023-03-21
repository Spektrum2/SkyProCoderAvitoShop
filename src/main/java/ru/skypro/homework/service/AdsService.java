package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.*;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Authorities;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AdsService {
    private final Logger logger = LoggerFactory.getLogger(AdsService.class);
    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final DtoMapper dtoMapper;
    private final ImageRepository imageRepository;
    private final AuthoritiesRepository authoritiesRepository;

    public AdsService(AdsRepository adsRepository,
                      CommentRepository commentRepository,
                      UserRepository userRepository,
                      ImageService imageService,
                      DtoMapper dtoMapper,
                      ImageRepository imageRepository,
                      AuthoritiesRepository authoritiesRepository) {
        this.adsRepository = adsRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.dtoMapper = dtoMapper;
        this.imageRepository = imageRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    public ResponseWrapperAds getWrapperAds() {
        logger.info("Was invoked method getWrapperAds");
        return dtoMapper.toResponseWrapperAds(adsRepository.findAll());
    }

    public AdsRecord addAds(CreateAds createAds, MultipartFile multipartFile, Authentication authentication) throws IOException {
        logger.info("Was invoked method addAds");
        User user = userRepository.findByUserName(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        Ads ads = dtoMapper.toCreateAdsEntity(createAds);
        ads.setImage(imageService.uploadImage(multipartFile));
        ads.setUser(user);
        return dtoMapper.toAdsDto(adsRepository.save(ads));
    }

    public ResponseWrapperComment getComments(Long id) {
        logger.info("Was invoked method getComments");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        return dtoMapper.toResponseWrapperComment(ads.getComments());
    }

    public CommentRecord addComments(Long id, CommentRecord commentRecord, Authentication authentication) {
        logger.info("Was invoked method addComments");
        LocalDateTime localDateTime = LocalDateTime.now();
        User user = userRepository.findByUserName(authentication.getName());
        if (user == null) {
            logger.error("There is not user with username = {}", authentication.getName());
            throw new UserNameNotFoundException(authentication.getName());
        }
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        Comment comment = dtoMapper.toCommentEntity(commentRecord);
        comment.setUser(user);
        comment.setAds(ads);
        comment.setCreatedAt(localDateTime);
        return dtoMapper.toCommentDto(commentRepository.save(comment));
    }

    public FullAds getFullAd(Long id) {
        logger.info("Was invoked method getFullAd");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        return dtoMapper.toFullAds(ads);
    }

    public void removeAds(Long id, Authentication authentication) {
        logger.info("Was invoked method removeAds");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        if (ads.getUser().getUserName().equals(authentication.getName())
                || getAuthorities(authentication).getAuthority().equals("ROLE_ADMIN")) {
            commentRepository.deleteAll(ads.getComments());
            adsRepository.deleteById(id);
        } else {
            logger.error("Access denied to remove the product");
            throw new UnauthorizedException();
        }
    }

    public AdsRecord updateAds(Long id, CreateAds createAds, Authentication authentication) {
        logger.info("Was invoked method updateAds");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        if (ads.getUser().getUserName().equals(authentication.getName())
                || getAuthorities(authentication).getAuthority().equals("ROLE_ADMIN")) {
            ads.setDescription(createAds.getDescription());
            ads.setPrice(BigDecimal.valueOf(createAds.getPrice()));
            ads.setTitle(createAds.getTitle());
            return dtoMapper.toAdsDto(adsRepository.save(ads));
        } else {
            logger.error("Access denied to remove the product");
            throw new UnauthorizedException();
        }
    }

    public CommentRecord getComment(Long id, Long commentId) {
        logger.info("Was invoked method getComment");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("There is not comment with id = {}", commentId);
                    return new CommentNotFoundException(commentId);
                });
        if (comment.getAds().getId() == id) {
            return dtoMapper.toCommentDto(comment);
        } else {
            logger.error("There is not comment in the product");
            throw new CommentForbiddenException(commentId);
        }
    }

    public void deleteComments(Long id, Long commentId, Authentication authentication) {
        logger.info("Was invoked method deleteComments");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", commentId);
                    return new CommentNotFoundException(commentId);
                });
        if (comment.getUser().getUserName().equals(authentication.getName())
                || getAuthorities(authentication).getAuthority().equals("ROLE_ADMIN")) {
            if (comment.getAds().getId() == id) {
                commentRepository.delete(comment);
            } else {
                logger.error("There is not comment in the product");
                throw new CommentForbiddenException(commentId);
            }
        } else {
            logger.error("Access denied to remove the product");
            throw new UnauthorizedException();
        }
    }

    public CommentRecord updateComments(Long id, Long commentId, CommentRecord commentRecord, Authentication authentication) {
        logger.info("Was invoked method updateComments");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            logger.error("There is not comment with id = {}", commentId);
            return new CommentNotFoundException(commentId);
        });
        if (comment.getUser().getUserName().equals(authentication.getName())
                || getAuthorities(authentication).getAuthority().equals("ROLE_ADMIN")) {
            if (comment.getAds().getId() == id) {
                comment.setText(commentRecord.getText());
                return dtoMapper.toCommentDto(commentRepository.save(comment));
            } else {
                logger.error("There is not comment in the product");
                throw new CommentForbiddenException(commentId);
            }
        } else {
            logger.error("Access denied to remove the product");
            throw new UnauthorizedException();
        }
    }

    public void updateAdsImage(Long id, MultipartFile multipartFile, Authentication authentication) throws IOException {
        logger.info("Was invoked method updateAdsImage");
        Ads ads = adsRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not ads with id = {}", id);
            return new AdsNotFoundException(id);
        });
        if (ads.getUser().getUserName().equals(authentication.getName())
                || getAuthorities(authentication).getAuthority().equals("ROLE_ADMIN")) {
            Long oldImage = ads.getImage().getId();
            ads.setImage(imageService.uploadImage(multipartFile));
            adsRepository.save(ads);
            imageRepository.deleteById(oldImage);
        } else {
            logger.error("Access denied to remove the product");
            throw new UnauthorizedException();
        }
    }

    public ResponseWrapperAds getAdsMe() {
        logger.info("Was invoked method getAdsMe");
        return dtoMapper.toResponseWrapperAds(adsRepository.findAll());
    }

    private Authorities getAuthorities(Authentication authentication) {
        Authorities authorities = authoritiesRepository.findByUsername(authentication.getName());
        if (authorities != null) {
            return authorities;
        } else {
            logger.error("There is not role with username = {}", authentication.getName());
            throw new AuthoritiesNotFoundException(authentication.getName());
        }
    }
}
