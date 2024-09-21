package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;

@Controller
@RequestMapping("notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping()
    public String addNote(Authentication authentication, @ModelAttribute("newNote") Note newNote, Model model) {
        String userName = authentication.getName();
        String newTitle = newNote.getNoteTitle();
        Integer noteId = newNote.getNoteId();
        String newDescription = newNote.getNoteDescription();
        if (noteId == null) {
            noteService.addNote(newTitle, newDescription, userName);
        } else {
            Note existingNote = getNote(noteId);
            noteService.updateNote(existingNote.getNoteId(), newTitle, newDescription);
        }
        model.addAttribute("success", true);

        return "result";
    }

    @GetMapping(value = "/{noteId}")
    public Note getNote(@PathVariable Integer noteId) {
        return noteService.getNote(noteId);
    }

    @GetMapping(value = "/delete/{noteId}")
    public String deleteNote(Authentication authentication, @PathVariable Integer noteId, Model model) {
        noteService.deleteNote(noteId);
        model.addAttribute("success", true);

        return "result";
    }
}
