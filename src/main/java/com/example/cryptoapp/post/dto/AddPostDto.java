package com.example.cryptoapp.post.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddPostDto {
    @NotEmpty(message = "cannot be empty")
    @Size(max = 100)
    private String title;
    @Size(max = 2000)
    @NotEmpty(message = "cannot be empty")
    private String content;
}
