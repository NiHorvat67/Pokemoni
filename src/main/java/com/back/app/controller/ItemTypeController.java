package com.back.app.controller;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.back.app.model.ItemType;
import com.back.app.service.ItemTypeService;
import com.back.app.service.ItemTypeService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Item Types", description = "Operations related to item types")
@RestController
@RequestMapping("/api/itemtypes")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemTypeController {

    private final ItemTypeService itemtypeService;

    @Operation(
        summary = "Retrieve all item types", 
        description = "Returns all itemtypes"
    )
    @GetMapping("/")
    public ResponseEntity<List<ItemType>> getAllTypeName() {
        return ResponseEntity.ok().body(itemtypeService.getAllItemTypes());
    }

    @Operation(
        summary = "Retrieve itemtypes by ID", 
        description = "Returns the details for a specific itemtype ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ItemType> getTypeIdName(@PathVariable Integer id) {
        return ResponseEntity.ok().body(itemtypeService.getItemTypebyId(id));
    }

   
}