package net.javaguides.springboot.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.springboot.dto.UserDto;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.exception.EmailAlreadyExistsException;
import net.javaguides.springboot.exception.ErrorDetails;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.mapper.AutoUserMapper;
import net.javaguides.springboot.mapper.UserMapper;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private ModelMapper modelMapper;
    private UserRepository userRepository;
    @Override
    public UserDto createUser(UserDto user) {
//        User changedUser = UserMapper.maptoUser(user);
//        User changedUser = modelMapper.map(user,User.class);
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("Email already exists for the following user.");
        }
        User changedUser = AutoUserMapper.MAPPER.mapToUser(user);
        User savedUser = userRepository.save(changedUser); // provides primary keys too.
//        UserDto dtoChanged = UserMapper.maptoUserDto(savedUser);
//        UserDto dtoChanged = modelMapper.map(savedUser,UserDto.class);
        UserDto dtoChanged = AutoUserMapper.MAPPER.mapToUserDto(savedUser);
        return dtoChanged;
    }

    @Override
    public UserDto getUserById(Long userId) {
//        Optional<User> optionalUser= userRepository.findById(userId);
        User optionalUser= userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User","id",userId)
        );
//UserMapper.maptoUserDto(optionalUser.get());
//    }
//        UserMapper.maptoUserDto(optionalUser.get())
//        UserDto changedUser =  modelMapper.map(optionalUser.get(),UserDto.class);
        UserDto changedUser =  AutoUserMapper.MAPPER.mapToUserDto(optionalUser);

        return changedUser;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
// users.stream().map(UserMapper::maptoUserDto).collect(Collectors.toList());
//        return  users.stream().map((user) -> modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
        return  users.stream().map((user) -> AutoUserMapper.MAPPER.mapToUserDto(user)).collect(Collectors.toList());

    }

    @Override
    public UserDto updateUser(UserDto user) {
        User targetUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User","id",user.getId())
        );
        targetUser.setFirstName(user.getFirstName());
        targetUser.setLastName(user.getLastName());
        targetUser.setEmail(user.getEmail());
        User updatedUser  = userRepository.save(targetUser);
//        return UserMapper.maptoUserDto(updatedUser);
//        return modelMapper.map(updatedUser,UserDto.class);
        return AutoUserMapper.MAPPER.mapToUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User checkUser = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User","id",userId)

        );
        userRepository.deleteById(userId)
        ;
    }


}
