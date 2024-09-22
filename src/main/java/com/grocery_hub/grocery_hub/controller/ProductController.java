package com.grocery_hub.grocery_hub.controller;

import com.grocery_hub.grocery_hub.constants.SuperMarketEnum;
import com.grocery_hub.grocery_hub.factory.ProductServiceFactory;
import com.grocery_hub.grocery_hub.model.ProductDTO;
import com.grocery_hub.grocery_hub.model.SendNotificationReqDTO;
import com.grocery_hub.grocery_hub.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductServiceFactory productServiceFactory;

    @Autowired
    public ProductController(ProductServiceFactory productServiceFactory) {
        this.productServiceFactory = productServiceFactory;
    }

    @Operation(summary = "Get all products", description = "Returns a list of all products available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<ProductDTO> getAllProducts(
            @Parameter(description = "Supermarket filter")
            @RequestParam(value = "market", required = false) SuperMarketEnum market,
            @RequestParam(value = "searchName", required = false) String searchName
    ) throws Exception {
        SuperMarketEnum selectedMarket = market != null ? market : SuperMarketEnum.ALL;

        ProductService productService = productServiceFactory.getProductService(selectedMarket);

        return productService.getProducts(searchName);
    }

    @Operation(summary = "Send notification", description = "Send notification to the specified email")
    @PostMapping("/notification")
    public void sendNotification(
            @RequestBody @Valid SendNotificationReqDTO req
    ) {
        System.out.println("Sending notification to " + req.getEmail());
        req.getProducts().forEach(product -> {
            System.out.println("Product: " + product.getName());
        });
    }
}
