package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import com.game.util.PlayerErrorResponse;
import com.game.util.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<Player> getPlayer() {

        return playerService.findAll(); // Jackson конвертирует эти объекты в JSON
//        return null;
    }

    @GetMapping("/players/{id}")
    public Player getPerson(@PathVariable("id") Long id) {
        // Статус - 200
        return playerService.findOne(id);
    }





    @ExceptionHandler
    private ResponseEntity<PlayerErrorResponse> handleException(PlayerNotFoundException e) {
        PlayerErrorResponse response = new PlayerErrorResponse(
                "Player with this id wasn't found!", System.currentTimeMillis());

        //в HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }


    @PostMapping("/players")
    public ResponseEntity<HttpStatus> create(@RequestBody Player player, BindingResult bindingResult) {

        /*if (bindingResult.hasErrors()) {
            System.out.println("ERROR");
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PlayerNotCreatedException(errorMsg.toString());
        }
        playerService.save(player);*/
        // отправляем HTTP ответ с пустым телом и со статус 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
