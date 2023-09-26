package com.learning.learningSpring.controller.poststats;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.learning.learningSpring.controller.binding.AddCommentForm;
import com.learning.learningSpring.controller.binding.AddPostForm;
import com.learning.learningSpring.controller.exceptions.ResourceNotFoundException;
import com.learning.learningSpring.entity.Comment;
import com.learning.learningSpring.entity.LikeId;
import com.learning.learningSpring.entity.LikeRecord;
import com.learning.learningSpring.entity.Post;
import com.learning.learningSpring.entity.User;
import com.learning.learningSpring.model.RegistrationForm;
import com.learning.learningSpring.repository.CommentRepository;
import com.learning.learningSpring.repository.LikeCRUDRepository;
import com.learning.learningSpring.repository.PostRepository;
import com.learning.learningSpring.repository.UserRepository;
import com.learning.learningSpring.service.DomainUserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;

@RestController
@CrossOrigin (origins = {"http://localhost:4200"})
@RequestMapping("/api")
public class ForumRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private DomainUserService domainUserService;

    @Autowired
    private LikeCRUDRepository likeCRUDRepository;

    @Autowired
    private CommentRepository commentRepository;

	
	@GetMapping("post/{id}")
    public ResponseEntity<Post> getPostDetail(@PathVariable int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<String> addCommentToPost(@PathVariable int id,
            @RequestBody AddCommentForm commentForm, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Comment comment = new Comment();
            comment.setContent(commentForm.getContent());
            comment.setPost(post.get());
            comment.setUser(domainUserService.getByName(userDetails.getUsername()).get());
            commentRepository.save(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }
    }

	// @PostMapping("/post/{id}/tags")
    // public ResponseEntity<?> addTagsToPost(@PathVariable int id, @RequestBody List<String> tagNames) {
    //     Optional<Post> postOptional = postRepository.findById(id);
    //     if (postOptional.isPresent()) {
    //         Post post = postOptional.get();
    //         Set<Tag> tags = new HashSet<>();

    //         for (String tagName : tagNames) {
    //             // Check if the tag already exists
    //             Optional<Tag> tagOptional = tagRepository.findByName(tagName);
    //             if (tagOptional.isPresent()) {
    //                 tags.add(tagOptional.get());
    //             } else {
    //                 Tag newTag = new Tag();
    //                 newTag.setName(tagName);
    //                 tagRepository.save(newTag);
    //                 tags.add(newTag);
    //             }
    //         }

    //         post.setTags(tags);
    //         postRepository.save(post);
    //         return ResponseEntity.status(HttpStatus.OK).body("Tags added to the post.");
    //     } else {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
    //     }
    // }

    // @GetMapping("/posts-by-tag")
    // public ResponseEntity<List<Post>> getPostsByTag(@RequestParam String tagName) {
    //     List<Post> posts = postRepository.findByTagsName(tagName);
    //     return ResponseEntity.status(HttpStatus.OK).body(posts);
    // }

    // @GetMapping("/post/{id}")
    // public ResponseEntity<?> getPostDetail(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) {
    //     Optional<Post> postOptional = postRepository.findById(id);
    //     if (postOptional.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No post with the requested ID");
    //     }
        
    //     Post post = postOptional.get();
    //     List<Comment> commentList = commentRepository.findAllByPostId(id);
    //     int numLikes = likeCRUDRepository.countByLikeIdPost(post);

    //     Map<String, Object> response = new HashMap<>();
    //     response.put("post", post);
    //     response.put("commentList", commentList);
    //     response.put("likerName", userDetails.getUsername());
    //     response.put("likeCount", numLikes);

    //     return ResponseEntity.ok(response);
    // }

    @PostMapping("/post")
    public ResponseEntity<?> createNewPost(@RequestBody AddPostForm postForm, @AuthenticationPrincipal UserDetails userDetails) {
        User author = domainUserService.getByName(userDetails.getUsername()).orElse(null);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(postForm.getContent());
        postRepository.save(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

 // Other REST endpoints for updating, deleting, and other operations can be added similarly.
// You can also create REST endpoints for handling comments and likes.


	@PostMapping("/post/{id}/like")
	public String postLike(@PathVariable int id, String likerName, RedirectAttributes attr) {
		LikeId likeId = new LikeId();
		likeId.setUser(userRepository.findByName(likerName).get());
		likeId.setPost(postRepository.findById(id).get());
		LikeRecord like = new LikeRecord();
		like.setLikeId(likeId);
		likeCRUDRepository.save(like);
		return String.format("redirect:/forum/post/%d", id);
	}


	@PostMapping("/post/{postId}/like-unlike-comment/{commentId}")
	public String likeUnlikeComment(@PathVariable("postId") int postId,
			@PathVariable("commentId") int commentId,
			@RequestParam("likedByUser") boolean likedByUser) {
		// Retrieve the comment by its ID
		Optional<Comment> commentOptional = commentRepository.findById(commentId);

		if (commentOptional.isPresent()) {
			Comment comment = commentOptional.get();

			// Update the likedByUser field
			comment.setLikedByUser(!likedByUser);

			// Update the likes count based on likedByUser
			int likes = comment.getLikes();
			if (!likedByUser) {
				likes++;
			} else {
				likes--;
			}
			comment.setLikes(likes);

			// Save the updated comment
			commentRepository.save(comment);

			// Redirect back to the post detail page
			return "redirect:/forum/post/" + postId;
		} else {
			// Handle the case where the comment with the given ID is not found
			return "redirect:/forum"; // or wherever you want to redirect
		}
	}

	@GetMapping("/register")
	public String getRegistrationForm(Model model) {
		if (!model.containsAttribute("registrationForm")) {
			model.addAttribute("registrationForm", new RegistrationForm());
		}
		return "forum/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("registrationForm") RegistrationForm registrationForm,
			BindingResult bindingResult,
			RedirectAttributes attr) {
		if (bindingResult.hasErrors()) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
			attr.addFlashAttribute("registrationForm", registrationForm);
			return "redirect:/register";
		}
		if (!registrationForm.isValid()) {
			attr.addFlashAttribute("message", "Passwords must match");
			attr.addFlashAttribute("registrationForm", registrationForm);
			return "redirect:/register";
		}
		domainUserService.save(registrationForm.getUsername(), registrationForm.getPassword());
		attr.addFlashAttribute("result", "Registration success!");
		return "redirect:/login";
	}

}
