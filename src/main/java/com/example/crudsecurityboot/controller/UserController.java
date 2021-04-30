package com.example.crudsecurityboot.controller;

import com.example.crudsecurityboot.dao.RoleRepository;
import com.example.crudsecurityboot.model.Role;
import com.example.crudsecurityboot.model.User;
import com.example.crudsecurityboot.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class UserController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;

        this.roleRepository = roleRepository;
    }

    @GetMapping("/pageForUser")
    public String userPage( Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userInPage = authentication.getName();
        model.addAttribute("user", userService.getByName(userInPage));
        return "pageForUser";
    }

    @GetMapping("/admin")
    public  String showAllUser(Model model) {
        List<User> allUs = userService.getAllUsers();
        model.addAttribute("allUs",allUs);
        model.addAttribute("allRol",roleRepository.findAll());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping(value = "/adNewUser")
    public String addNewUser (@ModelAttribute("newUser") User user,
                              @RequestParam(value = "roles", required = false) String[] role) {

        userService.updateUserAndRoles(user, role);
        return "redirect:/admin";
    }

    @PostMapping("/userUpdate/{id}")
    public String editUser(@PathVariable(value = "id") Long id,
                           @RequestParam(value = "firstName", required = false) String firstName,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "roles", required = false) String[] role,
                           Model model) {

        model.addAttribute("allRole", userService.getAllRoles());
        User user = userService.getUserId(id);
        user.setName(firstName);
        user.setPassword(password);

        for(int i=0; i<role.length; i++) {
            Set<Role> roleSet = new HashSet<>();
            if (!user.getRoles().contains(role[i])){
                roleSet.add(userService.getByRole(role[i]));
                user.setRoles(roleSet);
            }
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
