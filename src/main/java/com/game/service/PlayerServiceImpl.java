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

import java.util.Calendar;
import java.util.Optional;

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

    private Long validateId(String id) {
        try {
            Long idLong = Long.parseLong(id);
            if (idLong <= 0) return null;
            else return idLong;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    @Override
    public ResponseEntity<?> findById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Player> player = playerRepository.findById(id);

        if (!player.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(player.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> existsById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(playerRepository.existsById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!playerRepository.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            playerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> update(String idString, Player player) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //Если элемента с таким id нет в базе
        if (!playerRepository.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            Player playerForUpdate = playerRepository.findById(id).get();

            // Если тело запроса пустое
            if (player.getName() == null && player.getTitle() == null
                    && player.getRace() == null && player.getExperience() == null
                    && player.getBirthday() == null && player.getBanned() == null && player.getExperience() == null)
                return new ResponseEntity<>(playerForUpdate, HttpStatus.OK);
            else {
                if (!ServiceHelper.validatingFieldsForUpdate(player)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

                //Заменяем непустыми значениями
                ServiceHelper.updatingNotEmptyFields(player,playerForUpdate);

                playerRepository.save(playerForUpdate);
            }
            return new ResponseEntity<>(playerForUpdate, HttpStatus.OK);
        }
    }
}
