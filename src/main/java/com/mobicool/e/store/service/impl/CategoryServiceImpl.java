package com.mobicool.e.store.service.impl;

import com.mobicool.e.store.dto.CategoryDto;
import com.mobicool.e.store.dto.PageableResponse;
import com.mobicool.e.store.entity.Category;
import com.mobicool.e.store.exception.ResourceNotFoundException;
import com.mobicool.e.store.helper.Helper;
import com.mobicool.e.store.repository.CategoryRepo;
import com.mobicool.e.store.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepo.save(category);
        return mapper.map(savedCategory,CategoryDto.class);
    }

   @Override // method pending
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        //get category of given id
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found exception"));

        //update category details
        category.setTitle(category.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepo.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found exception"));
        categoryRepo.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<Category> page = categoryRepo.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {

        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found exception"));

        return mapper.map(category,CategoryDto.class);
    }
}
