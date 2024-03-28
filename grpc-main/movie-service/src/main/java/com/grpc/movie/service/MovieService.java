package com.grpc.movie.service;

import com.grpc.movie.entity.Movie;
import com.grpc.movie.repository.MovieRepository;
import com.sid.grpc.movie.MovieDto;
import com.sid.grpc.movie.MovieSearchRequest;
import com.sid.grpc.movie.MovieSearchResponse;
import com.sid.grpc.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
       List<MovieDto> movieDtoList= this.repository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                .stream()
                .map(movie ->
                    MovieDto.newBuilder()
                            .setTitle(movie.getTitle())
                            .setYear(movie.getYear())
                            .setRating(movie.getRating())
                            .build()).collect(Collectors.toList());
        responseObserver.onNext(MovieSearchResponse.newBuilder().addAllMovie(movieDtoList).build());
        responseObserver.onCompleted();
    }

    @Autowired
    private MovieRepository repository;

}
