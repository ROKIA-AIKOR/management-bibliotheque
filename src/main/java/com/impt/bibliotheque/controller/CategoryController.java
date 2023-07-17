package com.impt.bibliotheque.controller;


import com.impt.bibliotheque.model.Book;
import com.impt.bibliotheque.model.Category;
import com.impt.bibliotheque.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class CategoryController {

	final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;

	}

	/************************
	 *
	 * Affiche tous  les Categoriers
	 *
	 ************************/
	@RequestMapping("/categories")
	public String findAllCategories(Model model) {

		model.addAttribute("categories", categoryService.findAllCategories());
		return "category/list-categories";
	}

	/************************
	 *
	 * Affiche un Categorier par Id
	 *
	 ************************/
	@RequestMapping("/category/{id}")
	public String findCategoryById(@PathVariable("id") Long id, Model model) {

		model.addAttribute("category", categoryService.findCategoryById(id));
		return "category/list-category";
	}

	/************************
	 *
	 * Ajouter un Categorier
	 *
	 ************************/
	@GetMapping("/addCategory")
	public String showCreateForm(Category category) {
		return "category/add-category";
	}

	@RequestMapping("/addcategory")
	public String createCategory(Category category, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "category/add-category";
		}

		categoryService.createCategory(category);
		model.addAttribute("category", categoryService.findAllCategories());
		return "redirect:/categories";
	}

	/************************
	 *
	 * Modifier un Categorier
	 *
	 ************************/
	@GetMapping("/updateCategory/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		Category category= categoryService.findCategoryById(id);
		model.addAttribute("category", category);
		return "category/update-category";
	}

	@RequestMapping("/updatecategory/{id}")
	public String updateCategory(@PathVariable("id") Long id, @Valid @ModelAttribute("book") Category updateCategory, BindingResult result, Model model) {
			if (result.hasErrors()) {
			return "category/update-category";
		    }

			Category existingCategory = categoryService.findCategoryById(id);
		existingCategory.setName(updateCategory.getName());
		categoryService.updateCategory(existingCategory);
		model.addAttribute("category", categoryService.findAllCategories());
		   return "redirect:/categories";
	}


	/************************
	 *
	 * Supprimer un Categorier
	 *
	 ************************/
	@RequestMapping("/remove-category/{id}")
	public String deleteCategory(@PathVariable("id") Long id, Model model) {
		categoryService.deleteCategory(id);

		model.addAttribute("category", categoryService.findAllCategories());
		return "redirect:/categories";
	}

}
