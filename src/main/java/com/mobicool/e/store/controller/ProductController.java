package com.mobicool.e.store.controller;

import com.mobicool.e.store.dto.ApiResponseMessage;
import com.mobicool.e.store.dto.ImageResponse;
import com.mobicool.e.store.dto.PageableResponse;
import com.mobicool.e.store.dto.ProductDto;
import com.mobicool.e.store.service.FileService;
import com.mobicool.e.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    //create

    /**
     * @author Pankaj Gosavi
     * @apiNote this api is to create product
     * @param productDto
     * @return
     */

    @PostMapping("/")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {
        ProductDto createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //update

    /**
     * @author Pankaj Gosavi
     * @apiNote this api is to update product
     * @param productId
     * @param productDto
     * @return
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,@RequestBody ProductDto productDto)
    {
        ProductDto updatedProduct = productService.update(productDto,productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.CREATED);
    }

    //delete

    /**
     * @author Pankaj Gosavi
     * @apiNote this api is to delete product
     * @param productId
     * @return
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity <ApiResponseMessage> delete(@PathVariable String productId)
    {
        productService.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product is deleted Successfully").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //get single

    /**
     * @author Pankaj Gosavi
     * @apiNote this api is to get product
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity <ProductDto> getProduct(@PathVariable String productId)
    {
        ProductDto productDto = productService.get(productId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }


    //get all

    /**
     * @author Pankaj Gosavi
     * @apiNote this api is to get all products
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //get all live

    /**
     * @author Pankaj Gosavi
     * @apiNote this api is to get all live products
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //search all

    /**
     * @author Pankaj Gosavi
     * @apiNote this api is to search product
     * @param query
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */

    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @PathVariable String query,
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(query,pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //Upload Image

    /**
     * @author Pankaj Gosavi
     * @apiNote this api is to upload product image
     * @param productId
     * @param image
     * @return
     * @throws IOException
     */

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage")MultipartFile image
            ) throws IOException {
        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.get(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName())
                .message("Product image is successfully Uploaded").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

}
