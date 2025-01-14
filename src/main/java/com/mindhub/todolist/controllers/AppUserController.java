package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.AppUserDTO;
import com.mindhub.todolist.dtos.UpdateAppUserDto;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.services.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppUserDTO.class))))
    })
    @GetMapping
    public List<AppUserDTO> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AppUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public AppUserDTO getUserById(@PathVariable Long id) throws UserNotFoundException {
        return appUserService.getUserById(id);
    }

    @Operation(summary = "Create a new user", description = "Add a new user to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = AppUserDTO.class)))
    })
    @PostMapping
    public AppUserDTO createUser(@RequestBody AppUserDTO userDTO) {
        return appUserService.createUser(userDTO);
    }

    @Operation(summary = "Update an existing user", description = "Update the details of an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = AppUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public AppUserDTO updateUser(@PathVariable Long id, @RequestBody AppUserDTO userDTO) {
        return appUserService.updateUser(id, userDTO);
    }

    @Operation(summary = "Delete a user", description = "Remove a user from the system by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
    }

    @GetMapping("/self")
    public AppUserDTO getLoggedInUserData(Authentication authentication) throws UserNotFoundException {
        return appUserService.getLoggedInUserInfo(authentication.getName());
    }

    @PatchMapping("/self")
    public AppUserDTO updateLoggedInUserData(Authentication authentication, @RequestBody UpdateAppUserDto updateAppUserDto) throws UserNotFoundException {
        return this.appUserService.updateLoggedInUserInfo(authentication.getName(), updateAppUserDto);
    }

    @PutMapping("/self")
    public AppUserDTO replaceLoggedInUserData(Authentication authentication, @RequestBody UpdateAppUserDto updateAppUserDto) throws UserNotFoundException, BadRequestException {
        return this.appUserService.replaceLoggedInUserInfo(authentication.getName(), updateAppUserDto);
    }

    @DeleteMapping("/self")
    public ResponseEntity<String> deleteLoggedInUserData(Authentication authentication) throws UserNotFoundException {
        appUserService.deleteLoggedInUser(authentication.getName());
        return ResponseEntity.ok("User deleted successfully");
    }
}