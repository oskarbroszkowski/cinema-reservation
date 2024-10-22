package com.example.cinema_reservation.controller;

import com.example.cinema_reservation.model.Movie;
import com.example.cinema_reservation.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/movies")
public class AdminMovieController {
    private final MovieService movieService;

    @Autowired
    public AdminMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public String showMovies(Model model) {
        List<Movie> movies = movieService.findAllMovies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Movie movie : movies) {
            movie.setFormattedReleaseDate(movie.getReleaseDate().format(formatter));
        }

        model.addAttribute("movies", movies);
        return "admin-movies";
    }

    @PostMapping("/add")
    public String addMovie(@RequestParam("title") String title,
                           @RequestParam("genre") String genre,
                           @RequestParam("duration") int duration,
                           @RequestParam("releaseDate") LocalDate releaseDate,
                           @RequestParam("rating") double rating,
                           @RequestParam("description") String description,
                           @RequestParam("thumbnail") MultipartFile thumbnail) throws IOException {

        String filePath = saveThumbnail(thumbnail, title);

        Movie movie = new Movie(title, genre, duration, description, releaseDate, rating, filePath);
        movieService.saveMovie(movie);

        return "redirect:/admin/movies";
    }

    private String saveThumbnail(MultipartFile thumbnail, String title) {
        String uploadDir = "/Users/oskar/Desktop/cinema-reservation-main/src/main/resources/static/images";
        Path uploadPath = Paths.get(uploadDir);

        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String safeTitle = title.replaceAll(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");
        String originalFilename = thumbnail.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : ".jpg";
        String fileName = safeTitle + "_thumbnail" + fileExtension;

        Path filePath = uploadPath.resolve(fileName);

        try {
            thumbnail.transferTo(filePath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    @PostMapping("/edit/{id}")
    public String editMovie(@PathVariable Long id,
                            @RequestParam String title,
                            @RequestParam String genre,
                            @RequestParam int duration,
                            @RequestParam LocalDate releaseDate,
                            @RequestParam double rating,
                            @RequestParam String description,
                            @RequestParam(required = false) MultipartFile thumbnail,
                            RedirectAttributes redirectAttributes) {

        Optional<Movie> optionalMovie = movieService.findMovieById(id);
        if (optionalMovie.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Movie not found.");
            return "redirect:/admin/movies";
        }

        Movie movie = optionalMovie.get();

        boolean updated = false;

        if (title != null && !title.trim().isEmpty() && !title.equals(movie.getTitle())) {
            movie.setTitle(title);
            updated = true;
        }

        if (genre != null && !genre.trim().isEmpty() && !genre.equals(movie.getGenre())) {
            movie.setGenre(genre);
            updated = true;
        }

        if (duration > 0 && duration != movie.getDuration()) {
            movie.setDuration(duration);
            updated = true;
        }

        if (releaseDate != null && !releaseDate.equals(movie.getReleaseDate())) {
            movie.setReleaseDate(releaseDate);
            updated = true;
        }

        if (rating >= 0 && rating != movie.getRating()) {
            movie.setRating(rating);
            updated = true;
        }

        if (description != null && !description.trim().isEmpty() && !description.equals(movie.getDescription())) {
            movie.setDescription(description);
            updated = true;
        }


        if (thumbnail != null && !thumbnail.isEmpty()) {
            String filePath = saveThumbnail(thumbnail, title);
            movie.setThumbnailPath(filePath);
            updated = true;
        }

        if (updated) {
            movieService.saveMovie(movie);
            redirectAttributes.addFlashAttribute("successMessage", "Movie has been updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage", "No changes were made.");
        }

        return "redirect:/admin/movies";
    }

    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }
}
