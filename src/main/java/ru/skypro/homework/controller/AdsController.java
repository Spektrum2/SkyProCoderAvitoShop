package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("ads")
public class AdsController {
    private final AdsService adsService;
    private final ImageService imageService;

    public AdsController(AdsService adsService,
                         ImageService imageService) {
        this.adsService = adsService;
        this.imageService = imageService;
    }

    @Operation(
            summary = "getWrapperAds",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "getWrapperAds",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperAds.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @GetMapping
    public ResponseWrapperAds getWrapperAds() {
        return adsService.getWrapperAds();
    }

    @Operation(
            summary = "addAds",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "addAds",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdsRecord addAds(@RequestPart("properties") CreateAds createAds,
                            @RequestPart("image") MultipartFile multipartFile) throws IOException {
        return adsService.addAds(createAds, multipartFile);
    }

    @Operation(
            summary = "getComments",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "getComments",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperComment.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @GetMapping("/{id}/comments")
    public ResponseWrapperComment getComments(@PathVariable Long id) {
        return adsService.getComments(id);
    }

    @Operation(
            summary = "addComments",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "addComments",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @PostMapping("/{id}/comments")
    public CommentRecord addComments(@PathVariable Long id,
                                     @RequestBody CommentRecord commentRecord) {
        return adsService.addComments(id, commentRecord);
    }

    @Operation(
            summary = "getFullAd",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "getFullAd",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FullAds.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @GetMapping("/{id}")
    public FullAds getFullAd(@PathVariable Long id) {
        return adsService.getFullAd(id);
    }

    @Operation(
            summary = "removeAds",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "removeAds"
                    )
            },
            tags = "Объявления"
    )
    @DeleteMapping("/{id}")
    public void removeAds(@PathVariable Long id) {
        adsService.removeAds(id);
    }

    @Operation(
            summary = "updateAds",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updateAds",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @PatchMapping("/{id}")
    public AdsRecord updateAds(@PathVariable Long id,
                               @RequestBody CreateAds createAds) {
        return adsService.updateAds(id, createAds);
    }

    @Operation(
            summary = "getCommentsAd",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "getCommentsAd",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @GetMapping("/{adId}/comments/{commentId}")
    public CommentRecord getCommentsAd(@PathVariable Long adId,
                                       @PathVariable Long commentId) {
        return adsService.getCommentsAd(adId, commentId);
    }

    @Operation(
            summary = "deleteComments",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "deleteComments"
                    )
            },
            tags = "Объявления"
    )
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComments(@PathVariable Long adId,
                               @PathVariable Long commentId) {
        adsService.deleteComments(adId, commentId);
    }

    @Operation(
            summary = "updateComments",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updateComments",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @PatchMapping("/{adId}/comments/{commentId}")
    public CommentRecord updateComments(@PathVariable Long adId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentRecord commentRecord) {
        return adsService.updateComments(adId, commentId, commentRecord);
    }

    @Operation(
            summary = "getAdsMe",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "getAdsMe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @GetMapping(path = "/me")
    public ResponseWrapperAds getAdsMe() {
        return adsService.getAdsMe();
    }

    @Operation(
            summary = "updateAdsImage",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updateAdsImage",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentRecord.class)
                            )
                    )
            },
            tags = "Объявления"
    )
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/{id}/image")
    public String updateAdsImage(@PathVariable Long id,
                                 @RequestPart("image") MultipartFile multipartFile) {
        return adsService.updateAdsImage(id, multipartFile);
    }

    @Operation(
            summary = "readImage",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "readImage"
                    )
            },
            tags = "Объявления"
    )
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> readImage(@Parameter(description = "Введите id image", example = "1")
                                            @PathVariable Long id) {
        Pair<String, byte[]> pair = imageService.readImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getFirst()))
                .contentLength(pair.getSecond().length)
                .body(pair.getSecond());
    }


}
