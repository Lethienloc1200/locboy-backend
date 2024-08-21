package com.locboy.locboybackend.services;
import com.locboy.locboybackend.dtos.UserDTO;
import com.locboy.locboybackend.exceptions.DataNotFoundException;
import com.locboy.locboybackend.models.Role;
import com.locboy.locboybackend.models.User;
import com.locboy.locboybackend.repositories.RoleRepository;
import com.locboy.locboybackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {

        if(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())){
            throw new DataNotFoundException("Phone Number already exists");
        }
//        map UserDTO to User
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(()->new DataNotFoundException("Role not found"));

        User  newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .role(role)
                .build();
        if(userDTO.getFacebookAccountId() ==0 && userDTO.getFacebookAccountId() ==0){
            String password = userDTO.getPassword();
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        return null;
    }
}