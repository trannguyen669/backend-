package org.project4.backend.controller.api.user;

import org.project4.backend.controller.output.Movie_output;
import org.project4.backend.dto.Movie_DTO;
import org.project4.backend.dto.Orders_DTO;
import org.project4.backend.dto.User_DTO;
import org.project4.backend.service.admin_service.Movie_Service;
import org.project4.backend.service.admin_service.Order_Service;
import org.project4.backend.service.user_service.Movie_user_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/movie")
public class MovieController {
    @Autowired
    private Movie_user_service movie_user_service;
    @Autowired
    private Movie_Service movieService;
    @Autowired
    private Order_Service orderService;
    @GetMapping("/all")
    public Movie_output getAll(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movieService.getAll(pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }
    @GetMapping("/getbyname")
    public Movie_output getbyname(@RequestParam("name") String name,@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movieService.getByVn_name(name,pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }
    @GetMapping("/getbycategory")
    public Movie_output getbycategory(@RequestParam("categoryid") Long categoryid,@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movie_user_service.getByCategoryid(categoryid,pageable));
        result.setTotalPage((int) Math.ceil((double) (movieService.totalItem()) / limit));
        return result;
    }
    @GetMapping("/list")
    public Movie_output getListMovie(@RequestParam(value = "category", required = false) Long category,
                                     @RequestParam(value = "year", required = false) Long year,
                                     @RequestParam(value = "sortBy",required = false) String sortBy,
                                     @RequestParam(value = "vip",required = false) Boolean vip,
                                     @RequestParam("page") int page,
                                     @RequestParam("limit") int limit) {
        Movie_output result = new Movie_output();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(movie_user_service.getListMovie(category, year,vip, sortBy,pageable));
        result.setTotalPage((int) Math.ceil((double) (movie_user_service.totalItem()) / limit));
        return result;
    }
    @GetMapping("/checkvip")
    public ResponseEntity<?> checkvip(@RequestParam("userid") Long userid, @RequestParam("movieid") Long movieid) {
     try {
         Orders_DTO dto = orderService.getByUserIdAndMovieId(userid, movieid);
         return ResponseEntity.ok(dto);
     }catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
     }
    }
    @PostMapping("/buymovie")
    public ResponseEntity<?> buymovie(@RequestParam("userid") Long userid, @RequestParam("movieid") Long movieid) {
        try {
            User_DTO user = new User_DTO();
            user.setId(userid);
            Movie_DTO movie = new Movie_DTO();
            movie.setId(movieid);
            Orders_DTO dto = new  Orders_DTO();
            dto.setUser(user);
            dto.setMovie(movie);
            orderService.buy_Movie(dto);
            return new ResponseEntity<>("Mua phim thành công!", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
