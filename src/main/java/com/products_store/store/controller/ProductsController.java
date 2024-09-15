package com.products_store.store.controller;

import com.products_store.store.model.Product;
import com.products_store.store.model.ProductDto;
import com.products_store.store.services.ProductsRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductsRepo repo;
@GetMapping({"","/"})
    public String showproductsList(Model model)
    {
        List<Product> products=repo.findAll();
        model.addAttribute("products",products);
        return "products/index";
    }
@GetMapping("/create")
    public String showCreatePage(Model model)
    {
        ProductDto productDto=new ProductDto();
        model.addAttribute("productDto",productDto);
        return "products/CreateProduct";
    }
    @PostMapping("/create")
    public String CreateProduct( @Valid @ModelAttribute ProductDto productDto,
    BindingResult result)
    {
       if (productDto.getImgFileName().isEmpty())
       {
           result.addError(new FieldError("productDto","imgFileName","The image file is required"));
       }
       if (result.hasErrors())
       {
           return "products/CreateProduct";
       }
       //Save image
        MultipartFile image = productDto.getImgFileName();
       Date createdAt=new Date();
       String storageFileName= createdAt.getTime()+"_"+image.getOriginalFilename();

       try {
           String uploadDir = "public/images/";
           Path uploadPath = Paths.get(uploadDir);
           if (!Files.exists(uploadPath)) {
               Files.createDirectories(uploadPath);
           }

           try (InputStream inputStream=image.getInputStream())
           {
           Files.copy(inputStream,Paths.get(uploadDir+storageFileName),
                   StandardCopyOption.REPLACE_EXISTING);

           }
       } catch (IOException e) {
           System.out.println("Exception"+e.getMessage());
       }
       Product product=new Product();
       product.setName(productDto.getName());
       product.setBrand(productDto.getBrand());
       product.setCategory(productDto.getCategory());
       product.setPrice(productDto.getPrice());
       product.setDescription(productDto.getDescription());
       product.setCreatedAt(createdAt);
       product.setImgFileName(storageFileName);
       repo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String ShowEditPage(
            Model model,
            @RequestParam int id
    )
    {
        try {
            Product product=repo.findById(id).get();
            model.addAttribute("product",product);

            ProductDto productDto=new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto",productDto);
        }
        catch (Exception e)
        {
            System.out.println("Exception"+e.getMessage());
            return "redirect:/products";
        }
        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String UpdateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    )
    {
        try {
            Product product = repo.findById(id).get();
            model.addAttribute("product", product);
            if (result.hasErrors()) {
                return "products/EditProduct";
            }
            if (!productDto.getImgFileName().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagPath = Paths.get(uploadDir + product.getImgFileName());

                try {
                    Files.delete(oldImagPath);
                } catch (Exception e) {
                    System.out.println("Exception" + e.getMessage());
                }


                MultipartFile image = productDto.getImgFileName();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);

                }
                product.setImgFileName(storageFileName);
            }
            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            repo.save(product);
        }
            catch (Exception e) {
                System.out.println("Exception" + e.getMessage());
        }

        return "redirect:/products";

    }
@GetMapping("/delete")
    public String deleteProducts(@RequestParam int id)
    {
        try {
            Product product=repo.findById(id).get();
         Path imgPath=Paths.get("public/images"+product.getImgFileName());

         try {
             Files.delete(imgPath);
         }catch (Exception e)
         {
             System.out.println("Exception" + e.getMessage());

         }

         repo.delete(product);

        }
        catch (Exception e)
        {
            System.out.println("Exception" + e.getMessage());

        }
        return "redirect:/products";
    }

}
