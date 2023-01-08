package com.game.service;

import com.game.model.Player;
import com.game.repository.PlayerRepository;
import com.game.util.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll() {return playerRepository.findAll();}

    public Player findOne(Long id) {
        Optional<Player> foundPlayer = playerRepository.findById(id);
        return foundPlayer.orElseThrow(PlayerNotFoundException::new);
    }

    @Transactional
    public void save(Player player) {
        enrichPlayer(player);
        playerRepository.save(player);
    }

    private void enrichPlayer(Player player) {
        Double levelCalc = Math.ceil((Math.sqrt(player.getExperience() * 200 + 2500) - 50) / 100);
        Integer level = levelCalc.intValue();
        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);
    }

}
