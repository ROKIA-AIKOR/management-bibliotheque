package com.impt.bibliotheque.controller;


import com.impt.bibliotheque.model.Author;
import com.impt.bibliotheque.service.AuthorService;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AuthorController {

    final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /************************
     *
     * Affiche list all autheurs
     *
     ************************/
    @GetMapping("/authors")
    public String findAllAuthors(Model model, @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {

        var currentPage = page.orElse(1);
        var pageSize = size.orElse(5);
        var bookPage = authorService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("authors", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "author/list-authors";
    }

    /************************
     *
     * Affiche un autheur par ID
     *
     ************************/
    @GetMapping("/author/{id}")
    public String findAuthorById(@PathVariable("id") Long id, Model model) {

        model.addAttribute("author", authorService.findAuthorById(id));
        return "author/detail-author";
    }

    /************************
     *
     * Ajouter un autheur
     *
     ************************/
    @GetMapping("/addAuthor")
    public String showCreateForm(Author author) {
        return "author/add-author";
    }

    @PostMapping("/addAuthor")
    public String createAuthor(Author author, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "author/addAuthor";
        }

        authorService.createAuthor(author);
        model.addAttribute("author", authorService.findAllAuthors());
        return "redirect:/authors";
    }

    /************************
     *
     * Modifier un auteur
     *
     ************************/
    @GetMapping("/updateAuthor/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Author author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        return "author/update-author";
    }

    @PostMapping("/updateAuthor/{id}")
    public String updateAuthor(@PathVariable("id") Long id, @Valid @ModelAttribute("author") Author updatedAuthor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "author/update-author";
        }

        Author existingAuthor = authorService.findAuthorById(id);
        existingAuthor.setName_author(updatedAuthor.getName_author());
        existingAuthor.setDescription(updatedAuthor.getDescription());

        authorService.updateAuthor(existingAuthor);

        model.addAttribute("authors", authorService.findAllAuthors());
        return "redirect:/authors";
    }


    /************************
     *
     * Supprimer un autheur
     *
     ************************/
    @RequestMapping("/remove-author/{id}")
    public String deleteAuthor(@PathVariable("id") Long id, Model model) {
        authorService.deleteAuthor(id);

        model.addAttribute("author", authorService.findAllAuthors());
        return "redirect:/authors";
    }

}

