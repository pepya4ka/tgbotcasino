package com.pepyachka.mysqldbspringboot;

import com.pepyachka.mysqldbspringboot.model.User;
import com.pepyachka.mysqldbspringboot.model.UserRole;
import com.pepyachka.mysqldbspringboot.repository.RoleRepository;
import com.pepyachka.mysqldbspringboot.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/demo") // This means URL's start with /demo (after Application path)
public class MainController {

    // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public MainController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    String addNewUser(@RequestParam User user) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        if (!userRepository.findById(user.getId()).isPresent()) {
            userRepository.save(user);
            return "Регистрация прошла успешно. Ваше количество монет - " + user.getCoins();
        }
        user = userRepository.findById(user.getId()).get();
        return "Вы уже зарегистрированы. Ваше количество монет - " + user.getCoins() + ". Введите вашу ставку";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    String getAllUsers(@RequestParam Integer id) {
        boolean fl = false;
        List<UserRole> userRoleList = roleRepository.getListUserRole();
        for (UserRole ur : userRoleList) {
            if (ur.getCustomer_id().intValue() == id.intValue()) {
                fl = true;
            }
        }
        if (fl) {
            Iterator<User> userIterator = userRepository.findAll().iterator();
            StringBuilder result = new StringBuilder();
            while (userIterator.hasNext()) {
                User tempUser = userIterator.next();
                result.append(tempUser.getId()).append(" ");
                result.append(tempUser.getUsername() != null ? tempUser.getUsername() : "GENERAL_USER").append(" ");
                result.append(tempUser.getCoins()).append(";\n");
            }
            return result.toString();
        } else {
            return "У вас нет прав для этой команды!";
        }
        // This returns a JSON or XML with the users
    }

    @GetMapping(path = "/getById")
    public @ResponseBody
    User getById(@RequestParam Integer id) {
        if (!userRepository.findById(id).isPresent()) {
            return null;
        } else {
            return userRepository.findById(id).get();
        }
    }

    @GetMapping(path = "/updateCoins")
    public @ResponseBody
    User updateCoins(@RequestParam Integer id, @RequestParam Integer coins) {
        User user = userRepository.findById(id).get();
        user.setCoins(user.getCoins() + coins);
        userRepository.save(user);
        return user;
    }

    @GetMapping(path = "/getCoins")
    public @ResponseBody
    String updateCoins(@RequestParam Integer id) {
        User user = userRepository.findById(id).get();
        return user.getCoins().toString();
    }
}
