package com.maahi.recipe.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maahi.recipe.exception.ResourceNotFoundException;
import com.maahi.recipe.model.AuthRequest;
import com.maahi.recipe.model.AuthResponse;
import com.maahi.recipe.model.Employee;
import com.maahi.recipe.model.Recipe;
import com.maahi.recipe.model.User;
import com.maahi.recipe.repository.EmployeeRepository;
import com.maahi.recipe.repository.RecipeRepository;
import com.maahi.recipe.repository.UserRepository;
import com.maahi.recipe.utils.JwtUtil;
import com.maahi.recipe.utils.PasswordEncoderUtils;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1")
public class RecipeController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private EmployeeRepository employeeRepostory;
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoderUtils pass;
	
	//get all employees
	
	@GetMapping("/employees")
	public List<Employee> getAllEmployees(){
		return employeeRepostory.findAll();
	}
	
//	@PostMapping("/employees")
//	public Employee createEmployee(@RequestBody Employee recipe) {
//		return employeeRepostory.save(recipe);
//	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassWord()));

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUserName());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		User user = (User)authentication.getPrincipal();

		return ResponseEntity.ok(new AuthResponse(jwt));
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authrequest) {
		User user = new User();
		user.setUserName(authrequest.getUserName());
		user.setPassword(pass.encode(authrequest.getPassWord()));
		user.setRole("USER");
		userRepository.save(user);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authrequest.getUserName());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthResponse(jwt));
	}
	
	@PostMapping("/recipes")
	public void createRecipe(@RequestBody List<Recipe> recipe) {
	recipeRepository.saveAll(recipe);
	}
	
	@GetMapping("/recipes")
	public List<Recipe> getAllRecipes(){
		List<Recipe> list = recipeRepository.findAll();
		return list;
	}
	
	@PostMapping("/recipes/update")
	public void updateRecipes(@RequestBody List<Recipe> recipes) {
	    for (Recipe recipe : recipes) {
	        if (recipe.getId() != null && recipeRepository.existsById(recipe.getId())) {
	            recipeRepository.save(recipe); // Update existing recipe
	        } else {
	            throw new ResourceNotFoundException("Recipe with ID " + recipe.getId() + " not found");
	        }
	    }
	}
	
	@DeleteMapping("/recipes/delete/{id}")
	public List<Recipe> deleteRecipe(@PathVariable("id") long id) {
		Optional<Recipe> recipe = recipeRepository.findById(id);
		if (recipe.isPresent()) {
	        recipeRepository.delete(recipe.get());
	    } else {
	        throw new ResourceNotFoundException("Recipes with ID " + id + " not found");
	    }
		List<Recipe> list = getAllRecipes();
		return list;
	}

}
