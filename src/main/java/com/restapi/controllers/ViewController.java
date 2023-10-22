package com.restapi.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.entities.Post;
import com.blogapp.entities.User;
import com.blogapp.services.PostServices;
import com.blogapp.services.TagServices;
import com.blogapp.services.UserServices;

@RestController
public class ViewController {

	@Autowired
	UserServices userServices;

	@Autowired
	PostServices postServices;

	@Autowired
	TagServices tagService;

	@GetMapping("/")
	public ResponseEntity<CustomResponse> home(Model model) {
		return findPaginated(model, 1);
	}

	@GetMapping("/account/post")
	public ResponseEntity<String> post() {
		String responseBody = "Welcome to the home page";
	    return ResponseEntity.ok(responseBody);
	}

	@GetMapping("/register")
	public ResponseEntity<String> register() {
		String responseBody = "Welcome to the register page";
	    return ResponseEntity.ok(responseBody);
	}

	@GetMapping("/showLoginPage")
	public ResponseEntity<String> login(Authentication authentication) {
		if (authentication != null) {
			String responseBody = "User already logged in";
		    return ResponseEntity.ok(responseBody);
		}
		String responseBody = "Welcome to the register page";
	    return ResponseEntity.ok(responseBody);
	}

	@PostMapping("/account/admincontroll")
	public ResponseEntity<Post> showAdminController(@RequestParam("postId") int postId) {
	    Post post = postServices.getPostById(postId);
	    
	    if (post != null) {
	        return ResponseEntity.ok(post);
	    } else {
	        return ResponseEntity.notFound().build(); // Return a 404 Not Found status if the post is not found
	    }
	}

	@GetMapping("/page/{pageNo}")
	public ResponseEntity<CustomResponse> findPaginated(Model model,@PathVariable("pageNo") int pageNo) {
	    int pageSize = 4;
	    Page<Post> page = postServices.findPaginated(pageNo, pageSize);
	    Set<String> tags = tagService.getAllTagsName();
	    Set<String> authors = userServices.getallAuthorName();
	    List<Post> posts = page.getContent();

	    CustomResponse customResponse = new CustomResponse(pageNo, page.getTotalPages(), page.getTotalElements(), posts, tags, authors);

	    if (posts != null) {
	        return ResponseEntity.ok(customResponse);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}


	@GetMapping("/search")
    public CustomResponse search(@RequestParam("search") String search,
                                 @RequestParam("pageNo") int pageNo,
                                 Authentication authentication) {
        int pageSize = 4;
        Set<Post> searchPosts = postServices.searchPostPage(search, pageNo, pageSize);
        Set<String> tags = tagService.getAllTagsName();
        Set<String> authors = userServices.getallAuthorName();

        CustomResponse response = new CustomResponse(pageNo, (searchPosts.size() / pageSize) + 1, searchPosts.size(), 
                                                     new ArrayList<>(searchPosts), tags, authors);

        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userServices.findByEmail(username);
            response.setUser(user);
        }

        return response;
    }

	@GetMapping("/sortpost")
    public CustomResponse sortPost(@RequestParam("sortValue") String sortType,
                                   @RequestParam("pageNo") int pageNo,
                                   Authentication authentication) {
        int pageSize = 4;
        Page<Post> page = postServices.sortPost(pageNo, pageSize, sortType);
        Set<String> tags = tagService.getAllTagsName();
        Set<String> authors = userServices.getallAuthorName();
        List<Post> posts = page.getContent();

        CustomResponse response = new CustomResponse(pageNo, page.getTotalPages(), (int) page.getTotalElements(), 
                                                     new ArrayList<>(posts), tags, authors);
        response.setSortPaging(true);
        response.setSortType(sortType);

        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userServices.findByEmail(username);
            response.setUser(user);
        }

        return response;
    }

	 @PostMapping("/homefilter")
	    public CustomResponse processForm(@RequestParam("searchText") String searchText,
	                                  @RequestParam(value = "authors", required = false) String[] authors,
	                                  @RequestParam(value = "tags", required = false) String[] tags,
	                                  @RequestParam("startDate") String startDate,
	                                  @RequestParam("endDate") String endDate,
	                                  @RequestParam("pageNo") int pageNo,
	                                  Authentication authentication) {
	        int pageSize = 4;
	        System.out.println(searchText);
	        Set<Post> posts = postServices.filterPost(authors, tags, startDate, endDate, pageNo, pageSize, searchText);
	        Set<String> navtags = tagService.getAllTagsName();
	        Set<String> navauthor = userServices.getallAuthorName();

	        CustomResponse response = new CustomResponse(pageNo, (int) Math.ceil(posts.size() / (double) pageSize), posts.size(),
	                                                     new ArrayList<>(posts), navtags, navauthor);
	        response.setFilterPage(true);
	        response.setFilterTags(Arrays.asList(tags));
	        response.setFilterAuthor(Arrays.asList(authors));
	        response.setStartDate(startDate);
	        response.setEndDate(endDate);
	        response.setSearchText(searchText);

	        if (authentication != null) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            String username = userDetails.getUsername();
	            User user = userServices.findByEmail(username);
	            response.setUser(user);
	        }

	        return response;
	    }

}
