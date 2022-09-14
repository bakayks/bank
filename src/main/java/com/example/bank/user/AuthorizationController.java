package com.example.bank.user;

import com.example.bank.model.AuthenticationModel;
import com.example.bank.model.AuthenticationRequestModel;
import com.example.bank.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationModel> generateToken(@RequestBody AuthenticationRequestModel authRequest){
        User user = null;
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        user = userRepository.getByUsername(authRequest.getUsername());

        if (user != null) {
            return new ResponseEntity<>(AuthenticationModel.builder()
                    .username(user.getUsername())
                    .jwtToken(jwtUtil.generateToken(authRequest.getUsername()))
                    .cashBoxId(user.getCashBox().getId())
                    .build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
