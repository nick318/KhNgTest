package idv.imonahhov.khngtest;

import idv.imonahhov.khngtest.entities.User;
import idv.imonahhov.khngtest.repositories.UserRepository;
import idv.imonahhov.khngtest.requests.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/getWallets")
    public ResponseEntity getBalance(@RequestBody LoginRequest login){
        User user = userRepository.findUserByUsername(login.getUsername());
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user.getWallets());
    }

}
