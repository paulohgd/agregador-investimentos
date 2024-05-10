package tech.buildrun.agreagadorinvestimentos.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.buildrun.agreagadorinvestimentos.controller.CreateUserDto;
import tech.buildrun.agreagadorinvestimentos.entity.User;
import tech.buildrun.agreagadorinvestimentos.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    /*
    Nos nossos testes vamos utilizar o padrão triplo way.
    Triplo way significa. Basicamente nosso teste vai passar por 3 validações


        // Arrange (Vai arrumar e organizar tudo que precisamos para o nosso teste)
        // Act (Vai chamar o trecho que de fato queremos testar)
        // Assert (E fazemos todas as verificações para ver se foi executado como deveria)

    */
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser{
        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUserWithSuccess() {
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "password",
                    "mxzinhen@gmail.com",
                    Instant.now(),
                    null
            );


            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto(
                    "mxzinhen@gmail.com",
                    "dada",
                    "1234"
            );

            //Act
            var output = userService.CreateUser(input);

            //Assert
            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.email(),userCaptured.getEmail());
            assertEquals(input.username(),userCaptured.getUsername());
            assertEquals(input.password(),userCaptured.getPassword());

        }
        @Test
        @DisplayName("should throw exception when user already exists")
        void shouldThrowExcetionWhenErrorOccurs(){
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                    "pauloguerra@gmail.com",
                    "Paulo",
                    "333"
            );

            //Act & Assert
            assertThrows(RuntimeException.class, () -> userService.CreateUser(input));
        }

    }

    @Nested
    class getUserById{
        @Test
        @DisplayName("should get user by id with success when optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {
            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "password",
                    "mxzinhen@gmail.com",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());
            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }
        @Test
        @DisplayName("should get user by id with success when optional is Empty")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {
            // Arrange
            var userId = UUID.randomUUID();
            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());
            // Act
            var output = userService.getUserById(userId.toString());

            // Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }

    }

    @Nested
    class getAllUsers{
        @Test
        @DisplayName("Should get all users with success")
        void shouldGetAllUsersWithSuccess() {
            //Arrange
            var users = new User(
                    UUID.randomUUID(),
                    "username",
                    "password",
                    "mxzinhen@gmail.com",
                    Instant.now(),
                    null
            );
            var userList = List.of(users);
            doReturn(userList)
                    .when(userRepository)
                    .findAll();
            //ACT
            var output = userService.getAllUsers();
            //Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }
}