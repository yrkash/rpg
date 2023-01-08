package com.game.util;

import com.game.entity.Player;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PlayerValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player) target;
        if (player.getName() == null || player.getName().length() > 12)
            errors.rejectValue("name", HttpStatus.BAD_REQUEST.name(), "Name should be not empty and no longer than 12 characters");
    }
}
