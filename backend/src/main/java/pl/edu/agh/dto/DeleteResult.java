package pl.edu.agh.dto;

import java.util.Optional;

public record DeleteResult(boolean ok, int status, Optional<Exception> exception) {
}
