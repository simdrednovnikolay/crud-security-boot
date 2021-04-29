package com.example.crudsecurityboot.controller;

import com.example.crudsecurityboot.dao.RoleRepository;
import com.example.crudsecurityboot.model.Role;
import com.example.crudsecurityboot.model.User;
import com.example.crudsecurityboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
    public String userPage() {
        return "pageForUser";
    }




    @GetMapping(value = "/admin")
    public  String showAllUser(Model model) {
        List<User> allUs = userService.getAllUsers();
        model.addAttribute("allUs",allUs);
        model.addAttribute("allR", roleRepository.findAll());
        model.addAttribute("addUser", new User());
        return "admin";
    }



    @PostMapping(value = "/adNewUser")
    public String addNewUser (@ModelAttribute("addUser") User user,
                              @RequestParam(value = "roles", required = false) String[] role) {

        userService.updateUserAndRoles(user, role);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editUser (@RequestParam(value = "id", required = false) Long id,
                            @RequestParam(value = "firstName", required = false) String name,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "roles",required = false) String roles, Model model) {

        model.addAttribute("allR", roleRepository.findAll());
        User user = userService.getUserId(id);
        user.setName(name);
        user.setPassword(password);

        Set<Role> roleSet = new HashSet<>();
        if (roles.contains("ROLE_USER")){
            roleSet.add(new Role("ROLE_USER"));
            user.setRoles(roleSet);
        }
        if (roles.contains("ROLE_ADMIN")) {
            roleSet.add(new Role("ROLE_ADMIN"));
            user.setRoles(roleSet);
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

//    @RequestMapping(value = "/userUpdate/{id}",  method = {RequestMethod.GET, RequestMethod.POST})
//    public String edit(Model model, @PathVariable(value = "id") Long id) {
//        model.addAttribute("users", userService.getUserId(id));
//        model.addAttribute("roles", userService.getAllRoles());
//
//        return "redirect:/admin";
//    }

//    @PostMapping("/edit")
//    public String updateUser(@ModelAttribute("users") User user,
//                             @RequestParam(value = "roles", required = false)String [] role,
//                                @PathVariable("id") Long id) {
//
//
//        userService.updateUserAndRoles(user, role);
//        return "redirect:/admin";
//    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("idDelete") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

//    @GetMapping("/findById")
//    @ResponseBody
//    public User findById(Long id) {
//        return userService.getUserId(id);
//    }
}
