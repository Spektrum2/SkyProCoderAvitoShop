package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentForbiddenException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AdsService {
    private final Logger logger = LoggerFactory.getLogger(AdsService.class);
    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final DtoMapper dtoMapper;
    private final ImageRepository imageRepository;

    public AdsService(AdsRepository adsRepository,
                      CommentRepository commentRepository,
                      UserRepository userRepository,
                      ImageService imageService,
                      DtoMapper dtoMapper,
                      ImageRepository imageRepository) {
        this.adsRepository = adsRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.dtoMapper = dtoMapper;
        this.imageRepository = imageRepository;
    }

    public ResponseWrapperAds getWrapperAds() {
        logger.info("Was invoked method getWrapperAds");
        return dtoMapper.toResponseWrapperAds(adsRepository.findAll());
    }

    public AdsRecord addAds(CreateAds createAds, MultipartFile multipartFile) throws IOException {
        logger.info("Was invoked method addAds");
        Ads ads = dtoMapper.toCreateAdsEntity(createAds);
        ads.setImage(imageService.uploadImage(multipartFile));
        ads.setUser(userRepository.findById(1L).orElse(null));
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

    public CommentRecord addComments(Long id, CommentRecord commentRecord) {
        logger.info("Was invoked method addComments");
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        Comment comment = dtoMapper.toCommentEntity(commentRecord);
        comment.setUser(ads.getUser());
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

    public void removeAds(Long id) {
        logger.info("Was invoked method removeAds");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", id);
                    return new AdsNotFoundException(id);
                });
        commentRepository.deleteAll(ads.getComments());
        adsRepository.deleteById(id);
    }

    public AdsRecord updateAds(Long id, CreateAds createAds) {
        logger.info("Was invoked method updateAds");
        Ads ads = adsRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not ads with id = {}", id);
            return new AdsNotFoundException(id);
        });
        ads.setDescription(createAds.getDescription());
        ads.setPrice(BigDecimal.valueOf(createAds.getPrice()));
        ads.setTitle(createAds.getTitle());
        return dtoMapper.toAdsDto(adsRepository.save(ads));
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
            throw new CommentForbiddenException(commentId);
        }
    }

    public void deleteComments(Long id, Long commentId) throws RuntimeException {
        logger.info("Was invoked method deleteComments");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("There is not ads with id = {}", commentId);
                    return new CommentNotFoundException(commentId);
                });
        if (comment.getAds().getId() == id) {
            commentRepository.deleteById(commentId);
        } else {
            throw new CommentForbiddenException(commentId);
        }
    }

    public CommentRecord updateComments(Long id, Long commentId, CommentRecord commentRecord) {
        logger.info("Was invoked method updateComments");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            logger.error("There is not comment with id = {}", commentId);
            return new CommentNotFoundException(commentId);
        });
        if (comment.getAds().getId() == id) {
            comment.setText(commentRecord.getText());
            return dtoMapper.toCommentDto(commentRepository.save(comment));
        } else {
            throw new CommentForbiddenException(commentId);
        }
    }

    public void updateAdsImage(Long id, MultipartFile multipartFile) throws IOException {
        logger.info("Was invoked method updateAdsImage");
        Ads ads = adsRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not ads with id = {}", id);
            return new AdsNotFoundException(id);
        });
        Long oldImage = ads.getImage().getId();
        ads.setImage(imageService.uploadImage(multipartFile));
        adsRepository.save(ads);
        imageRepository.deleteById(oldImage);
    }

    public ResponseWrapperAds getAdsMe() {
        logger.info("Was invoked method getAdsMe");
        return dtoMapper.toResponseWrapperAds(adsRepository.findAll());
    }
}
