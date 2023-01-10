package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.service.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping(value = "/players")
    public ResponseEntity<?> getAllBy(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "race", required = false) Race race,
                                      @RequestParam(value = "profession", required = false) Profession profession,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "banned", required = false) Boolean banned,
                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                      @RequestParam(value = "order", required = false) PlayerOrder order,
                                      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        List<Player> playerList = ServiceHelper.findByPagingCriteria(playerService,
                name, title, race, profession,
                after, before,
                banned,
                minExperience, maxExperience,
                minLevel, maxLevel,
                order,
                pageNumber, pageSize);

        return new ResponseEntity<>(playerList, HttpStatus.OK);
    }
    @GetMapping(value = "/players/count")
    public ResponseEntity<?> getCount(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "race", required = false) Race race,
                                      @RequestParam(value = "profession", required = false) Profession profession,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "banned", required = false) Boolean banned,
                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        int count = ServiceHelper.countByCriteria(playerService, name, title,
                race, profession,
                after, before,
                banned,
                minExperience, maxExperience,
                minLevel, maxLevel);

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping(value = "/players")
    public ResponseEntity<?> create(@RequestBody Player player) {
        return playerService.create(player);
    }

    @GetMapping(value = "/players/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable String id) {
        return playerService.findById(id);
    }

    @DeleteMapping(value = "/players/{id}")
    public ResponseEntity<?> deletePlayerById(@PathVariable String id) {
        return playerService.deleteById(id);
    }

    @PostMapping(value = "/players/{id}")
    public ResponseEntity<?> updatePlayerById(@PathVariable String id, @RequestBody Player player) {
        ResponseEntity<?> responseEntity = playerService.update(id, player);
        return responseEntity;
    }



}
