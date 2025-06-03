package bees.io.Berzza.controllers;

import bees.io.Berzza.domain.dto.NewUserDTO;
import bees.io.Berzza.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    @GetMapping("/me/balance")
    public Double getBalance(@RequestParam String userId) {
        return userService.getBalance(userId);
    }
    @PostMapping()
    public void saveUser(@RequestBody NewUserDTO user) {
        userService.saveUser(user.username());
    }
}
