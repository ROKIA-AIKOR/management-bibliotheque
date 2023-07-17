package com.impt.bibliotheque.controller;


import com.impt.bibliotheque.model.Category;
import com.impt.bibliotheque.model.Publisher;
import com.impt.bibliotheque.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class PublisherController {

	final PublisherService publisherService;

	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;

	}
	/************************
	 *
	 * Afficher all Publishers
	 *
	 ************************/
	@RequestMapping("/publishers")
	public String findAllPublishers(Model model) {

		model.addAttribute("publishers", publisherService.findAllPublishers());
		return "publisher/list-publishers";
	}
	/************************
	 *
	 * Afficher un Publisher par id
	 *
	 ************************/
	@RequestMapping("/publisher/{id}")
	public String findPublisherById(@PathVariable("id") Long id, Model model) {

		model.addAttribute("publisher", publisherService.findPublisherById(id));
		return "publisher/list-publisher";
	}
	/************************
	 *
	 * Ajouter un Publisher
	 *
	 ************************/
	@GetMapping("/addPublisher")
	public String showCreateForm(Publisher publisher) {
		return "publisher/add-publisher";
	}

	@RequestMapping("/addPublisher")
	public String createPublisher(Publisher publisher, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "publisher/add-publisher";
		}

		publisherService.createPublisher(publisher);
		model.addAttribute("publisher", publisherService.findAllPublishers());
		return "redirect:/publishers";
	}
	/************************
	 *
	 * Modifier un Publisher
	 *
	 ************************/
	@GetMapping("/updatePublisher/{id}")
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		Publisher publisher= publisherService.findPublisherById(id);
		model.addAttribute("publisher", publisher);
		return "publisher/update-publisher";
	}


	@RequestMapping("/updatePublisher/{id}")
	public String updatePublisher(@PathVariable("id") Long id, @Valid @ModelAttribute("publisher") Publisher updatePublisher, BindingResult result, Model model) {

			if (result.hasErrors()) {
			return "publisher/update-publishers";
		}
		Publisher existingPublisher = publisherService.findPublisherById(id);
		existingPublisher.setName(updatePublisher.getName());
		publisherService.updatePublisher(existingPublisher);
		model.addAttribute("publisher", publisherService.findAllPublishers());
		return "redirect:/publishers";
	}


	/************************
	 *
	 * Supprimer un Publisher
	 *
	 ************************/
	@RequestMapping("/remove-publisher/{id}")
	public String deletePublisher(@PathVariable("id") Long id, Model model) {
		publisherService.deletePublisher(id);

		model.addAttribute("publisher", publisherService.findAllPublishers());
		return "redirect:/publishers";
	}

}
