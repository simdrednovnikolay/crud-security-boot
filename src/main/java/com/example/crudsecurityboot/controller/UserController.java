package com.example.crudsecurityboot.controller;

import com.example.crudsecurityboot.dao.RoleRepository;
import com.example.crudsecurityboot.model.User;
import com.example.crudsecurityboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    private final RoleRepository roleRepository;




    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;

        this.roleRepository = roleRepository;
    }

    @GetMapping("/pageForUser")
    public String userPage() {
        return "pageForUser";
    }

    @GetMapping("/admin")
    public  String showAllUser(Model model) {
        List<User> allUs = userService.getAllUsers();
        model.addAttribute("allUs",allUs);
        return "admin";
    }



    @GetMapping("/addNewUser")
    public String addNewUser (Model model) {
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("user", new User());
        return "addNewUser";
    }
    @PostMapping("/saveUser")
    public String saveUser (@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/userUpdate/{id}")
    public String edit(Model model, @PathVariable(value = "id") Long id) {
        model.addAttribute("user", userService.getUserId(id));
        model.addAttribute("roles", userService.getAllRoles());

        return "userUpdate";
    }

    @PostMapping("/userUpdate")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "roles", required = false)String [] roleList) {
        userService.updateUserAndRoles(user, roleList);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
