package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
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

    public AdsService(AdsRepository adsRepository,
                      CommentRepository commentRepository,
                      UserRepository userRepository,
                      ImageService imageService,
                      DtoMapper dtoMapper) {
        this.adsRepository = adsRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.dtoMapper = dtoMapper;
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
        Ads ads = adsRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not ads with id = {}", id);
            return new AdsNotFoundException(id);
        });
        return dtoMapper.toResponseWrapperComment(ads.getComments());
    }

    public CommentRecord addComments(Long id, CommentRecord commentRecord) {
        logger.info("Was invoked method addComments");
        Comment comment = dtoMapper.toCommentEntity(commentRecord);
        Ads ads = adsRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not ads with id = {}", id);
            return new AdsNotFoundException(id);
        });
        comment.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        comment.setAds(ads);
        comment.setUser(userRepository.findById(1L).orElse(null));
        return dtoMapper.toCommentDto(commentRepository.save(comment));
    }

    public FullAds getFullAd(Long id) {
        logger.info("Was invoked method getFullAd");
        Ads ads = adsRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not ads with id = {}", id);
            return new AdsNotFoundException(id);
        });
        return dtoMapper.toFullAds(ads);
    }

    public void removeAds(Long id) {
    }

    public AdsRecord updateAds(Long id, CreateAds createAds) {
        return null;
    }

    public CommentRecord getCommentsAd(Long id, Long commentId) {
        return null;
    }

    public void deleteComments(Long id, Long commentId) {
    }

    public CommentRecord updateComments(Long id, Long commentId, CommentRecord commentRecord) {
        return null;
    }

    public String updateAdsImage(Long id, MultipartFile multipartFile) {
        return null;
    }

    public ResponseWrapperAds getAdsMe() {
        logger.info("Was invoked method getAdsMe");
        return dtoMapper.toResponseWrapperAds(adsRepository.findAll());
    }
}
