package com.impt.bibliotheque.controller;


import com.impt.bibliotheque.model.Author;
import com.impt.bibliotheque.model.Book;
import com.impt.bibliotheque.service.AuthorService;
import com.impt.bibliotheque.service.BookService;
import com.impt.bibliotheque.service.CategoryService;
import com.impt.bibliotheque.service.PublisherService;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BookController {

    final BookService bookService;
    final AuthorService authorService;
    final CategoryService categoryService;
    final PublisherService publisherService;

    public BookController(PublisherService publisherService, CategoryService categoryService, BookService bookService,
                          AuthorService authorService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
    }
    /************************
     *
     * Affiche list all Livres
     *
     ************************/
    @RequestMapping({ "/books", "/" })
    public String findAllBooks(Model model, @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {

        var currentPage = page.orElse(1);
        var pageSize = size.orElse(5);

        var bookPage = bookService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("books", bookPage);

        var totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "book/list-books";
    }
    /************************
     *
     * Chercher un Livre
     *
     ************************/

    /*
    @RequestMapping("/searchBook")
    public String searchBook(@Param("keyword") String keyword, Model model) {

        model.addAttribute("books", bookService.searchBooks(keyword));
        model.addAttribute("keyword", keyword);
        return "book/list-books";
    }

     */
    /************************
     *
     * Affiche  un Livre par Id
     *
     ************************/
    @RequestMapping("/book/{id}")
    public String findBookById(@PathVariable("id") Long id, Model model) {

        model.addAttribute("book", bookService.findBookById(id));
        return "book/list-book";
    }

    /************************
     *
     * Ajouter un Livre
     *
     ************************/
    @GetMapping("/addbook")
    public String showCreateForm(Book book, Model model) {
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("authors", authorService.findAllAuthors());
        model.addAttribute("publishers", publisherService.findAllPublishers());
        return "book/add-book";
    }

    @PostMapping("/addbook")
    public String createBook(@Valid Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("authors", authorService.findAllAuthors());
            model.addAttribute("publishers", publisherService.findAllPublishers());
            return "book/add-book";
        }

        bookService.createBook(book);
        model.addAttribute("book", bookService.findAllBooks());
        return "redirect:/books";
    }

    /************************
     *
     * Modifier un Livre
     *
     ************************/
    @GetMapping("/updatebook/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        return "book/update-book";
    }


    @RequestMapping("/updatebook/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid @ModelAttribute("book") Book updateBook, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-book";
        }
        Book existingBook = bookService.findBookById(id);
        existingBook.setName_book(updateBook.getName_book());
        existingBook.setSerialName(updateBook.getSerialName());
        existingBook.setGenre(updateBook.getGenre());
        existingBook.setDescription(updateBook.getDescription());
        bookService.updateBook(existingBook);
        model.addAttribute("book",bookService.findAllBooks());

        return "redirect:/books";
    }


    /************************
     *
     * Supprimer un Livre
     *
     ************************/
    @RequestMapping("/remove-book/{id}")
    public String deleteBook(@PathVariable("id") Long id, Model model) {
        bookService.deleteBook(id);

        model.addAttribute("book", bookService.findAllBooks());
        return "redirect:/books";
    }

}
