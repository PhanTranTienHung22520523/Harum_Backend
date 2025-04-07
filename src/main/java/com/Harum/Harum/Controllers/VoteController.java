package com.Harum.Harum.Controllers;

import com.Harum.Harum.Models.Views;
import com.Harum.Harum.Models.Votes;
import com.Harum.Harum.Services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;


}
