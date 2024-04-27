package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Controller class handling CRUD operations for users.
 */
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Handler for displaying the list of users.
     *
     * @param model The Model object to add attributes.
     * @return The view name for the list of users.
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    /**
     * Handler for displaying the form to add a new user.
     *
     * @param user The User object to be populated in the form.
     * @return The view name for the add user form.
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }


    /**
     * Handler for validating and saving a new user.
     *
     * @param user   The User object to be validated and saved.
     * @param result The BindingResult for validation errors.
     * @param model  The Model object to add attributes.
     * @return The view name to redirect to after saving the user.
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            if (userRepository.existsByUsername(user.getUsername())) {
                redirectAttributes.addFlashAttribute("usernameError", "Ce nom d'utilisateur est déjà utilisé.");
                return "redirect:/user/add";
            }

            // Vérification des champs sensibles
            if (!isValidUsername(user.getUsername())) {
                redirectAttributes.addFlashAttribute("usernameError", "Le nom d'utilisateur est invalide.");
                return "redirect:/user/add";
            }
            if (!isValidPassword(user.getPassword())) {
                redirectAttributes.addFlashAttribute("passwordError", "Le mot de passe doit contenir au moins 8 caractères, dont au moins un chiffre et une majuscule.");
                return "redirect:/user/add";
            }
            if (user.getRole() == null || user.getRole().isEmpty()) {
                redirectAttributes.addFlashAttribute("roleError", "Veuillez sélectionner un rôle pour l'utilisateur.");
                return "redirect:/user/add";
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            model.addAttribute("users", userRepository.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    /**
     * Méthode pour valider le nom d'utilisateur
     * @param username
     * @return
     */
    private boolean isValidUsername(String username) {
        // Ajoutez vos règles de validation ici
        return username != null && username.matches("[a-zA-Z0-9]+");
    }
    /**
     * Method to check if the password meets the required criteria.
     *
     * @param password The password to be validated.
     * @return True if the password meets the criteria, false otherwise.
     */
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 &&
                password.matches(".*\\d.*") && // Vérifie la présence d'au moins un chiffre
                password.matches(".*[A-Z].*"); // Vérifie la présence d'au moins une majuscule
    }


    /**
     * Handler for displaying the form to update an existing user.
     *
     * @param id    The ID of the user to be updated.
     * @param model The Model object to add attributes.
     * @return The view name for the update user form.
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // Récupérer l'ID de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Récupérer l'utilisateur à mettre à jour
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Vérifier si l'utilisateur actuellement connecté est autorisé à mettre à jour cet utilisateur
        if (!user.getUsername().equals(currentPrincipalName)) {
            // L'utilisateur actuel n'est pas autorisé à mettre à jour cet utilisateur
            throw new IllegalArgumentException("Accès non autorisé à cette ressource.");
        }

        // Retirer le mot de passe pour des raisons de sécurité
        user.setPassword("");

        // Ajouter l'utilisateur au modèle et afficher le formulaire de mise à jour
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Handler for updating an existing user.
     *
     * @param id     The ID of the user to be updated.
     * @param user   The User object with updated information.
     * @param result The BindingResult for validation errors.
     * @param model  The Model object to add attributes.
     * @return The view name to redirect to after updating the user.
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        // Vérifier les erreurs de validation dans le formulaire
        if (result.hasErrors()) {
            // Retourner à la vue avec les erreurs de validation
            return "user/update";
        }

        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Récupérer l'utilisateur à mettre à jour
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Vérifier si l'utilisateur actuellement connecté est autorisé à mettre à jour cet utilisateur
        if (existingUser.getUsername().equals(currentPrincipalName)) {
            // L'utilisateur actuel ne peut pas mettre à jour son propre utilisateur
            result.addError(new FieldError("user", "username", "Vous ne pouvez pas mettre à jour votre propre utilisateur."));
            return "user/update";
        }

        // Mettre à jour les informations de l'utilisateur
        existingUser.setFullname(user.getFullname());
        // Ne pas mettre à jour le mot de passe si le champ est vide dans le formulaire
        if (!user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existingUser.setRole(user.getRole());

        // Enregistrer les modifications dans la base de données
        userRepository.save(existingUser);

        // Redirection vers la liste des utilisateurs après la mise à jour
        return "redirect:/user/list";
    }

    /**
     * Handler for deleting an existing user.
     *
     * @param id    The ID of the user to be deleted.
     * @return The view name to redirect to after deleting the user.
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Récupérer l'utilisateur à supprimer
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Vérifier si l'utilisateur actuellement connecté est autorisé à supprimer cet utilisateur
        if (userToDelete.getUsername().equals(currentPrincipalName)) {
            // L'utilisateur actuel ne peut pas supprimer son propre utilisateur
            redirectAttributes.addFlashAttribute("error", "Vous ne pouvez pas supprimer votre propre utilisateur.");
            return "redirect:/user/list";
        }

        // Supprimer l'utilisateur de la base de données
        userRepository.delete(userToDelete);

        // Redirection vers la liste des utilisateurs après la suppression
        return "redirect:/user/list";
    }


}
