package ru.skypro.homework.component;

import org.mapstruct.*;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface DtoMapper {

    @Mapping(target = "regDate", source = "regDate", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "image", expression = "java(user.getAvatar() != null ? \"http://localhost:8080/user/\" + user.getAvatar().getId() + \"/avatar\" : null)")
    UserRecord toUserDto(User user);

    @Mapping(target = "regDate", source = "regDate", dateFormat = "dd.MM.yyyy HH:mm:ss")
    User toUserEntity(UserRecord userRecord);

    RegisterReq toRegisterReq(User user);

    @Mapping(target = "author", expression = "java(ads.getUser() != null ? ads.getUser().getFirstName() + \" \" + ads.getUser().getLastName() : null)")
    @Mapping(target = "image", expression = "java(ads.getImage() != null ? \"http://localhost:8080/ads/\" + ads.getImage().getId() + \"/image\" : null)")
    AdsRecord toAdsDto(Ads ads);

    CreateAds toCreateAds(Ads ads);

    @Mapping(target = "authorFirstName", expression = "java(ads.getUser() != null ? ads.getUser().getFirstName() : null)")
    @Mapping(target = "authorLastName", expression = "java(ads.getUser() != null ? ads.getUser().getLastName() : null)")
    @Mapping(target = "email", expression = "java(ads.getUser() != null ? ads.getUser().getEmail() : null)")
    @Mapping(target = "image", expression = "java(ads.getImage() != null ? \"http://localhost:8080/ads/\" + ads.getImage().getId() + \"/image\" : null)")
    @Mapping(target = "phone", expression = "java(ads.getUser() != null ? ads.getUser().getPhone() : null)")
    FullAds toFullAds(Ads ads);

    @Mapping(target = "author", expression = "java(comment.getUser() != null ? comment.getUser().getFirstName() + \" \" + comment.getUser().getLastName() : null)")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    CommentRecord toCommentDto(Comment comment);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    Comment toCommentEntity(CommentRecord commentRecord);
}
