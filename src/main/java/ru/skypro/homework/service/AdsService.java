package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.IOException;

@Service
public class AdsService {

    public ResponseWrapperAds getWrapperAds() {
        return null;
    }

    public AdsNotFull addAds(CreateAds createAds, MultipartFile multipartFile) throws IOException {
        return null;
    }

    public ResponseWrapperComment getComments(Long id) {
        return null;
    }

    public CommentRecord addComments(Long id, CommentRecord commentRecord) {
        return null;
    }

    public AdsRecord getFullAd(Long id) {
        return null;
    }

    public void removeAds(Long id) {
    }

    public AdsNotFull updateAds(Long id, CreateAds createAds) {
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
}
