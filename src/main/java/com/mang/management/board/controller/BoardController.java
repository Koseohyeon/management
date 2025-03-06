package com.mang.management.board.controller;

import com.mang.management.board.entity.Board;
import com.mang.management.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping
    public List<Board> getAllBoards(){
        return boardRepository.findAll();
    }

    @PostMapping
    public Board createBoard(@RequestBody Board board){
        return boardRepository.save(board);
    }

    @DeleteMapping("/{id}")
    public void  deleteBoard(@PathVariable Long id){
        boardRepository.deleteById(id);
    }

}
