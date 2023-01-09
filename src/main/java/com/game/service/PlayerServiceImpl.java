package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("playerService")
@Repository
@Transactional
public class PlayerServiceImpl implements PlayerService{

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Page<Player> findAll(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable);
    }

    @Override
    public long count(Specification<Player> specification) {
        return playerRepository.count(specification);
    }

    @Override
    public ResponseEntity<?> create(Player player) {
        if (!ServiceHelper.validatePlayer(player)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player createPlayer = playerRepository.save(player);
        return new ResponseEntity<>(createPlayer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> existsById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> findById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(String id, Player player) {
        return null;
    }
}
