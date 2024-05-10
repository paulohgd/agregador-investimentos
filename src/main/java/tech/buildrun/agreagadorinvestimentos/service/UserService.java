package tech.buildrun.agreagadorinvestimentos.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.buildrun.agreagadorinvestimentos.controller.CreateUserDto;
import tech.buildrun.agreagadorinvestimentos.controller.UpdateUserDto;
import tech.buildrun.agreagadorinvestimentos.entity.User;
import tech.buildrun.agreagadorinvestimentos.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID CreateUser(CreateUserDto createUserDto){
        //PARA FAZER A INSERÇÃO VAMOS CONVERTER DE DTO PARA ENTITY
        var entity = new User(UUID.randomUUID(),
                createUserDto.email(),
                createUserDto.username(),
                createUserDto.password(),
                Instant.now(),
                null);
        var userSaved = userRepository.save(entity);
        return userSaved.getUserId();
    }
    public Optional<User> getUserById(String userId){
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void updateUserById(String userId,
                               UpdateUserDto updateUserDto){
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();

            if(updateUserDto.username() != null){
                user.setUsername(updateUserDto.username());
            }
            if(updateUserDto.email() != null){
                user.setEmail(updateUserDto.email());
            }
            if(updateUserDto.password() != null){
                user.setPassword(updateUserDto.password());
            }
            userRepository.save(user);
        }
    }


    public void deleteUserById(String userId){
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);

        if (userExists){
            userRepository.deleteById(id);
        }
    }

}
