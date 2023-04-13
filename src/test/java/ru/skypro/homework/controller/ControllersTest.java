package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.component.DtoMapper;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.*;
import ru.skypro.homework.repository.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllersTest {
    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AdsRepository adsRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DtoMapper dtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private UsernamePasswordAuthenticationToken principal;
    private final Faker faker = new Faker();


    @BeforeEach
    public void beforeEach() {
        User user = register(generateUser());

        principal = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(principal);
    }

    @AfterEach
    public void afterEach() {
        commentRepository.deleteAll();
        adsRepository.deleteAll();
        userRepository.deleteAll();
        avatarRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    public void registerNegativeTest() {
        RegisterReq registerReq = generateUser();
        registerReq.setUsername("user2@gmail.com");
        registerReq.setPassword("password");

        register(registerReq);

        ResponseEntity<User> registerNegativeReqResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/register", registerReq, User.class);

        assertThat(registerNegativeReqResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForLogin")
    public void loginTest(String login, String password, HttpStatus httpStatus) {
        RegisterReq registerReq = generateUser();
        registerReq.setUsername("user2@gmail.com");
        registerReq.setPassword("password");

        User user = register(registerReq);

        principal = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(principal);

        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(login);
        loginReq.setPassword(password);

        ResponseEntity<User> loginReqResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/login", loginReq, User.class);

        assertThat(loginReqResponseEntity.getStatusCode()).isEqualTo(httpStatus);
    }

    @Test
    public void setPasswordTest() throws Exception {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("password");
        newPassword.setNewPassword("password10");

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:" + port + "/users/set_password")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    NewPassword newPasswordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), NewPassword.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(newPasswordResult).isNotNull();
                    assertThat(newPasswordResult).isEqualTo(newPassword);
                });
    }

    @Test
    public void getUserTest() throws Exception {
        RegisterReq registerReq = generateUser();
        registerReq.setUsername("user2@gmail.com");
        registerReq.setPassword("password");
        User user = register(registerReq);
        UserRecord userRecord = dtoMapper.toUserDto(user);

        principal = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(principal);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/users/me")
                        .principal(principal)
                )
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    UserRecord userRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), UserRecord.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(userRecordResult).isNotNull();
                    assertThat(userRecordResult).isEqualTo(userRecord);
                });
    }

    @Test
    public void updateUserTest() throws Exception {
        UserRecord userRecord = new UserRecord();
        userRecord.setFirstName("Petr");
        userRecord.setLastName("Romanov");
        userRecord.setPhone("+79568524175");

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("http://localhost:" + port + "/users/me")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRecord)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    UserRecord userRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), UserRecord.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(userRecordResult).isNotNull();
                    assertThat(userRecordResult).usingRecursiveComparison().ignoringFields("id", "email", "regDate", "image").isEqualTo(userRecord);
                });
    }

    @Test
    public void updateUserImageTest() throws Exception {
        MockMultipartHttpServletRequestBuilder builder
                = MockMvcRequestBuilders.multipart("http://localhost:" + port + "/users/me/image");
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        MockMultipartFile file
                = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                new byte[1024]
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(builder
                        .file(file)
                        .principal(principal))
                .andExpect(status().isOk());

        User user = userRepository.findByUsername(principal.getName());

        Path path = Paths.get(user.getAvatar().getFilePath());
        Files.delete(path);
    }

    @Test
    public void readAvatarTest() throws Exception {
        Avatar avatar = generateAvatar();

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/users/" + avatar.getId() + "/avatar")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getWrapperAds() {
        List<AdsRecord> ads = Stream.generate(() -> generateAds(register(generateUser()), generateImage()))
                .limit(10)
                .map(dtoMapper::toAdsDto)
                .toList();

        ResponseEntity<ResponseWrapperAds> getAllAdsResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/ads/",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                });

        assertThat(getAllAdsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getAllAdsResponseEntity.getBody()).getResults())
                .hasSize(ads.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(ads);
    }

    @Test
    public void addAdsAndRemoveAdsAndUpdateAdsAndUpdateAdsImageTest() throws Exception {
        CreateAds createAds = new CreateAds();
        createAds.setPrice(1000);
        createAds.setTitle("Nail Hammer");
        createAds.setDescription("for carpentry");

        MockMultipartFile image
                = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                new byte[1024]
        );

        MockMultipartFile body
                = new MockMultipartFile(
                "properties",
                "properties",
                "application/json",
                objectMapper.writeValueAsString(createAds).getBytes()
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("http://localhost:" + port + "/ads")
                        .file(image)
                        .file(body)
                        .principal(principal))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    AdsRecord adsRecordResponse = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), AdsRecord.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(adsRecordResponse).isNotNull();
                    assertThat(adsRecordResponse.getPrice()).isEqualTo(createAds.getPrice());
                    assertThat(adsRecordResponse.getTitle()).isEqualTo(createAds.getTitle());
                });

        Ads ads = adsRepository.findByTitle(createAds.getTitle());


        createAds.setPrice(3500);
        createAds.setTitle("frying pan");
        createAds.setDescription("teflon coating");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/" + ads.getId())
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAds)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    AdsRecord adsRecordResponse = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), AdsRecord.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(adsRecordResponse).isNotNull();
                    assertThat(adsRecordResponse.getPrice()).isEqualTo(createAds.getPrice());
                    assertThat(adsRecordResponse.getTitle()).isEqualTo(createAds.getTitle());
                });

        MockMultipartHttpServletRequestBuilder builder
                = MockMvcRequestBuilders.multipart("http://localhost:" + port + "/ads/" + ads.getId() + "/image");
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        mockMvc.perform(builder
                        .file(image)
                        .principal(principal))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/" + ads.getId())
                        .principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    public void getComments() throws Exception {
        Ads ads = generateAds(register(generateUser()), generateImage());
        List<CommentRecord> comments = Stream.generate(() -> generateComment(register(generateUser()), ads))
                .limit(10)
                .map(dtoMapper::toCommentDto)
                .toList();

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/ads/" + ads.getId() + "/comments")
                        .principal(principal)
                )
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    ResponseWrapperComment responseWrapperComment = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), ResponseWrapperComment.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(responseWrapperComment).isNotNull();
                    assertThat(responseWrapperComment.getResults())
                            .hasSize(comments.size())
                            .usingRecursiveFieldByFieldElementComparator()
                            .containsExactlyInAnyOrderElementsOf(comments);
                });
    }

    @Test
    public void addCommentsAndGetCommentAndDeleteCommentsAndUpdateCommentsTest() throws Exception {
        Ads ads = generateAds(register(generateUser()), generateImage());
        CommentRecord commentRecord = new CommentRecord();
        commentRecord.setText("great product");


        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:" + port + "/ads/" + ads.getId() + "/comments")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRecord))
                )
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    CommentRecord commentRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), CommentRecord.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(commentRecordResult).isNotNull();
                    assertThat(commentRecordResult.getText()).isEqualTo(commentRecord.getText());
                });

        Comment comment = commentRepository.findByText(commentRecord.getText());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/ads/" + ads.getId() + "/comments/" + comment.getId()))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    CommentRecord commentRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), CommentRecord.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(commentRecordResult).isNotNull();
                    assertThat(commentRecordResult.getText()).isEqualTo(commentRecord.getText());
                });

        commentRecord.setText("terrible product");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("http://localhost:" + port + "/ads/" + ads.getId() + "/comments/" + comment.getId())
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRecord))
                )
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    CommentRecord commentRecordResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), CommentRecord.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(commentRecordResult).isNotNull();
                    assertThat(commentRecordResult.getText()).isEqualTo(commentRecord.getText());
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/" + ads.getId() + "/comments/" + comment.getId())
                        .principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    public void getFullAdTest() throws Exception {
        Ads ads = generateAds(register(generateUser()), generateImage());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/ads/" + ads.getId())
                        .principal(principal)
                )
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    FullAds fullAdsResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), FullAds.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(fullAdsResult).isNotNull();
                    assertThat(fullAdsResult.getAuthorFirstName()).isEqualTo(ads.getUser().getFirstName());
                    assertThat(fullAdsResult.getAuthorLastName()).isEqualTo(ads.getUser().getLastName());
                    assertThat(fullAdsResult.getDescription()).isEqualTo(ads.getDescription());
                    assertThat(fullAdsResult.getEmail()).isEqualTo(ads.getUser().getEmail());
                    assertThat(fullAdsResult.getPhone()).isEqualTo(ads.getUser().getPhone());
                    assertThat(fullAdsResult.getTitle()).isEqualTo(ads.getTitle());
                });
    }

    @Test
    public void getAdsMeTest() throws Exception {
        User user = userRepository.findByUsername(principal.getName());

        List<AdsRecord> ads = Stream.generate(() -> generateAds(user, generateImage()))
                .limit(10)
                .map(dtoMapper::toAdsDto)
                .toList();

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/ads/me")
                        .principal(principal))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    ResponseWrapperAds responseWrapperAdsResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString().getBytes(StandardCharsets.UTF_8), ResponseWrapperAds.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(responseWrapperAdsResult).isNotNull();
                    assertThat(responseWrapperAdsResult.getResults()).hasSize(ads.size())
                            .usingRecursiveFieldByFieldElementComparator()
                            .containsExactlyInAnyOrderElementsOf(ads);
                });
    }

    @Test
    public void readImageTest() throws Exception {
        Image image = generateImage();

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/ads/" + image.getId() + "/image")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private RegisterReq generateUser() {
        RegisterReq registerReq = new RegisterReq();
        registerReq.setUsername(faker.internet().emailAddress());
        registerReq.setPassword(faker.internet().password());
        registerReq.setRole(Role.USER);
        registerReq.setFirstName(faker.name().firstName());
        registerReq.setLastName(faker.name().lastName());
        registerReq.setPhone(faker.phoneNumber().phoneNumber());
        return registerReq;
    }

    private Avatar generateAvatar() {
        Avatar avatar = new Avatar();
        avatar.setData(new byte[1024]);
        avatar.setMediaType("image/jpeg");
        return avatarRepository.save(avatar);
    }

    private Image generateImage() {
        Image image = new Image();
        image.setData(new byte[1024]);
        image.setMediaType("image/jpeg");
        Image image2 = imageRepository.save(image);
        image2.setFilePath("imagesTest\\" + image2.getId() + ".jpg");
        return imageRepository.save(image2);
    }

    private Comment generateComment(User user, Ads ads) {
        Comment comment = new Comment();
        comment.setText(faker.weather().description());
        comment.setCreatedAt(LocalDateTime.now());
        if (user != null) {
            comment.setUser(user);
        }
        if (ads != null) {
            comment.setAds(ads);
        }
        return commentRepository.save(comment);
    }

    private Ads generateAds(User user, Image image) {
        Ads ads = new Ads();
        ads.setTitle(faker.commerce().productName());
        ads.setPrice(BigDecimal.valueOf(faker.random().nextLong(1000)));
        ads.setDescription(faker.weather().description());
        if (user != null) {
            ads.setUser(user);
        }
        if (image != null) {
            ads.setImage(image);
        }
        return adsRepository.save(ads);
    }

    private User register(RegisterReq registerReq) {
        ResponseEntity<User> registerReqResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/register", registerReq, User.class);
        assertThat(registerReqResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        User user = userRepository.findByUsername(registerReq.getUsername());
        assertThat(user).isNotNull();
        assertThat(user).usingRecursiveComparison().ignoringFields("id", "email", "regDate", "avatar", "comments", "ads", "enabled", "password").isEqualTo(registerReq);
        return user;
    }

    public static Stream<Arguments> provideParamsForLogin() {
        return Stream.of(
                Arguments.of("user2@gmail.com", "password", HttpStatus.OK),
                Arguments.of("user3@gmail.com", "password", HttpStatus.FORBIDDEN),
                Arguments.of("user2@gmail.com", "password10", HttpStatus.FORBIDDEN)
        );
    }

}