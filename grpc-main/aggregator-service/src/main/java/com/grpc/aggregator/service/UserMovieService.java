package com.grpc.aggregator.service;

import com.grpc.aggregator.dto.RecommendedMovie;
import com.grpc.aggregator.dto.UserGenre;
import com.sid.grpc.movie.MovieSearchRequest;
import com.sid.grpc.movie.MovieSearchResponse;
import com.sid.grpc.movie.MovieServiceGrpc;
import com.sid.grpc.user.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<RecommendedMovie> getUserMovieSuggestion(String loginId){
        UserSearchRequest userSearchRequest =UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse =this.userStub.getUserGenre(userSearchRequest);
        MovieSearchRequest movieSearchRequest=MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse=this.movieStub.getMovies(movieSearchRequest);
        return movieSearchResponse.getMovieList()
                .stream()
                .map(movieDto -> new RecommendedMovie(movieDto.getTitle(),movieDto.getYear(),movieDto.getRating()))
                .collect(Collectors.toList());

    }
    public void setUserGenre(UserGenre userGenre){
        UserGenreUpdateRequest userGenreUpdateRequest=UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        UserResponse userResponse=this.userStub.updateUserGenre(userGenreUpdateRequest);

    }
}
