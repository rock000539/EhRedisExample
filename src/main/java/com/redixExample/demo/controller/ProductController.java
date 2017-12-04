package com.redixExample.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redixExample.demo.entity.Product;

@RestController
@RequestMapping("/product")
public class ProductController {
	@GetMapping("/{id}")
	public Product getProductInfo(@PathVariable("id") Long productId) {
		// TODO
		return null;
	}

	@PutMapping("/{id}")
	public Product updateProductInfo(@PathVariable("id") Long productId, @RequestBody Product newProduct) {
		// TODO
		return null;
	}
}
