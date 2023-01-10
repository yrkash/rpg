package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

public class ServiceHelper {

    public static List<Player> findByPagingCriteria(PlayerService playerService,
                                                    String name, String title, Race race,
                                                    Profession profession, Long after, Long before,
                                                    Boolean banned,
                                                    Integer minExperience, Integer maxExperience,
                                                    Integer minLevel, Integer maxLevel,
                                                    PlayerOrder order,
                                                    Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page page = playerService.findAll(new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                if (title != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"), "%" + title + "%")));
                }
                if (race != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("race"), race)));
                }
                if (profession != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("profession"), profession)));
                }
                if (after != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after))));
                }
                if (before != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("birthday"), new Date(before))));
                }
                if (banned != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("banned"), banned)));
                }
                if (minExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience)));
                }
                if (maxExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience)));
                }
                if (minLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel)));
                }
                if (maxLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel)));
                }
                if (order != null) {
                    query.orderBy(criteriaBuilder.asc(root.get(order.getFieldName())));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        return page.getContent();
    }

    public static Integer countByCriteria(PlayerService playerService,
                                          String name, String title,
                                          Race race, Profession profession,
                                          Long after, Long before,
                                          Boolean banned,
                                          Integer minExperience, Integer maxExperience,
                                          Integer minLevel, Integer maxLevel) {
        return (int) playerService.count(new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                if (title != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"), "%" + title + "%")));
                }
                if (race != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("race"), race)));
                }
                if (profession != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("profession"), profession)));
                }
                if (after != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after))));
                }
                if (before != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("birthday"), new Date(before))));
                }
                if (banned != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("banned"), banned)));
                }
                if (minExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience)));
                }
                if (maxExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience)));
                }
                if (minLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel)));
                }
                if (maxLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

    public static boolean validatePlayer(Player player) {

        if (player.getName() == null
                || player.getTitle() == null
                || player.getRace() == null
                || player.getProfession() == null
                || player.getBirthday() == null
                || player.getExperience() == null)
            return false;

        if (player.getBanned() == null) player.setBanned(false);
        if (!validatingFields(player)) return false;

        enrichPlayer(player);

        return true;
    }

    //Set level and untilNextLevel for player by experience
    private static void enrichPlayer(Player player) {
        Double levelCalc = Math.ceil((Math.sqrt(player.getExperience() * 200 + 2500) - 50) / 100);
        Integer level = levelCalc.intValue();
        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);
    }

    public static Boolean validatingFields(Player player) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(player.getBirthday());

        if (player.getName().length() > 12 || player.getTitle().length() > 30
                || player.getName().equals("") || player.getTitle().equals("")
                || player.getExperience() < 0 || player.getExperience() > 1000000
                || calendar.get(Calendar.YEAR) < 2000 || calendar.get(Calendar.YEAR) > 3000)
            return false;
        else return true;
    }

    public static Boolean validatingFieldsForUpdate(Player player) {

        Calendar calendar = new GregorianCalendar();
        if (player.getBirthday() != null ) calendar.setTime(player.getBirthday());

        if ((player.getName() != null && (player.getName().length() > 12 || player.getName().equals("")))
                || (player.getTitle() != null && (player.getTitle().length() > 30 || player.getTitle().equals("")))
                || (player.getExperience() != null && (player.getExperience() < 0 || player.getExperience() > 1000000))
                || (player.getBirthday() != null && (calendar.get(Calendar.YEAR) < 2000 || calendar.get(Calendar.YEAR) > 3000)))
            return false;
        return true;
    }

    public static void updatingNotEmptyFields(Player player, Player playerForUpdate) {

        if (player.getName() != null) playerForUpdate.setName(player.getName());
        if (player.getTitle() != null) playerForUpdate.setTitle(player.getTitle());
        if (player.getRace() != null) playerForUpdate.setRace(player.getRace());
        if (player.getProfession() != null) playerForUpdate.setProfession(player.getProfession());
        if (player.getBirthday() != null) playerForUpdate.setBirthday(player.getBirthday());
        if (player.getBanned() != null) playerForUpdate.setBanned(player.getBanned());
        if (player.getExperience() != null) playerForUpdate.setExperience(player.getExperience());
        enrichPlayer(playerForUpdate);
    }


}
